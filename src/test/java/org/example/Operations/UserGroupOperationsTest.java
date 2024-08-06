package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Serialize.ModifyMetadataOperations;
import org.example.Wrapper;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.example.IrodsResponseUtils.getIrodsResponseStatusCode;

public class UserGroupOperationsTest {
    private Wrapper client;
    private String rodsToken;

    private String aliceToken;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(UserGroupOperationsTest.class);

    @Before
    public void setup() {
        String host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client
        client = new Wrapper(baseUrl);

        // Authenticate rods
        Response res = client.authenticate("rods", "rods");
        rodsToken = res.getBody();

        // Create alice user
        this.client.userGroupOperations().createUser(rodsToken, "alice", "tempZone", Optional.of("rodsuser"));
        this.client.userGroupOperations().setPassword(rodsToken, "alice", "tempZone", "alicepass");
        res = client.authenticate("alice", "alicepass");
        aliceToken = res.getBody();
    }

    @Test
    public void testCreateStatAndRemoveRodsuser() {
        String newUsername = "test_user_rodsuser";
        String userType = "rodsuser";
        String zone = "tempZone";

        try {
            // Create a new user.
            Response res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Stat the user.
            Response statRes = client.userGroupOperations().stat(rodsToken, newUsername, Optional.of(zone));
            logger.debug(statRes.getBody());
            assertEquals("Stat on the user request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on the user failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingExcept:ion was thrown");
            assertTrue(rootNode.path("exists").asBoolean());
            assertTrue("'id' field should be present in the response JSON", rootNode.has("id"));
            assertEquals(newUsername + "#" + zone, rootNode.path("local_unique_name").asText());
            assertEquals(userType, rootNode.path("type").asText());

            // Remove the user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
        }
    }

