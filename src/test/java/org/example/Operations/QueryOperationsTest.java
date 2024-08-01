package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Properties.Query.QueryExecuteGenqueryParams;
import org.example.Properties.Query.QueryExecuteSpecifcQueryParams;
import org.example.Util.Response;
import org.example.Wrapper;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QueryOperationsTest {
    private Wrapper rods;
    private String rodsToken;

    private Wrapper alice;
    private String aliceToken;

    private Wrapper bob;
    private String bobToken;

    private String host;
    private final ObjectMapper mapper = new ObjectMapper();
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
    public void testSupportForGenQuery1() {
        String query = "select COLL_NAME";
        QueryExecuteGenqueryParams params = new QueryExecuteGenqueryParams();
        params.setParser("genquery1");
        Response res = rods.queryOperations().executeGenQuery(aliceToken, query, params);
        logger.debug(res.getBody());
        assertEquals("Executing genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("remove data object failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(res.getBody()),
                "JsonProcessingException was thrown");
        int rowsLength = rootNode.path("rows").size();
        assertTrue(rowsLength > 0);
    }

    @Test
    public void testSupportForSpecificQueries() {
        String collectionPath = "/tempZone/home/alice/common_ops";
        QueryExecuteSpecifcQueryParams params = new QueryExecuteSpecifcQueryParams();
        params.setArgs(collectionPath);
        params.setCount(100);
        Response res = rods.queryOperations().executeSpecificQuery(rodsToken, "ShowCollAcls", params);

        logger.debug(res.getBody());
        assertEquals("Executing specific genquery request failed", 200, res.getHttpStatusCode());
        assertEquals("Executing specific genquery failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void testAddingAndRemovingSpecificQueries() {
        Response res;
        String specificQueryName = "get_rodsgroup_token_id";
        String sql = "select token_id from R_TOKN_MAIN where token_name = 'rodsgroup'";

        // Show that rodsusers are NOT allowed to add specific queries.
        res = rods.queryOperations().addSpecificQuery(aliceToken, specificQueryName, sql);
        logger.debug(res.getBody());
        assertEquals("Adding specific query request failed", 200, res.getHttpStatusCode());
        // ierror code of -13000: SYS_NO_API_PRIV
        assertEquals("Adding specific query was supposed to fail", -13000,
                getIrodsResponseStatusCode(res.getBody()));

        try {
            // Show that only rodsadmin are allowed to add specific queries.
            res = rods.queryOperations().addSpecificQuery(rodsToken, specificQueryName, sql);
            logger.debug(res.getBody());
            assertEquals("Adding specific query request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding specific query failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Use the new specific query.
            res = rods.queryOperations().executeSpecificQuery(aliceToken, specificQueryName, null);
            logger.debug(res.getBody());
            assertEquals("Executing specific query request failed", 200, res.getHttpStatusCode());
            assertEquals("Executing specific query failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that rodsusers are NOT allowed to remove specific queries.
            res = rods.queryOperations().removeSpecificQuery(aliceToken, specificQueryName);
            logger.debug(res.getBody());
            assertEquals("Removing specific query request failed", 200, res.getHttpStatusCode());
            // ierror code of -13000: SYS_NO_API_PRIV
            assertEquals("Removing specific query was supposed to fail", -13000,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that only rodsadmins are allowed to remove specific queries.
            res = rods.queryOperations().removeSpecificQuery(rodsToken, specificQueryName);
            logger.debug(res.getBody());
            assertEquals("Removing specific query request failed", 200, res.getHttpStatusCode());
            // ierror code of -13000: SYS_NO_API_PRIV
            assertEquals("Removing specific query was failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove specific query
            rods.queryOperations().removeSpecificQuery(rodsToken, specificQueryName);
        }
    }

    private int getIrodsResponseStatusCode(String jsonString) {
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(jsonString),
                "JsonProcessingException was thrown");
        JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
        return statusCodeNode.asInt();
    }
}