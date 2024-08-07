package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.IrodsHttpClient;
import org.example.Properties.DataObject.DataObjectRemoveParams;
import org.example.Properties.Ticket.TicketCreateParams;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.example.IrodsResponseUtils.getIrodsResponseStatusCode;

public class TicketOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

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
        client = new IrodsHttpClient(baseUrl);

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
    public void testCreateAndRemoveTicketForDataObject() {
        String dataObject = "/tempZone/home/alice/test_object";

        try {
            // Create a data object.
            Response res = client.dataObject().touch(aliceToken, dataObject, null);
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
            Response ticketRes = client.ticketOperations().create(aliceToken, dataObject, createParams);
            // logger.debug(res.getBody());
            assertEquals("Creating ticket request failed", 200, ticketRes.getHttpStatusCode());
            assertEquals("Creating ticket failed",0,
                    getIrodsResponseStatusCode(ticketRes.getBody()));

            // Getting ticketString
            JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(ticketRes.getBody()),
                    "JsonProcessingException was thrown");
            String ticketString = rootNode.path("ticket").asText();

            // Remove the ticket.
            res = client.ticketOperations().remove(aliceToken, ticketString);
            // logger.debug(res.getBody());
            assertEquals("Removing ticket request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing ticket failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Remove the data object.
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(1);
            res = client.dataObject().remove(aliceToken, dataObject, 0, removeParams);
            // logger.debug(res.getBody());
            assertEquals("Removing data object request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing data object failed",0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the data object.
            DataObjectRemoveParams removeParams = new DataObjectRemoveParams();
            removeParams.setNoTrash(1);
            client.dataObject().remove(aliceToken, dataObject, 0, removeParams);
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
        Response ticketRes = client.ticketOperations().create(aliceToken, ticketPath, createParams);
        // logger.debug(res.getBody());
        assertEquals("Creating ticket request failed", 200, ticketRes.getHttpStatusCode());
        assertEquals("Creating ticket failed",0,
                getIrodsResponseStatusCode(ticketRes.getBody()));

        // Getting ticketString
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(ticketRes.getBody()),
                "JsonProcessingException was thrown");
        String ticketString = rootNode.path("ticket").asText();

        // Remove the ticket.
        Response res = client.ticketOperations().remove(aliceToken, ticketString);
        // logger.debug(res.getBody());
        assertEquals("Removing ticket request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing ticket failed",0,
                getIrodsResponseStatusCode(res.getBody()));
    }
}