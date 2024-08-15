package org.irods.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.serialize.ModifyPermissionsOperations;
import org.irods.properties.DataObject.*;
import org.irods.properties.Resource.ResourceCreateParams;
import org.irods.util.Permission;
import org.irods.util.MetadataOperation;
import org.irods.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.irods.util.UserType;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.*;
import static org.irods.IrodsResponseUtils.getIrodsResponseStatusCode;


public class DataObjectOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

    private String aliceToken;

    private String host;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(DataObjectOperationsTest.class);

    @Before
    public void setup() {
        host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client
        client = new IrodsHttpClient(baseUrl);

        // Authenticate rods
        Response res = client.authenticate("rods", "rods");
        rodsToken = res.getBody();

        // Create alice user
        this.client.userGroupOperations().createUser(rodsToken, "alice", "tempZone", Optional.of(UserType.RODSUSER));
        this.client.userGroupOperations().setPassword(rodsToken, "alice", "tempZone", "alicepass");
        res = client.authenticate("alice", "alicepass");
        aliceToken = res.getBody();
    }


    @Test
    public void testCommonOperations() {
        Response res;
        String lpath = "/tempZone/home/rods/common_ops.txt";
        String resc_name = "test_ufs_common_ops_resc";
        String resc_name2 = "test_ufs_common_ops_resc2";

        try {
            // Create a unixfilesystem resource.
            ResourceCreateParams prop = new ResourceCreateParams();
            prop.setHost(host);
            prop.setVaultPath("/tmp/test_ufs_common_ops_resc_vault");
            res = client.resourceOperations().create(rodsToken, resc_name, "unixfilesystem", prop);

            logger.debug(res.getBody());
            assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a unixfilesystem resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            prop = new ResourceCreateParams();
            prop.setHost(host);
            prop.setVaultPath("/tmp/test_ufs_common_ops_resc_vault");
            res = client.resourceOperations().create(rodsToken, resc_name2, "unixfilesystem", prop);

            logger.debug(res.getBody());
            assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a unixfilesystem resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Create a non-empty data object
            byte[] content = "hello, this message was written via the iRODS HTTP API!".getBytes();
            res = client.dataObject().write(rodsToken, lpath, content, null);
            logger.debug(res.getBody());
            assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a non-empty data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            res = client.dataObject().replicate(rodsToken, lpath, resc_name2, resc_name, OptionalInt.empty());
            logger.debug(res.getBody());
            assertEquals("Replicating data object request failed", 200, res.getHttpStatusCode());
            // replicating results in error code of 168000: SYS_REPLICA_INACCESSIBLE

            // Trim the first replica
            res = client.dataObject().trim(rodsToken, lpath, 0, null);
            logger.debug(res.getBody());
            assertEquals("Trimming first replica request failed", 200, res.getHttpStatusCode());
            assertEquals("Trimming first replica failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Rename the data object
            res = client.dataObject().rename(rodsToken, lpath, lpath + ".renamed");
            logger.debug(res.getBody());
            assertEquals("Renaming data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Renaming data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Copy the data object
            res = client.dataObject().copy(rodsToken, lpath + ".renamed", lpath + ".copied", null);
            logger.debug(res.getBody());
            assertEquals("Copying the data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Copying the data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Modify permissions on the data object
            res = client.dataObject().setPermission(rodsToken, lpath + ".copied", "alice",
                    "read", OptionalInt.empty());
            logger.debug(res.getBody());
            assertEquals("Modifying permissions on the data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Modifying permissions on the data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the permissions were updated
            Response statRes = client.dataObject().stat(rodsToken, lpath + ".copied", Optional.empty());
            logger.debug(statRes.getBody());
            assertEquals("Stat request failed", 200, statRes.getHttpStatusCode());
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode permissionsNode = rootNode.path("permissions");
            boolean permissionExists = false;
            for (JsonNode permission : permissionsNode) {
                if ("alice".equals(permission.path("name").asText()) &&
                        "read_object".equals(permission.path("perm").asText()) &&
                        "rodsuser".equals(permission.path("type").asText()) &&
                        "tempZone".equals(permission.path("zone").asText())) {
                    permissionExists = true;
                    break;
                }
            }
            assertTrue("Permission with specified attributes does not exist.", permissionExists);

            // Remove the data objects
        /* The data objects created in this test include:
            - /tempZone/home/rods/common_ops.txt.copied
            - /tempZone/home/rods/common_ops.txt.renamed
         */
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            res = client.dataObject().remove(rodsToken, lpath + ".copied", 0, prop3);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
            res = client.dataObject().remove(rodsToken, lpath + ".renamed", 0, prop3);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the resource
            res = client.resourceOperations().remove(rodsToken, resc_name);
            logger.debug(res.getBody());
            assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            res = client.resourceOperations().remove(rodsToken, resc_name2);
            logger.debug(res.getBody());
            assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove permissions
           client.dataObject().setPermission(rodsToken, lpath + ".copied", "alice",
                    "null", OptionalInt.empty());

            // Remove data objects
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            client.dataObject().remove(rodsToken, lpath + ".copied", 0, prop3);
            client.dataObject().remove(rodsToken, lpath + ".renamed", 0, prop3);

            // Remove resources
            client.resourceOperations().remove(rodsToken, resc_name);
            client.resourceOperations().remove(rodsToken, resc_name2);
        }
    }

    @Test
    public void testCopyOperationSupportsOverwritingExistingDataObjects() {
        Response res;
        String dataObjectA = "/tempZone/home/alice/copy_op_overwrite_param.txt.a";
        String dataObjectB = "/tempZone/home/alice/copy_op_overwrite_param.txt.b";

        try {
            // Create a non-empty data object.
            byte[] content = "some data".getBytes();
            res = client.dataObject().write(aliceToken, dataObjectA, content, null);
            logger.debug(res.getBody());
            assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a non-empty data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Create a second data object containing different information.
            content = "different data".getBytes();
            res = client.dataObject().write(aliceToken, dataObjectB, content, null);
            logger.debug(res.getBody());
            assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a non-empty data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Calculate checksums for each data object to show they are different.
            Response checksumRes = client.dataObject().calculateChecksum(aliceToken, dataObjectA, null);
            logger.debug(checksumRes.getBody());
            assertEquals("Calculating checksum for data object request failed", 200,
                    checksumRes.getHttpStatusCode());
            assertEquals("Calculating checksum for data object failed", 0,
                    getIrodsResponseStatusCode(checksumRes.getBody()));

            String expectedChecksum = "sha2:EweZDmulyhRes16ZGCqb7EZTG8VN32VqYCx4D6AkDe4=";
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(checksumRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode checksumNode = rootNode.path("checksum");
            String actualChecksum = checksumNode.asText();
            assertEquals("Checksum did not match", expectedChecksum, actualChecksum);


            Response checksumRes2 = client.dataObject().calculateChecksum(aliceToken, dataObjectB, null);
            logger.debug(checksumRes2.getBody());
            assertEquals("Calculating checksum for data object request failed", 200,
                    checksumRes2.getHttpStatusCode());
            assertEquals("Calculating checksum for data object failed", 0,
                    getIrodsResponseStatusCode(checksumRes2.getBody()));

            expectedChecksum = "sha2:YIoGizPRi+g4vLB77QHjVSHTCED6JNsJGS5nv9GG5iE=";
            rootNode = assertDoesNotThrow(() -> mapper.readTree(checksumRes2.getBody()),
                    "JsonProcessingException was thrown");
            checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
            assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

            // Show attempting to copy over an existing data object isn't allowed without the "overwrite" parameter.
            // -312000 is the associated irods error
            res = client.dataObject().copy(aliceToken, dataObjectA, dataObjectB, null);
            logger.debug(res.getBody());
            assertEquals("Copying data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Expected to get a failed message of 'OVERWRITE_WITHOUT_FORCE_FLAG'", -312000,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show copying over an existing data object is possible with the "overwrite" parameter.
            DataObjectCopyParams prop = new DataObjectCopyParams();
            prop.setOverwrite(1);
            res = client.dataObject().copy(aliceToken, dataObjectA, dataObjectB, prop);
            logger.debug(res.getBody());
            assertEquals("Copying data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Copying data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the data objects are now identical.
            // data object a
            expectedChecksum = "sha2:EweZDmulyhRes16ZGCqb7EZTG8VN32VqYCx4D6AkDe4=";
            DataObjectCalculateChecksumParams prop2 = new DataObjectCalculateChecksumParams();
            prop2.setForce(1);

            Response checksumRes3 = client.dataObject().calculateChecksum(aliceToken, dataObjectA, prop2);
            logger.debug(checksumRes3.getBody());
            assertEquals("Calculating checksum for data object request failed", 200,
                    checksumRes3.getHttpStatusCode());
            assertEquals("Calculating checksum for data object failed", 0,
                    getIrodsResponseStatusCode(checksumRes3.getBody()));

            // Checking match
            rootNode = assertDoesNotThrow(() -> mapper.readTree(checksumRes3.getBody()),
                    "JsonProcessingException was thrown");
            checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
            assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

            // data object b
            Response checksumRes4 = client.dataObject().calculateChecksum(aliceToken, dataObjectB, prop2);
            logger.debug(checksumRes4.getBody());
            assertEquals("Calculating checksum for data object request failed", 200,
                    checksumRes4.getHttpStatusCode());
            assertEquals("Calculating checksum for data object failed", 0,
                    getIrodsResponseStatusCode(checksumRes4.getBody()));

            // Checking match
            rootNode = assertDoesNotThrow(() -> mapper.readTree(checksumRes4.getBody()),
                    "JsonProcessingException was thrown");
            checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
            assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

            // Remove the data objects
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            prop3.setAdmin(1);
            res = client.dataObject().remove(rodsToken, dataObjectA, 0, prop3);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
            res = client.dataObject().remove(rodsToken, dataObjectB, 0, prop3);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data objects
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            prop3.setAdmin(1);
            client.dataObject().remove(rodsToken, dataObjectA, 0, prop3);
            client.dataObject().remove(rodsToken, dataObjectB, 0, prop3);
        }

    }

    @Test
    public void testCalculatingAndVerifyingChecksums() {
        Response res;
        String rescName = "test_ufs_checksums_resc";
        String dataObject = "/tempZone/home/rods/checksums.txt";

        try {
            // Create a unixfilesystem resource.
            ResourceCreateParams prop = new ResourceCreateParams();
            prop.setHost(host);
            prop.setVaultPath("/tmp/test_ufs_checksums_resc_vault");
            res = client.resourceOperations().create(rodsToken, rescName, "unixfilesystem", prop);

            logger.debug(res.getBody());
            assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a unixfilesystem resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Create a non-empty object
            byte[] content = "hello, this message was written via the iRODS HTTP API!".getBytes();
            res = client.dataObject().write(rodsToken, dataObject, content, null);
            logger.debug(res.getBody());
            assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating a non-empty data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Calculate checksum
            DataObjectCalculateChecksumParams prop2 = new DataObjectCalculateChecksumParams();
            prop2.setReplicaNum(0);
            Response checksumRes = client.dataObject().calculateChecksum(rodsToken, dataObject, prop2);
            logger.debug(checksumRes.getBody());
            assertEquals("Calculating checksum for data object request failed", 200,
                    checksumRes.getHttpStatusCode());
            assertEquals("Calculating checksum for data object failed", 0,
                    getIrodsResponseStatusCode(checksumRes.getBody()));

            String expectedChecksum = "sha2:1SgRcbKcy3+4fjwMvf7xQNG5OZmiYzBVbNuMIgiWbBE=";
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(checksumRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode checksumNode = rootNode.path("checksum");
            String actualChecksum = checksumNode.asText();
            assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

            // Verify checksum information
            res = client.dataObject().verifyChecksum(rodsToken, dataObject, null);
            logger.debug(res.getBody());
            assertEquals("Verifying checksum for data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Verifying checksum for data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the data objects
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            res = client.dataObject().remove(rodsToken, dataObject, 0, prop3);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the resource
            res = client.resourceOperations().remove(rodsToken, rescName);
            logger.debug(res.getBody());
            assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing resource failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data objects
            DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
            prop3.setNoTrash(1);
            client.dataObject().remove(rodsToken, dataObject, 0, prop3);

            // Remove the resource
            client.resourceOperations().remove(rodsToken, rescName);
        }
    }

    @Test
    public void testRegisteringANewDataObject() {
        Response res;
        String filename = "newly_registered_file.txt";
        String physicalPath = "/tmp/" + filename;
        String dirName = "/tempZone/home/rods";
        String dataObject = dirName + filename;
        String resource = "demoResc";

        String content = "data";
        BufferedWriter writer = assertDoesNotThrow(() -> new BufferedWriter(new FileWriter(physicalPath)));
        try {
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            fail("IOException was thrown");
        }

        try {
            // Show the data object we want to create via registration does not exist.
            res = client.dataObject().stat(rodsToken, dataObject, Optional.empty());
            logger.debug(res.getBody());
            assertEquals("Stat request failed", 200, res.getHttpStatusCode());
            // -171000 is the error code for NOT_A_DATA_OBJECT
            assertEquals("Stat to data object was supposed to failed", -171000,
                    getIrodsResponseStatusCode(res.getBody()));

            // Register local file into the catalog as a new data object
            // We know we're registering a new data object because the "as-additional-replica" parameter isn't set to 1.
            DataObjectRegisterParams prop = new DataObjectRegisterParams();
            prop.setDataSize(content.length());
            res = client.dataObject().register(rodsToken, dataObject, physicalPath, resource, prop);
            logger.debug(res.getBody());
            assertEquals("Registering a data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Registering a data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show a new data object exists with the expected replica information.
            String query = "select COLL_NAME, DATA_NAME, DATA_PATH, RESC_NAME where COLL_NAME = " +
                    "'" + dirName + "' and DATA_NAME = '" + filename + "'";
            res = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(res.getBody());
            assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Unregister the data object
            res = client.dataObject().remove(rodsToken, dataObject, 1, null);
            logger.debug(res.getBody());
            assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Unregister the data object
            client.dataObject().remove(rodsToken, dataObject, 1, null);
        }
    }

    @Test
    public void testParallelWrites() {
        Response res;
        String dataObject = "/tempZone/home/rods/parallel_write.txt";

        try {
            // Tell the server we're about to do a parallel write.
            Response writeRes = client.dataObject().parallelWriteInit(rodsToken, dataObject, 3, null);
            logger.debug(writeRes.getBody());
            assertEquals("parallel_write_init request failed", 200, writeRes.getHttpStatusCode());
            assertEquals("parallel_write_init failed", 0,
                    getIrodsResponseStatusCode(writeRes.getBody()));
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(writeRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode statusCodeNode = rootNode.path("parallel_write_handle");
            String parallelWriteHandle = statusCodeNode.asText();

            // Write to the data object using the parallel write handle.
            ExecutorService executor = Executors.newFixedThreadPool(3); // managing a pool of threads
            List<Future<Response>> futures = new ArrayList<>();
            char[] chars = {'A', 'B', 'C'};
            int count = 10;
            int index = 0;
            for (char c : chars) {
                byte[] bytes = new byte[count];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = (byte) c;
                }

                // Vars used in lambda expression should be final or effectively final
                int finalIndex = index;
                futures.add(executor.submit(() -> {
                    DataObjectWriteParams params = new DataObjectWriteParams();
                    params.setOffset(finalIndex  * count);
                    params.setStreamIndex(finalIndex);
                    params.setParallelWriteHandle(parallelWriteHandle);

                    return client.dataObject().write(rodsToken, dataObject, bytes, params);
                }));
                index++;
            }

            // Process the results of the write operations
            for (Future<Response> future : futures) {
                res = assertDoesNotThrow(() -> future.get());
                logger.debug(res.getBody());
                assertEquals("write request failed", 200, res.getHttpStatusCode());
                assertEquals("write failed", 0,
                        getIrodsResponseStatusCode(res.getBody()));
            }

            // Shutdown the executor
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }

            // End the parallel write
            res = client.dataObject().parallelWriteShutdown(rodsToken, parallelWriteHandle);
            logger.debug(res.getBody());
            assertEquals("parallel_write_shutdown request failed", 200, res.getHttpStatusCode());
            assertEquals("parallel_write_shutdown failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Read the contents of the data object and show it contains exactly what we expect.
            DataObjectReadParams paramsRead = new DataObjectReadParams();
            paramsRead.setCount(60);
            res = client.dataObject().read(rodsToken, dataObject, paramsRead);
            logger.debug(res.getBody());
            assertEquals("reading data object request failed", 200, res.getHttpStatusCode());

            String readData = res.getBody();
            String expectedContent = "A".repeat(10) + "B".repeat(10) + "C".repeat(10);
            assertEquals("Data read is not what was expected", expectedContent, readData);

            // Remove the data object.
            DataObjectRemoveParams paramRemove = new DataObjectRemoveParams();
            paramRemove.setNoTrash(1);
            res = client.dataObject().remove(rodsToken, dataObject, 0, paramRemove);
            logger.debug(res.getBody());

            assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
            assertEquals("remove data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data object.
            DataObjectRemoveParams paramRemove = new DataObjectRemoveParams();
            paramRemove.setNoTrash(1);
            client.dataObject().remove(rodsToken, dataObject, 0, paramRemove);
        }
    }

    @Test
    public void testModifyingMetadataAtomically() {
        Response res;
        String dataObject = "/tempZone/home/rods/for_atomic_metadata.txt";


        try {
            // Create a data object
            res = client.dataObject().touch(rodsToken, dataObject, null);
            logger.debug(res.getBody());
            assertEquals("touch request failed", 200, res.getHttpStatusCode());
            assertEquals("touch failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Add metadata to the home data object
            List<ModifyMetadataOperations> jsonParam = new ArrayList<>();
            jsonParam.add(new ModifyMetadataOperations(MetadataOperation.ADD, "a1", "v1", "u1"));
            res = client.dataObject().modifyMetadata(rodsToken, dataObject, jsonParam, OptionalInt.empty());
            logger.debug(res.getBody());
            assertEquals("adding metadata to data object request failed", 200, res.getHttpStatusCode());
            assertEquals("adding metadata to data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show metadata exists on the data object.
            String query = "select COLL_NAME, DATA_NAME where META_DATA_ATTR_NAME = 'a1' and META_DATA_ATTR_VALUE = " +
                    "'v1' and META_DATA_ATTR_UNITS = 'u1'";
            Response queryRes = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(queryRes.getBody());
            assertEquals("execute_genquery request failed", 200, queryRes.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(queryRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes.getBody()),
                    "JsonProcessingException was thrown");
            String firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asText();
            String firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asText();

            String expectedDirname = "/tempZone/home/rods";
            String expectedBasename = "for_atomic_metadata.txt";

            assertEquals(expectedDirname, firstRowFirstColumn);
            assertEquals(expectedBasename, firstRowSecondColumn);


            // Remove the metadata from the data object.
            List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
            jsonParam2.add(new ModifyMetadataOperations(MetadataOperation.REMOVE, "a1", "v1", "u1"));
            res = client.dataObject().modifyMetadata(rodsToken, dataObject, jsonParam2, OptionalInt.empty());
            logger.debug(res.getBody());
            assertEquals("adding metadata to data object request failed", 200, res.getHttpStatusCode());
            assertEquals("adding metadata to data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the metadata no longer exists on the data object.
            query = "select COLL_NAME, DATA_NAME where META_DATA_ATTR_NAME = 'a1' and META_DATA_ATTR_VALUE = 'v1' and META_DATA_ATTR_UNITS = 'u1'";
            res = client.queryOperations().executeGenQuery(rodsToken, query, null);

            logger.debug(res.getBody());
            assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the data object.
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            res = client.dataObject().remove(rodsToken, dataObject, 0, params);
            logger.debug(res.getBody());
            assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
            assertEquals("remove data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            client.dataObject().remove(rodsToken, dataObject, 0, params);
        }
    }

    @Test
    public void testModifyingPermissionsAtomically() {
        Response res;
        String dataObject = "/tempZone/home/rods/for_atomic_acls.txt";

        try {
            // Create a data object.
            res = client.dataObject().touch(rodsToken, dataObject, null);
            logger.debug(res.getBody());
            assertEquals("touch request failed", 200, res.getHttpStatusCode());
            assertEquals("touch failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Give alice read permission on the data object.
            List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
            assertDoesNotThrow(() -> jsonParam.add(new ModifyPermissionsOperations("alice", Permission.READ)));
            res = assertDoesNotThrow(() ->
                    client.dataObject().modifyPermissions(rodsToken, dataObject, jsonParam, OptionalInt.empty()),
                    "JsonProcessingException was thrown"
            );

            logger.debug(res.getBody());
            assertEquals("modifying permission request failed", 200, res.getHttpStatusCode());
            assertEquals("modifying permission failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show alice now has permission to read the data object.
            Response statRes = client.dataObject().stat(rodsToken, dataObject, Optional.empty());
            logger.debug(statRes.getBody());
            assertEquals("stat request failed", 200, statRes.getHttpStatusCode());
            assertEquals("stat endpoint failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode permissions = rootNode.path("permissions").get(0);
            assertEquals("alice", permissions.path("name").asText());
            assertEquals("tempZone", permissions.path("zone").asText());
            assertEquals("rodsuser", permissions.path("type").asText());
            assertEquals("read_object", permissions.path("perm").asText());

            // Remove the data object.
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            params.setAdmin(1);
            res = client.dataObject().remove(rodsToken, dataObject, 0, params);
            logger.debug(res.getBody());
            assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
            assertEquals("remove data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Reset permissions
            List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
            assertDoesNotThrow(() -> jsonParam.add(new ModifyPermissionsOperations("alice", Permission.NULL)));
            assertDoesNotThrow(() ->
                            client.dataObject().modifyPermissions(rodsToken, dataObject, jsonParam, OptionalInt.empty()),
                    "JsonProcessingException was thrown"
            );
            assertDoesNotThrow(() ->
                    client.dataObject().modifyPermissions(rodsToken, dataObject, jsonParam, OptionalInt.empty()));
            // Remove the data object.
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            params.setAdmin(1);
            client.dataObject().remove(rodsToken, dataObject, 0, params);
        }
    }

    @Test
    public void testModifyingReplicaProperties() {
        String collection = "/tempZone/home/rods";
        String dataObjectName = "modrepl.txt";
        String dataObject = collection + "/" + dataObjectName;

        try {
            // Create a data object.
            Response res = client.dataObject().touch(rodsToken, dataObject, null);
            logger.debug(res.getBody());
            assertEquals("touch request failed", 200, res.getHttpStatusCode());
            assertEquals("touch failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the replica is currently marked as good and has a size of 0.
            String query = String.format(
                    "select DATA_REPL_STATUS, DATA_SIZE where COLL_NAME = '%s' and DATA_NAME = '%s'",
                    collection, dataObjectName
            );

            Response queryRes = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(queryRes.getBody());
            assertEquals("execute_genquery request failed", 200, queryRes.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(queryRes.getBody()));
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes.getBody()),
                    "JsonProcessingException was thrown");
            int firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asInt();
            int firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asInt();

            assertEquals(1, firstRowFirstColumn);
            assertEquals(0, firstRowSecondColumn);

            // Change the replica's status and data size using the modify_replica operation.
            DataObjectModifyReplicaParams modifyParams = new DataObjectModifyReplicaParams();
            modifyParams.setReplicaNum(0);
            modifyParams.setNewDataRepliaStatus(0);
            modifyParams.setNewDataSize(15);
            res = client.dataObject().modifyReplica(rodsToken, dataObject, modifyParams);

            logger.debug(res.getBody());
            assertEquals("modify_replica request failed", 200, res.getHttpStatusCode());
            assertEquals("modify_replica failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the replica's status and size has changed in the catalog.
            // Same query as before
            Response queryRes2 = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(queryRes2.getBody());
            assertEquals("execute_genquery request failed", 200, queryRes2.getHttpStatusCode());
            assertEquals("execute_genquery failed", 0,
                    getIrodsResponseStatusCode(queryRes2.getBody()));
            rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes2.getBody()),
                    "JsonProcessingException was thrown");
            firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asInt();
            firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asInt();

            assertEquals(0, firstRowFirstColumn);
            assertEquals(15, firstRowSecondColumn);

            // Remove the data object.
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            params.setAdmin(1);
            res = client.dataObject().remove(rodsToken, dataObject, 0, params);
            logger.debug(res.getBody());
            assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
            assertEquals("remove data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data object.
            DataObjectRemoveParams params = new DataObjectRemoveParams();
            params.setNoTrash(1);
            params.setAdmin(1);
            client.dataObject().remove(rodsToken, dataObject, 0, params);
        }
    }
}