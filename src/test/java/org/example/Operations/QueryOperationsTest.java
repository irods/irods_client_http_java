package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Properties.Query.QueryExecuteGenqueryParams;
import org.example.Util.Response;
import org.example.Wrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QueryOperationsTest {
    private Wrapper rods;
    private String rodsToken;

    private Wrapper alice;
    private String aliceToken;

    private String host;
    private ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(QueryOperationsTest.class);
    @Before
    public void setup() {
        host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client with rodsadmin status
        rods = new Wrapper(baseUrl, "rods", "rods");
        rods.authenticate();
        rodsToken = rods.getAuthToken();

        // Create a client with rodsuser status
        alice = new Wrapper(baseUrl, "alice", "alicepass");
        alice.authenticate();
        aliceToken = alice.getAuthToken();
    }

    @Test
    public void testSupportForGenquery1() {
        String query = "select COLL_NAME";
        QueryExecuteGenqueryParams params = new QueryExecuteGenqueryParams();
        params.setParser("genquery1");
        Response res = rods.queryOperations().execute_genquery(aliceToken, query, params);
        logger.debug(res);
        assertEquals("Executing genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(res.getBody()),
                "JsonProcessingException was thrown");
        int rowsLength = rootNode.path("rows").size();
        assertTrue(rowsLength > 0);
    }

    private int getIrodsResponseStatusCode(String jsonString) {
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(jsonString),
                "JsonProcessingException was thrown");
        JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
        return statusCodeNode.asInt();
    }

}