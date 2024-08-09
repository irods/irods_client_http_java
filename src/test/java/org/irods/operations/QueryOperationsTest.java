package org.irods.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.irods.properties.Query.QueryExecuteGenQueryParams;
import org.irods.properties.Query.QueryExecuteSpecifcQueryParams;
import org.irods.util.Response;
import org.irods.IrodsHttpClient;
import org.irods.util.UserType;
import org.junit.Before;
import org.junit.Test;
import static org.irods.IrodsResponseUtils.getIrodsResponseStatusCode;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QueryOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

    private String aliceToken;

    private String host;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(QueryOperationsTest.class);

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
    public void testSupportForGenQuery1() {
        String query = "select COLL_NAME";
        QueryExecuteGenQueryParams params = new QueryExecuteGenQueryParams();
        params.setParser("genquery1");
        Response res = client.queryOperations().executeGenQuery(aliceToken, query, params);
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
        Response res = client.queryOperations().executeSpecificQuery(rodsToken, "ShowCollAcls", params);

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
        res = assertDoesNotThrow(() -> client.queryOperations().addSpecificQuery(aliceToken, specificQueryName, sql));
        logger.debug(res.getBody());
        assertEquals("Adding specific query request failed", 200, res.getHttpStatusCode());
        // ierror code of -13000: SYS_NO_API_PRIV
        assertEquals("Adding specific query was supposed to fail", -13000,
                getIrodsResponseStatusCode(res.getBody()));

        try {
            // Show that only rodsadmin are allowed to add specific queries.
            res = assertDoesNotThrow(() -> client.queryOperations().addSpecificQuery(rodsToken, specificQueryName, sql));
            logger.debug(res.getBody());
            assertEquals("Adding specific query request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding specific query failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Use the new specific query.
            res = client.queryOperations().executeSpecificQuery(aliceToken, specificQueryName, null);
            logger.debug(res.getBody());
            assertEquals("Executing specific query request failed", 200, res.getHttpStatusCode());
            assertEquals("Executing specific query failed",0,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that rodsusers are NOT allowed to remove specific queries.
            res = client.queryOperations().removeSpecificQuery(aliceToken, specificQueryName);
            logger.debug(res.getBody());
            assertEquals("Removing specific query request failed", 200, res.getHttpStatusCode());
            // ierror code of -13000: SYS_NO_API_PRIV
            assertEquals("Removing specific query was supposed to fail", -13000,
                    getIrodsResponseStatusCode(res.getBody()));

            // Show that only rodsadmins are allowed to remove specific queries.
            res = client.queryOperations().removeSpecificQuery(rodsToken, specificQueryName);
            logger.debug(res.getBody());
            assertEquals("Removing specific query request failed", 200, res.getHttpStatusCode());
            // ierror code of -13000: SYS_NO_API_PRIV
            assertEquals("Removing specific query was failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove specific query
            client.queryOperations().removeSpecificQuery(rodsToken, specificQueryName);
        }
    }
}