package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Properties.DataObject.DataObjectRemoveParams;
import org.example.Properties.Ticket.TicketCreateParams;
import org.example.Wrapper;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
public class TicketOperationsTest {
    private Wrapper rods;
    private String rodsToken;

    private Wrapper alice;
    private String aliceToken;

    private String host;
    private final ObjectMapper mapper = new ObjectMapper();
    // private static final Logger logger = LogManager.getLogger(TicketOperationsTest.class);

    @Before
    public void setup() {
        host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";
        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client
        rods = new Wrapper(baseUrl, "rods", "rods");
        rods.authenticate();
        rodsToken = rods.getAuthToken();

        // Create alice user
        rods.userGroupOperations().createUser(rodsToken, "alice", "tempZone", Optional.of("rodsuser"));
        rods.userGroupOperations().setPassword(rodsToken, "alice", "tempZone", "alicepass");
        alice = new Wrapper(baseUrl, "alice", "alicepass");
        alice.authenticate();
        aliceToken = alice.getAuthToken();
    }

    @Test
    public void testCreateAndRemoveTicketForDataObject() {
        String dataObject = "/tempZone/home/alice/test_object";

        try {
            // Create a data object.
            Response res = rods.dataObject().touch(aliceToken, dataObject, null);
            // logger.debug(res.getBody());
            assertEquals("Creating data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Creating data object failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Create a ticket.
            String ticketType = "read";
            int ticketUseCount = 1000;
            int ticketSecondsUntilExpiratoin = 3600;
            TicketCreateParams createParams = new TicketCreateParams();
            createParams.setUseCount(ticketUseCount);
            createParams.setSecondsUntilExpiration(ticketSecondsUntilExpiratoin);
            Response ticketRes = rods.ticketOperations().create(aliceToken, dataObject, createParams);
            // logger.debug(res.getBody());
            assertEquals("Creating ticket request failed", 200, ticketRes.getHttpStatusCode());
            assertEquals("Creating ticket failed",0,
                    getIrodsResponseStatusCode(ticketRes.getBody()));

            // Getting ticketString
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(ticketRes.getBody()),
                    "JsonProcessingException was thrown");
            String ticketString = rootNode.path("ticket").asText();

            // Remove the ticket.
            res = rods.ticketOperations().remove(aliceToken, ticketString);
            // logger.debug(res.getBody());
            assertEquals("Removing ticket request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing ticket failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the data object.
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(1);
            res = rods.dataObject().remove(aliceToken, dataObject, 0, removeParams);
            // logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed",0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data object.
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(1);
            rods.dataObject().remove(aliceToken, dataObject, 0, removeParams);
        }

    }

    @Test
    public void testCreateAndRemoveTicketForCollection() {
        // Create a write ticket.
        String ticketType = "write";
        String ticketPath = "/tempZone/home/alice";
        int ticketUseCount = 2000;
        String ticketGroups = "public";
        String ticketHosts = this.host;

        TicketCreateParams createParams = new TicketCreateParams();
        createParams.setType(ticketType);
        createParams.setUseCount(ticketUseCount);
        createParams.setSecondsUntilExpiration(3600);
        createParams.setUsers("rods,alice");
        createParams.setGroups(ticketGroups);
        createParams.setHosts(ticketHosts);
        Response ticketRes = rods.ticketOperations().create(aliceToken, ticketPath, createParams);
        // logger.debug(res.getBody());
        assertEquals("Creating ticket request failed", 200, ticketRes.getHttpStatusCode());
        assertEquals("Creating ticket failed",0,
                getIrodsResponseStatusCode(ticketRes.getBody()));

        // Getting ticketString
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(ticketRes.getBody()),
                "JsonProcessingException was thrown");
        String ticketString = rootNode.path("ticket").asText();

        // Remove the ticket.
        Response res = rods.ticketOperations().remove(aliceToken, ticketString);
        // logger.debug(res.getBody());
        assertEquals("Removing ticket request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing ticket failed",0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    private int getIrodsResponseStatusCode(String jsonString) {
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(jsonString),
                "JsonProcessingException was thrown");
        JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
        return statusCodeNode.asInt();
    }
}