    @Test
    public void testCreateStatAndRemoveRodsadmin() {
        String newUsername = "test_user_rodsadmin";
        String userType = "rodsadmin";
        String zone = "tempZone";

        try {
            // Create a new user.
            Response res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Stat the user.
            Response statRes = client.userGroupOperations().stat(rodsToken, newUsername, Optional.of(zone));
            logger.debug(statRes.getBody());
            assertEquals("Stat on the user request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on the user failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            assertTrue(rootNode.path(("exists")).asBoolean());
            assertTrue("'id' field should be present in the response JSON", rootNode.has("id"));
            assertEquals(newUsername + "#" + zone, rootNode.path("local_unique_name").asText());
            assertEquals(userType, rootNode.path("type").asText());

            // Remove the user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
        }
    }

    @Test
    public void testCreateStatAndRemoveGroupAdmin() {
        String newUsername = "test_user_groupadmin";
        String userType = "groupadmin";
        String zone = "tempZone";

        try {
            // Create a new user.
            Response res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Stat the user.
            Response statRes = client.userGroupOperations().stat(rodsToken, newUsername, Optional.of(zone));
            logger.debug(statRes.getBody());
            assertEquals("Stat on the user request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on the user failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            assertTrue(rootNode.path(("exists")).asBoolean());
            assertTrue("'id' field should be present in the response JSON", rootNode.has("id"));
            assertEquals(newUsername + "#" + zone, rootNode.path("local_unique_name").asText());
            assertEquals(userType, rootNode.path("type").asText());

            // Remove the user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, "tempZone");
        }
    }

    @Test
    public void testAddRemoveUserToAndFromGroup() {
        String newGroup = "test_group";
        String newUsername = "test_user_rodsuser";
        String userType = "rodsuser";
        String zone = "tempZone";

        try {
            // Create a new group.
            Response res = client.userGroupOperations().createGroup(rodsToken, newGroup);
            logger.debug(res.getBody());
            assertEquals("Adding group request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Stat the group.
            Response statRes = client.userGroupOperations().stat(rodsToken, newGroup, Optional.empty());
            logger.debug(statRes.getBody());
            assertEquals("Stat on the user request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on the user failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            assertTrue(rootNode.path(("exists")).asBoolean());
            assertTrue("'id' field should be present in the response JSON", rootNode.has("id"));
            assertEquals("rodsgroup", rootNode.path("type").asText());

            // Create a new user.
            res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Add user to group.
            res = client.userGroupOperations().addToGroup(rodsToken, newUsername, zone, newGroup);
            logger.debug(res.getBody());
            assertEquals("Adding user to group request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding user to group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that the user is a member of the group.
            res = client.userGroupOperations().isMemberOfGroup(rodsToken, newGroup, newUsername, zone);
            logger.debug(res.getBody());
            assertEquals("isMemberOfGroup request failed", 200, res.getHttpStatusCode());
            assertEquals("isMemberOfGroup failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove user from group.
            res = client.userGroupOperations().removeFromGroup(rodsToken, newUsername, zone, newGroup);
            logger.debug(res.getBody());
            assertEquals("Removing user from group request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user from group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove group.
            res = client.userGroupOperations().removeGroup(rodsToken, newGroup);
            logger.debug(res.getBody());
            assertEquals("Removing user from group request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user from group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that the group no longer exists.
            Response statRes2 = client.userGroupOperations().stat(rodsToken, newGroup, Optional.empty());
            logger.debug(statRes2.getBody());
            assertEquals("Stat on the user request failed", 200, statRes2.getHttpStatusCode());
            assertEquals("Stat on the user failed", 0,
                    getIrodsResponseStatusCode(statRes2.getBody()));

            rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes2.getBody()),
                    "JsonProcessingException was thrown");
            assertFalse(rootNode.path(("exists")).asBoolean());
        } finally {
            // Remove group.
            client.userGroupOperations().removeGroup(rodsToken, newGroup);

            // Remove user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
        }
    }

    @Test
    public void testChangeUserType() {
        String newUsername = "test_user_rodsuser";
        String userType = "rodsuser";
        String newUserType = "groupadmin";
        String zone = "tempZone";

        try {
            // Create a new user.
            Response res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Change the type of the new user.
            res = client.userGroupOperations().setUserType(rodsToken, newUsername, zone, newUserType);
            logger.debug(res.getBody());
            assertEquals("Changing user type request failed", 200, res.getHttpStatusCode());
            assertEquals("Changing user type failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Confirm the user type changed.
            Response statRes = client.userGroupOperations().stat(rodsToken, newUsername, Optional.of(zone));
            logger.debug(statRes.getBody());
            assertEquals("Stat on user request failed", 200, statRes.getHttpStatusCode());
            assertEquals("Stat on user failed", 0,
                    getIrodsResponseStatusCode(statRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(statRes.getBody()),
                    "JsonProcessingException was thrown");
            assertTrue(rootNode.path(("exists")).asBoolean());
            assertEquals(newUsername + "#" + zone, rootNode.path("local_unique_name").asText());
            assertTrue("'id' field should be present in the response JSON", rootNode.has("id"));
            assertEquals(newUserType, rootNode.path("type").asText());

            // Remove the user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
        }
    }

    @Test
    public void testUserPassword() {
        String newUsername = "test_user_rodsuser";
        String userType = "rodsuser";
        String zone = "tempZone";

        try {
            // Create a new user.
            Response res = client.userGroupOperations().createUser(rodsToken, newUsername, zone, Optional.of(userType));
            logger.debug(res.getBody());
            assertEquals("Creating user request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Set password.
            res = client.userGroupOperations().setPassword(rodsToken, newUsername, zone, "newPassword");
            logger.debug(res.getBody());
            assertEquals("Setting password for user request failed", 200, res.getHttpStatusCode());
            assertEquals("Setting password for user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the user.
            res = client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
            logger.debug(res.getBody());
            assertEquals("Removing user request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing user failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove user.
            client.userGroupOperations().removeUser(rodsToken, newUsername, zone);
        }
    }

    @Test
    public void testListingAllUsersInZone() {
        Response res = client.userGroupOperations().users(aliceToken);
        logger.debug(res.getBody());
        assertEquals("Listing users request failed", 200, res.getHttpStatusCode());
        assertEquals("Listing users failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void testListingAllGroupsinZone() {
        String newGroup = "test_group";

        try {
            // Create a new group.
            Response res = client.userGroupOperations().createGroup(rodsToken, newGroup);
            logger.debug(res.getBody());
            assertEquals("Adding group request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Get all groups.
            Response groupRes = client.userGroupOperations().groups(rodsToken);
            logger.debug(groupRes.getBody());
            assertEquals("Listing groups request failed", 200, groupRes.getHttpStatusCode());
            assertEquals("Listing groups failed", 0,
                    getIrodsResponseStatusCode(groupRes.getBody()));

            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(groupRes.getBody()),
                    "JsonProcessingException was thrown");
            List<String> groups = mapper.convertValue(rootNode.get("groups"), List.class);
            assertTrue(groups.contains("public"));
            assertTrue(groups.contains(newGroup));

            // Remove the new group.
            res = client.userGroupOperations().removeGroup(rodsToken, newGroup);
            logger.debug(res.getBody());
            assertEquals("Removing group request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing group failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

        } finally {
            client.userGroupOperations().removeGroup(rodsToken, newGroup);
        }
    }

    @Test
    public void testModifyingMetadataAtomically() {
        String username = "alice";

        // Add metadata to the user
        List<ModifyMetadataOperations> metadata = new ArrayList<>();
        metadata.add(new ModifyMetadataOperations("add", "a1", "v1", "u1"));
        Response res = assertDoesNotThrow(() -> client.userGroupOperations().modifyMetadata(rodsToken, username, metadata),
                "IOException was thrown");
        logger.debug(res.getBody());
        assertEquals("Adding metadata to user request failed", 200, res.getHttpStatusCode());
        assertEquals("Adding metadata to user failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
        
        // Show the metadata exists on the user.
        String query = "select USER_NAME where META_USER_ATTR_NAME = 'a1' and META_USER_ATTR_VALUE = 'v1' and " +
                "META_USER_ATTR_UNITS = 'u1'";
        Response queryRes = client.queryOperations().executeGenQuery(rodsToken, query, null);
        logger.debug(queryRes.getBody());
        assertEquals("Executing genQuery request failed", 200, queryRes.getHttpStatusCode());
        assertEquals("Executing genQuery failed", 0,
                getIrodsResponseStatusCode(queryRes.getBody()));

        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes.getBody()),
                "JsonProcessingException was thrown");
        assertEquals(username, rootNode.path("rows").get(0).get(0).asText());

        // Remove the metadata from the user
        List<ModifyMetadataOperations> metadata2 = new ArrayList<>();
        metadata2.add(new ModifyMetadataOperations("remove", "a1", "v1", "u1"));
        res = assertDoesNotThrow(() -> client.userGroupOperations().modifyMetadata(rodsToken, username, metadata2),
                "IOException was thrown");
        logger.debug(res.getBody());
        assertEquals("Removing metadata to user request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing metadata to user failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Show the metadata exists on the user.
        Response queryRes2 = client.queryOperations().executeGenQuery(rodsToken, query, null);
        logger.debug(queryRes2.getBody());
        assertEquals("Executing genQuery request failed", 200, queryRes2.getHttpStatusCode());
        assertEquals("Executing genQuery failed", 0,
                getIrodsResponseStatusCode(queryRes2.getBody()));

        rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes2.getBody()),
                "JsonProcessingException was thrown");
        assertEquals(0, rootNode.path("rows").size());
    }
}