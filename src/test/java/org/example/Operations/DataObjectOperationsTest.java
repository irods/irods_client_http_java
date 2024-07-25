package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.Serialize.ModifyMetadataOperations;
import org.example.Serialize.ModifyPermissionsOperations;
import org.example.Properties.DataObject.*;
import org.example.Properties.Resource.ResourceCreateParams;
import org.example.Util.Response;
import org.example.Wrapper;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.*;

public class DataObjectOperationsTest {
    private Wrapper rods;
    private Wrapper alice;
    private String token;
    private String aliceToken;
    private List<String> entries;
    private Response res;
    private String host;
    private ObjectMapper mapper = new ObjectMapper();
    @Before
    public void setup() {
        host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client with rodsadmin status
        rods = new Wrapper(baseUrl, "rods", "rods");
        rods.authenticate();
        token = rods.getAuthToken();

        // Create a client with rodsuser status
        alice = new Wrapper(baseUrl, "alice", "alicepass");
        alice.authenticate();
        aliceToken = alice.getAuthToken();

        resetPath(rods, token, "/tempZone/home/rods");
    }


    @Test
    public void test_common_operations() {
        String lpath = "/tempZone/home/rods/common_ops.txt";
        String resc_name = "test_ufs_common_ops_resc";
        String resc_name2 = "test_ufs_common_ops_resc2";

        // Create a unixfilesystem resource.
        ResourceCreateParams prop = new ResourceCreateParams();
        prop.setHost(host);
        prop.setVaultPath("/tmp/test_ufs_common_ops_resc_vault");
        res = rods.resourceOperations().create(token, resc_name, "unixfilesystem", prop);

        assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a unixfilesystem resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        prop = new ResourceCreateParams();
        prop.setHost(host);
        prop.setVaultPath("/tmp/test_ufs_common_ops_resc_vault");
        res = rods.resourceOperations().create(token, resc_name2, "unixfilesystem", prop);

        assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a unixfilesystem resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Create a non-empty data object
        byte[] content = "hello, this message was written via the iRODS HTTP API!".getBytes();
        res = rods.dataObject().write(token, lpath, content, null);
        assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a non-empty data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        res = rods.dataObject().replicate(token, lpath, resc_name2, resc_name, OptionalInt.empty());
        assertEquals("Replicating data object request failed", 200, res.getHttpStatusCode());
        // replicating results in error code of 168000: SYS_REPLICA_INACCESSIBLE

        // Trim the first replica
        res = rods.dataObject().trim(token, lpath, 0, null);
        assertEquals("Trimming first replica request failed", 200, res.getHttpStatusCode());
        assertEquals("Trimming first replica failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Rename the data object
        res = rods.dataObject().rename(token, lpath, lpath + ".renamed");
        assertEquals("Renaming data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Renaming data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Copy the data object
        res = rods.dataObject().copy(token, lpath + ".renamed", lpath + ".copied", null);
        assertEquals("Copying the data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Copying the data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Modify permissions on the data object
        res = rods.dataObject().set_permission(token, lpath + ".copied", "alice",
                "read", OptionalInt.empty());
        assertEquals("Modifying permissions on the data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Modifying permissions on the data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the permissions were updated
        res = rods.dataObject().stat(token, lpath + ".copied", Optional.empty());
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(res.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode permissionsNode = rootNode.path("permissions");
        boolean permissionExists = false;
        for (JsonNode permission : permissionsNode) {
            if (permission.path("name").asText().equals("alice") &&
                    permission.path("perm").asText().equals("read_object") &&
                    permission.path("type").asText().equals("rodsuser") &&
                    permission.path("zone").asText().equals("tempZone")) {
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
        res = rods.dataObject().remove(token, lpath + ".copied", 0, prop3);
        assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        res = rods.dataObject().remove(token, lpath + ".renamed", 0, prop3);
        assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Remove the resource
        res = rods.resourceOperations().remove(token, resc_name);
        assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        res = rods.resourceOperations().remove(token, resc_name2);
        assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_copy_operation_supports_overwriting_existing_data_objects() {
        String data_object_a = "/tempZone/home/rods/copy_op_overwrite_param.txt.a";
        String data_object_b = "/tempZone/home/rods/copy_op_overwrite_param.txt.b";

        // Create a non-empty data object.
        byte[] content = "some data".getBytes();
        res = alice.dataObject().write(aliceToken, data_object_a, content, null);
        assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a non-empty data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Create a second data object containing different information.
        content = "different data".getBytes();
        res = alice.dataObject().write(aliceToken, data_object_b, content, null);
        assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a non-empty data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Calculate checksums for each data object to show they are different.
        res = alice.dataObject().calculate_checksum(aliceToken, data_object_a, null);
        assertEquals("Calculating checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Calculating checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        String expectedChecksum = "sha2:EweZDmulyhRes16ZGCqb7EZTG8VN32VqYCx4D6AkDe4=";
        String actualChecksum = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

        res = alice.dataObject().calculate_checksum(aliceToken, data_object_b, null);
        assertEquals("Calculating checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Calculating checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        expectedChecksum = "sha2:YIoGizPRi+g4vLB77QHjVSHTCED6JNsJGS5nv9GG5iE=";
        actualChecksum = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

        // Show attempting to copy over an existing data object isn't allowed without the "overwrite" parameter.
        // -312000 is the associated irods error
        res = alice.dataObject().copy(aliceToken, data_object_a, data_object_b, null);
        assertEquals("Copying data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Expected to get a failed message of \'OVERWRITE_WITHOUT_FORCE_FLAG\'", -312000,
                getIrodsResponseStatusCode(res.getBody()));

        // Show copying over an existing data object is possible with the "overwrite" parameter.
        DataObjectCopyParams prop = new DataObjectCopyParams();
        prop.setOverwrite(1);
        res = alice.dataObject().copy(aliceToken, data_object_a, data_object_b, prop);
        assertEquals("Copying data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Copying data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the data objects are now identical.
        expectedChecksum = "sha2:EweZDmulyhRes16ZGCqb7EZTG8VN32VqYCx4D6AkDe4=";
        DataObjectCalculateChecksumParams prop2 = new DataObjectCalculateChecksumParams();
        prop2.setForce(1);

        res = alice.dataObject().calculate_checksum(aliceToken, data_object_a, prop2);
        assertEquals("Calculating checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Calculating checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        actualChecksum = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

        res = alice.dataObject().calculate_checksum(aliceToken, data_object_b, prop2);
        assertEquals("Calculating checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Calculating checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        actualChecksum = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

        // Remove the data objects
        DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
        prop3.setNoTrash(1);
        prop3.setAdmin(1);
        res = rods.dataObject().remove(token, data_object_a, 0, prop3);
        assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        res = rods.dataObject().remove(token, data_object_b, 0, prop3);
        assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_calculating_and_verifying_checksums() {
        String resc_name = "test_ufs_checksums_resc";
        String data_object = "/tempZone/home/rods/checksums.txt";

        // Create a unixfilesystem resource.
        ResourceCreateParams prop = new ResourceCreateParams();
        prop.setHost(host);
        prop.setVaultPath("/tmp/test_ufs_checksums_resc_vault");
        res = rods.resourceOperations().create(token, resc_name, "unixfilesystem", prop);

        assertEquals("Creating a unixfilesystem resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a unixfilesystem resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Create a non-empty object
        byte[] content = "hello, this message was written via the iRODS HTTP API!".getBytes();
        res = rods.dataObject().write(token, data_object, content, null);
        assertEquals("Creating a non-empty data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating a non-empty data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Calculate checksum
        DataObjectCalculateChecksumParams prop2 = new DataObjectCalculateChecksumParams();
        prop2.setReplicaNum(0);
        res = rods.dataObject().calculate_checksum(token, data_object, prop2);
        assertEquals("Calculating checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Calculating checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        String expectedChecksum = "sha2:1SgRcbKcy3+4fjwMvf7xQNG5OZmiYzBVbNuMIgiWbBE=";
        String actualChecksum = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode checksumNode = rootNode.path("checksum");
            actualChecksum = checksumNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("Checksum did not match", expectedChecksum, actualChecksum);

        // Verify checksum information
        res = rods.dataObject().verify_checksum(token, data_object, null);
        assertEquals("Verifying checksum for data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Verifying checksum for data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));


        // Remove the data objects
        DataObjectRemoveParams prop3 = new DataObjectRemoveParams();
        prop3.setNoTrash(1);
        res = rods.dataObject().remove(token, data_object, 0, prop3);
        assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Remove the resource
        rods.resourceOperations().remove(token, resc_name);
        assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing resource failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_registering_a_new_data_object() {
        String filename = "newly_registered_file.txt";
        String physical_path = "/tmp/" + filename;
        String dir_name = "/tempZone/home/rods";
        String data_object = dir_name + filename;
        String resource = "demoResc";

        String content = null;
        try {
            // Create a non-empty local file.
            content = "data";
            BufferedWriter writer = new BufferedWriter(new FileWriter(physical_path));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show the data object we want to create via registration does not exist.
        res = rods.dataObject().stat(token, data_object, Optional.empty());
        assertEquals("Stat request failed", 200, res.getHttpStatusCode());
        // -171000 is the error code for NOT_A_DATA_OBJECT
        assertEquals("Stat to data object was supposed to failed", -171000,
                getIrodsResponseStatusCode(res.getBody()));

        // Register local file into the catalog as a new data object
        // We know we're registering a new data object because the "as-additional-replica" parameter isn't set to 1.
        DataObjectRegisterParams prop = new DataObjectRegisterParams();
        prop.setDataSize(content.length());
        res = rods.dataObject().register(token, data_object, physical_path, resource, prop);
        assertEquals("Registering a data object request failed", 200, res.getHttpStatusCode());
        assertEquals("Registering a data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show a new data object exists with the expected replica information.
        String query = "select COLL_NAME, DATA_NAME, DATA_PATH, RESC_NAME where COLL_NAME = " +
                "'" + dir_name + "' and DATA_NAME = '" + filename + "'";
        res = rods.queryOperations().execute_genquery(token, query, null);
        assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("execute_genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Unregister the data object
        res = rods.dataObject().remove(token, data_object, 1, null);
    }

    @Test
    public void test_parallel_writes() {
        // Tell the server we're about to do a parallel write.
        String data_object = "/tempZone/home/rods/parallel_write.txt";

        res = rods.dataObject().parallel_write_init(token, data_object, 3, null);
        assertEquals("parallel_write_init request failed", 200, res.getHttpStatusCode());
        assertEquals("parallel_write_init failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        String parallel_write_handle = null;
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode statusCodeNode = rootNode.path("parallel_write_handle");
            parallel_write_handle = statusCodeNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            String parallelWriteHandleFINAL = parallel_write_handle;
            int finalIndex = index;
            futures.add(executor.submit(() -> {
                DataObjectWriteParams params = new DataObjectWriteParams();
                params.setOffset(finalIndex  * count);
                params.setStreamIndex(finalIndex);
                params.setParallelWriteHandle(parallelWriteHandleFINAL);

                return rods.dataObject().write(token, data_object, bytes, params);
            }));
            index++;
        }

        // Process the results of the write operations
        for (Future<Response> future : futures) {
            try {
                res = future.get();
                assertEquals("write request failed", 200, res.getHttpStatusCode());
                assertEquals("write failed", 0,
                        getIrodsResponseStatusCode(res.getBody()));
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        // Shutdown the executor
        executor.shutdown();

        // End the parallel write
        res = rods.dataObject().parallel_write_shutdown(token, parallel_write_handle);
        assertEquals("parallel_write_shutdown request failed", 200, res.getHttpStatusCode());
        assertEquals("parallel_write_shutdown failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Read the contents of the data object and show it contains exactly what we expect.
        DataObjectReadParams paramsRead = new DataObjectReadParams();
        paramsRead.setCount(60);
        res = rods.dataObject().read(token, data_object, paramsRead);
        assertEquals("reading data object request failed", 200, res.getHttpStatusCode());

        String readData = res.getBody();
        String expectedContent = "A".repeat(10) + "B".repeat(10) + "C".repeat(10);
        assertEquals("Data read is not what was expected", expectedContent, readData);


        // Remove the data object.
        DataObjectRemoveParams paramRemove = new DataObjectRemoveParams();
        paramRemove.setNoTrash(1);
        res = rods.dataObject().remove(token, data_object, 0, paramRemove);

        assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_modifying_metadata_atomically() {
        // Create a data object
        String data_object = "/tempZone/home/rods/for_atomic_metadata.txt";
        res = rods.dataObject().touch(token, data_object, null);
        assertEquals("touch request failed", 200, res.getHttpStatusCode());
        assertEquals("touch failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Add metadata to the home data object
        List<ModifyMetadataOperations> jsonParam = new ArrayList<>();
        jsonParam.add(new ModifyMetadataOperations("add", "a1", "v1", "u1"));
        res = rods.dataObject().modify_metadata(token, data_object, jsonParam, OptionalInt.empty());
        assertEquals("adding metadata to data object request failed", 200, res.getHttpStatusCode());
        assertEquals("adding metadata to data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show metadata exists on the data object.
        String query = "select COLL_NAME, DATA_NAME where META_DATA_ATTR_NAME = 'a1' and META_DATA_ATTR_VALUE = " +
                "'v1' and META_DATA_ATTR_UNITS = 'u1'";
        res = rods.queryOperations().execute_genquery(token, query, null);
        assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("execute_genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            String firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asText();
            String firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asText();

            String expectedDirname = "/tempZone/home/rods";
            String expectedBasename = "for_atomic_metadata.txt";

            assertEquals(expectedDirname, firstRowFirstColumn);
            assertEquals(expectedBasename, firstRowSecondColumn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove the metadata from the data object.
        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
        jsonParam2.add(new ModifyMetadataOperations("remove", "a1", "v1", "u1"));
        res = rods.dataObject().modify_metadata(token, data_object, jsonParam2, OptionalInt.empty());
        assertEquals("adding metadata to data object request failed", 200, res.getHttpStatusCode());
        assertEquals("adding metadata to data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the metadata no longer exists on the data object.
        query = "select COLL_NAME, DATA_NAME where META_DATA_ATTR_NAME = 'a1' and META_DATA_ATTR_VALUE = 'v1' and META_DATA_ATTR_UNITS = 'u1'";
        res = rods.queryOperations().execute_genquery(token, query, null);
        assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("execute_genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Remove the data object.
        DataObjectRemoveParams params = new DataObjectRemoveParams();
        params.setNoTrash(1);
        res = rods.dataObject().remove(token, data_object, 0, params);
        assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_modifying_permissions_atomically() {
        // Create a data object.
        String data_object = "/tempZone/home/rods/for_atomic_acls.txt";
        res = rods.dataObject().touch(token, data_object, null);
        assertEquals("touch request failed", 200, res.getHttpStatusCode());
        assertEquals("touch failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Give alice read permission on the data object.
        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
        jsonParam.add(new ModifyPermissionsOperations("alice", "read"));
        res = rods.dataObject().modify_permissions(token, data_object, jsonParam, OptionalInt.empty());
        assertEquals("modifying permission request failed", 200, res.getHttpStatusCode());
        assertEquals("modifying permission failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show alice now has permission to read the data object.
        res = rods.dataObject().stat(token, data_object, Optional.empty());
        assertEquals("stat request failed", 200, res.getHttpStatusCode());
        assertEquals("stat endpoint failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode permissionsNode = rootNode.path("permissions").get(0);

            String name = permissionsNode.path("name").asText();
            String zone = permissionsNode.path("zone").asText();
            String type = permissionsNode.path("type").asText();
            String perm = permissionsNode.path("perm").asText();

            assertEquals("alice", name);
            assertEquals("tempZone", zone);
            assertEquals("rodsuser", type);
            assertEquals("read_object", perm);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove the data object.
        DataObjectRemoveParams params = new DataObjectRemoveParams();
        params.setNoTrash(1);
        params.setAdmin(1);
        res = rods.dataObject().remove(token, data_object, 0, params);
        assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void test_modifying_replica_properties() {
        String directoryName = "/tempZone/home/rods";
        String fileName = "modrepl.txt";
        String data_object = directoryName + "/" + fileName;

        // Create a data object.
        res = rods.dataObject().touch(token, data_object, null);
        assertEquals("touch request failed", 200, res.getHttpStatusCode());
        assertEquals("touch failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the replica is currently marked as good and has a size of 0.
        String query = String.format(
                "select DATA_REPL_STATUS, DATA_SIZE where COLL_NAME = '%s' and DATA_NAME = '%s'",
                directoryName, fileName
        );
        res = rods.queryOperations().execute_genquery(token, query, null);
        assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("execute_genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            int firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asInt();
            int firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asInt();

            assertEquals(1, firstRowFirstColumn);
            assertEquals(0, firstRowSecondColumn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Change the replica's status and data size using the modify_replica operation.
        DataObjectModifyReplicaParams modifyParams = new DataObjectModifyReplicaParams();
        modifyParams.setReplicaNum(0);
        modifyParams.setNewDataRepliaStatus(0);
        modifyParams.setNewDataSize(15);
        res = rods.dataObject().modify_replica(token, data_object, modifyParams);
        assertEquals("modify_replica request failed", 200, res.getHttpStatusCode());
        assertEquals("modify_replica failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the replica's status and size has changed in the catalog.
        // Same query as before
        res = rods.queryOperations().execute_genquery(token, query, null);
        assertEquals("execute_genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("execute_genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            int firstRowFirstColumn = rootNode.path("rows").get(0).get(0).asInt();
            int firstRowSecondColumn = rootNode.path("rows").get(0).get(1).asInt();

            assertEquals(0, firstRowFirstColumn);
            assertEquals(15, firstRowSecondColumn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Remove the data object.
        DataObjectRemoveParams params = new DataObjectRemoveParams();
        params.setNoTrash(1);
        params.setAdmin(1);
        res = rods.dataObject().remove(token, data_object, 0, params);
        assertEquals("remove data object request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    private int getIrodsResponseStatusCode(String jsonString) {
        try {
            JsonNode rootNode = mapper.readTree(jsonString);
            JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
            return statusCodeNode.asInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void resetPath(Wrapper wrapper, String token, String lpath) {
        res = wrapper.collections().list(token, lpath, null);

        List<String> entries = new ArrayList<>();
        try {
            JsonNode rootNode = mapper.readTree(res.getBody());
            JsonNode entriesNode = rootNode.path("entries");

            for (JsonNode node : entriesNode) {
                entries.add(node.asText());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (entries != null && !entries.isEmpty()) {
            for (String entry : entries) {
                wrapper.collections().remove(token, entry, null);
                wrapper.dataObject().remove(token, entry, 0, null);
                wrapper.resourceOperations().remove(token, entry);
            }
        }
    }
}