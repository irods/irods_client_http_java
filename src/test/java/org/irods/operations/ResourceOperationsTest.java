package org.irods.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.properties.DataObject.DataObjectRemoveParams;
import org.irods.properties.DataObject.DataObjectWriteParams;
import org.irods.properties.Resource.ResourceCreateParams;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.util.Response;
import org.irods.util.MetadataOperation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.irods.IrodsResponseUtils.getIrodsResponseStatusCode;


public class ResourceOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

    private String host;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(ResourceOperationsTest.class);

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
    }

    @Ignore("Remove this annotation once issue #61 is resolved")
    @Test
    public void testCommonOperations() {
        Response res;
        String rescRepl = "test_repl";
        String rescUfs0 = "test_ufs0";
        String rescUfs1 = "test_ufs1";
        String dataObject = "/tempZone/home/rods/test_object_for_resources";

        try {
            // Create three resources (replication w/ two unixfilesystem resources).
            res = client.resourceOperations().create(rodsToken, rescRepl, "replication", null);
            logger.debug(res.getBody());
            assertEquals("Creating resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating resource failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the replication resource was created.
            Response statRes = client.resourceOperations().stat(rodsToken, rescRepl);
            logger.debug(res.getBody());
            assertEquals("Stat on the resource request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on the resource failed",0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            JsonNode infoNode = rootNode.path("info");
            boolean exists = rootNode.path("exists").asBoolean();
            assertTrue("'id' field should be present in the 'info' node", infoNode.has("id"));
            String name = infoNode.path("name").asText();
            String type = infoNode.path("type").asText();
            String zone = infoNode.path("zone").asText();
            String host = infoNode.path("host").asText();
            String vaultPathInfo = infoNode.path("vault_path").asText();

            assertTrue(exists);
            assertEquals(rescRepl, name);
            assertEquals("replication", type);
            assertEquals("tempZone", zone);
            assertEquals("EMPTY_RESC_HOST", host);
            assertEquals("EMPTY_RESC_PATH", vaultPathInfo);

            // Capture the replication resource's id.
            // This resource is going to be the parent of the unixfilesystem resources.
            // This value is needed to verify the relationship.
            int rescReplId = infoNode.path("id").asInt();
            String[] rescNames = {rescUfs0, rescUfs1};
            for (String rescName : rescNames) {
                String vaultPath = "/tmp/" + rescName + "_vault";

                // Create a unixfilesystem resource.
                ResourceCreateParams createParams = new ResourceCreateParams();
                createParams.setHost(this.host);
                createParams.setVaultPath(vaultPath);
                res = client.resourceOperations().create(rodsToken, rescName, "unixfilesystem", createParams);
                logger.debug(res.getBody());
                assertEquals("Creating resource request failed", 200, res.getHttpStatusCode());
                assertEquals("Creating resource failed",0,
                        getIrodsResponseStatusCode(res.getBody()));

                // Add the unixfilesystem resource as a child of the replication resource.
                res = client.resourceOperations().addChild(rodsToken, rescRepl, rescName, Optional.empty());
                logger.debug(res.getBody());
                assertEquals("Adding as a child request failed", 200, res.getHttpStatusCode());
                assertEquals("Adding as a child failed",0,
                        getIrodsResponseStatusCode(res.getBody()));

                // Show that the resource was created and configured successfully.
                Response statRes2 = client.resourceOperations().stat(rodsToken, rescName);
                logger.debug(res.getBody());
                assertEquals("Stat on resource request failed", 200, statRes2.getHttpStatusCode());
                assertEquals("Stat on resource failed",0,
                        getIrodsResponseStatusCode(statRes2.getBody()));

                rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes2.getBody()),
                        "JsonProcessingException was thrown");
                infoNode = rootNode.path("info");
                exists = rootNode.path("exists").asBoolean();
                assertTrue("'id' field should be present in the 'info' node", infoNode.has("id"));
                name = infoNode.path("name").asText();
                type = infoNode.path("type").asText();
                zone = infoNode.path("zone").asText();
                host = infoNode.path("host").asText();
                int parentId = infoNode.path("parent_id").asInt();
                vaultPathInfo = infoNode.path("vault_path").asText();

                assertTrue(exists);
                assertEquals(rescName, name);
                assertEquals("unixfilesystem", type);
                assertEquals("tempZone", zone);
                assertEquals(this.host, host);
                assertEquals(vaultPath, vaultPathInfo);
                assertEquals(rescReplId, parentId);
            }

            // TODO(#61): figure out why writing to a resource makes test fail
            //  Message from logs: could not open data object for write

            // Create a data object targeting the replication resource.
            byte[] contents = "hello, iRODS HTTP API!".getBytes();
            DataObjectWriteParams writeParams = new DataObjectWriteParams();
            writeParams.setResource(rescRepl);
            writeParams.setOffset(0);
            res = client.dataObject().write(rodsToken, dataObject, contents, writeParams);
            logger.debug(res.getBody());
            assertEquals("Creating data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating data object failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Launch rebalance
            res = client.resourceOperations().rebalance(rodsToken, rescRepl);
            logger.debug(res.getBody());
            assertEquals("Rebalance request failed", 200, res.getHttpStatusCode());
            assertEquals("Rebalance failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Give the rebalance operation time to complete!
            assertDoesNotThrow(() -> Thread.sleep(3000), "An interruption occurred while sleeping.");

            // Clean-up.
            // Remove the data object
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(0);
            res = client.dataObject().remove(rodsToken, dataObject, 0, removeParams);
            logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the resources.
            for (String rescName : rescNames) {
                // Detach ufs resource from the replication resource.
                res = client.resourceOperations().removeChild(rodsToken, rescRepl, rescName);
                logger.debug(res.getBody());
                assertEquals("Removing child resource request failed", 200, res.getHttpStatusCode());
                assertEquals("Removing child resource failed",0,
                        getIrodsResponseStatusCode(res.getBody()));

                // Remove ufs resource.
                res = client.resourceOperations().remove(rodsToken, rescName);
                logger.debug(res.getBody());
                assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
                assertEquals("Removing resource failed",0,
                        getIrodsResponseStatusCode(res.getBody()));
            }

            // Remove the replication resource.
            res = client.resourceOperations().remove(rodsToken, rescRepl);
            logger.debug(res.getBody());
            assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing resource failed",0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data object
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(0);
            client.dataObject().remove(rodsToken, dataObject, 0, removeParams);

            // Remove the resources.
            String[] rescNames = {rescUfs0, rescUfs1};
            for (String rescName : rescNames) {
                // Detach ufs resource from the replication resource.
                client.resourceOperations().removeChild(rodsToken, rescRepl, rescName);

                // Remove ufs resource.
                client.resourceOperations().remove(rodsToken, rescName);
            }

            // Remove the replication resource.
            client.resourceOperations().remove(rodsToken, rescRepl);
        }
    }

    @Test
    public void testMoidfingDataAtomically() {
        String resource = "demoResc";

        try {
            // Add metadata to the resource.
            List<ModifyMetadataOperations> modifyJson = new ArrayList<>();
            modifyJson.add(new ModifyMetadataOperations(MetadataOperation.ADD, "a1", "v1", "u1"));
            Response res = client.resourceOperations().modifyMetadata(rodsToken, resource, modifyJson);
            logger.debug(res.getBody());
            assertEquals("Modifying metadata request failed", 200, res.getHttpStatusCode());
            assertEquals("Modifying metadata failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the metadata exists on the resource.
            String query = "select RESC_NAME where META_RESC_ATTR_NAME = 'a1' and META_RESC_ATTR_VALUE = 'v1' and " +
                    "META_RESC_ATTR_UNITS = 'u1'";
            Response queryRes = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(res.getBody());
            assertEquals("Executing genQuery request failed", 200, queryRes.getHttpStatusCode());
            assertEquals("Executing genQuery failed",0,
                    getIrodsResponseStatusCode(queryRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes.getBody()),
                    "JsonProcessingException was thrown");
            String firstRowFirstCol = rootNode.path("rows").get(0).get(0).asText();
            assertEquals(resource, firstRowFirstCol);

            // Remove the metadata from the resource.
            List<ModifyMetadataOperations> modifyJson2 = new ArrayList<>();
            modifyJson2.add(new ModifyMetadataOperations(MetadataOperation.REMOVE, "a1", "v1", "u1"));
            res = client.resourceOperations().modifyMetadata(rodsToken, resource, modifyJson2);
            logger.debug(res.getBody());
            assertEquals("Modifying metadata request failed", 200, res.getHttpStatusCode());
            assertEquals("Modifying metadata failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show the metadata no longer exists on the resource.
            Response queryRes2 = client.queryOperations().executeGenQuery(rodsToken, query, null);
            logger.debug(res.getBody());
            assertEquals("Executing genQuery request failed", 200, queryRes2.getHttpStatusCode());
            assertEquals("Executing genQuery failed",0,
                    getIrodsResponseStatusCode(queryRes2.getBody()));

            rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes2.getBody()),
                    "JsonProcessingException was thrown");
            int rowsLength = rootNode.path("rows").size();
            assertEquals(0, rowsLength);
        } finally {
            // Remove metadata
            List<ModifyMetadataOperations> modifyJson = new ArrayList<>();
            modifyJson.add(new ModifyMetadataOperations(MetadataOperation.REMOVE, "a1", "v1", "u1"));
            client.resourceOperations().modifyMetadata(rodsToken, resource, modifyJson);
        }
    }

    @Test
    public void testModifyingResourceProperties() {
        String resource = "test_modifying_resource_properties_original";

        try {
            // Create a new resource.
            Response res = client.resourceOperations().create(rodsToken, resource, "replication", null);
            logger.debug(res.getBody());
            assertEquals("Creating resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating resource failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // The list of updates to apply in sequence.
            List<Map.Entry<String, String>> propertyMap = List.of(
                    Map.entry("name", "test_modifying_resource_properties_renamed"),
                    Map.entry("type", "passthru"),
                    Map.entry("host", "example.org"),
                    Map.entry("vault_path", "/tmp/test_modifying_resource_properties_vault"),
                    Map.entry("status", "down"),
                    Map.entry("status", "up"),
                    Map.entry("comments", "test_modifying_resource_properties_comments"),
                    Map.entry("information", "test_modifying_resource_properties_information"),
                    Map.entry("free_space", "test_modifying_resource_properties_free_space"),
                    Map.entry("context", "test_modifying_resource_properties_context")
            );

            // Apply each update to the resource and verify that each one results in the expected results.
            for (Map.Entry<String, String> entry : propertyMap) {
                String property = entry.getKey();
                String value = entry.getValue();
                // Change a property of the resource.
                res = client.resourceOperations().modify(rodsToken, resource, property, value);
                logger.debug(res.getBody());
                assertEquals("Modifying property of resource request failed", 200, res.getHttpStatusCode());
                assertEquals("Modifying property of resource failed",0,
                        getIrodsResponseStatusCode(res.getBody()));

                // Make sure to update the "resource" variable following a successful rename.
                if ("name".equals(property)) {
                    resource = value;
                    logger.debug("Resource name updated successfully. Capturing new name.");
                }

                // Show the property was modified.
                Response statRes = client.resourceOperations().stat(rodsToken, resource);
                logger.debug(res.getBody());
                assertEquals("Stat on resource request failed", 200, statRes.getHttpStatusCode());
                assertEquals("Stat on resource failed",0,
                        getIrodsResponseStatusCode(statRes.getBody()));
                assertTrue(statRes.getBody().contains("\"" + property + "\":\"" + value + "\""));
            }
            // Remove the resource.
            res = client.resourceOperations().remove(rodsToken, resource);
            logger.debug(res.getBody());
            assertEquals("Removing resource request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing resource failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

        } finally {
            // Remove the resource.
            client.resourceOperations().remove(rodsToken, resource);
        }
    }
}