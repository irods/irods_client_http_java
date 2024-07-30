package Collections;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Properties.Collection.CollectionsListParams;
import org.example.Properties.Collection.CollectionsRemoveParams;
import org.example.Serialize.ModifyMetadataOperations;
import org.example.Serialize.ModifyPermissionsOperations;
import org.example.Util.Permission;
import org.example.Wrapper;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CollectionOperationsTest {
    private Wrapper rods;
    private String rodsToken;

    private Wrapper alice;
    private String aliceToken;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // Create client
        rods = new Wrapper(baseUrl, "rods", "rods");
        rods.authenticate();
        rodsToken = rods.getAuthToken();

        // Create alice user
        rods.userGroupOperations().create_user(rodsToken, "alice", "tempZone", Optional.of("rodsuser"));
        rods.userGroupOperations().set_password(rodsToken, "alice", "tempZone", "alicepass");
        alice = new Wrapper(baseUrl, "alice", "alicepass");
        alice.authenticate();
        aliceToken = alice.getAuthToken();

        resetPath(rods, rodsToken, "/tempZone/home/rods");
    }

    @Test
    public void testListOperation() {
        Response res;
        String homeCollection = "/tempZone/home/alice";

        // Created nested collections
        String collection = homeCollection + "/c0/c1/c2";
        res = alice.collections().create(aliceToken, collection, OptionalInt.of(1));
        assertEquals("Creating collection request failed", 200, res.getHttpStatusCode());
        assertEquals("Creating collection failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Create one data object in each collection.
        List<String> dataObjects = new ArrayList<>();
        dataObjects.add(homeCollection + "/c0/d0");
        dataObjects.add(homeCollection + "/c0/c1/d1");
        dataObjects.add(homeCollection + "/c0/c1/c2/d2");

        for (String name : dataObjects) {
            res = alice.dataObject().touch(aliceToken, name, null);
            assertEquals("Creating data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating data object failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        }

        // List only the contents of the home collection.
        Response listRes = alice.collections().list(aliceToken, homeCollection, null);
        assertEquals("Listing collections request failed", 200, listRes.getHttpStatusCode());

        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(listRes.getBody()),
                "JsonProcessingException was thrown");
        String entry = rootNode.path("entries").get(0).asText();
        assertEquals("/tempZone/home/alice/c0", entry);

        // List the home collection recursively.
        CollectionsListParams listParams = new CollectionsListParams();
        listParams.setRecurse(1);
        Response listRes2 = alice.collections().list(aliceToken, homeCollection, listParams);
        List<String> actualEntries = new ArrayList<>();

        rootNode = assertDoesNotThrow(() -> mapper.readTree(listRes2.getBody()),
                "JsonProcessingException was thrown");
        JsonNode entriesNode = rootNode.path("entries");

        for (JsonNode node : entriesNode) {
            actualEntries.add(node.asText());
        }
        Collections.sort(actualEntries);

        List<String> expectedEntries = new ArrayList<>();
        expectedEntries.add("/tempZone/home/alice/c0");
        expectedEntries.add("/tempZone/home/alice/c0/c1");
        expectedEntries.add("/tempZone/home/alice/c0/c1/c2");
        expectedEntries.add("/tempZone/home/alice/c0/c1/c2/d2");
        expectedEntries.add("/tempZone/home/alice/c0/c1/d1");
        expectedEntries.add("/tempZone/home/alice/c0/d0");
        Collections.sort(expectedEntries);
        assertEquals(expectedEntries, actualEntries);

        // Remove collections
        CollectionsRemoveParams removeParams = new CollectionsRemoveParams();
        removeParams.setRecurse(1);
        removeParams.setNoTrash(1);
        res = alice.collections().remove(aliceToken, "/tempZone/home/alice/c0", removeParams);
        assertEquals("Removing collection request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing collection failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    private int getIrodsResponseStatusCode(String jsonString) {
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(jsonString),
                "JsonProcessingException was thrown");
        JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
        return statusCodeNode.asInt();
    }

    private void resetPath(Wrapper wrapper, String token, String lpath) {
        Response res;
        res = wrapper.collections().list(token, lpath, null);

        List<String> entries = new ArrayList<>();
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(res.getBody()),
                "JsonProcessingException was thrown");
        JsonNode entriesNode = rootNode.path("entries");

        for (JsonNode node : entriesNode) {
            entries.add(node.asText());
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