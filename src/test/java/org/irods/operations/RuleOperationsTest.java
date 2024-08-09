package org.irods.operations;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.irods.IrodsResponseUtils.getIrodsResponseStatusCode;


public class RuleOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(RuleOperationsTest.class);

    @Before
    public void setup() {
        String host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client
        client = new IrodsHttpClient(baseUrl);

        // Authenticate rods
        Response res = client.authenticate("rods", "rods");
        rodsToken = res.getBody();
    }

    @Test
    public void testListAllRuleEnginePlugins() {
        Response res = client.ruleOperations().listRuleEngines(rodsToken);
        logger.debug(res.getBody());
        assertEquals("Listing rule engines request failed", 200, res.getHttpStatusCode());
        assertEquals("Listing rule engines failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void testRemoveDelayRule() {
        String repInstance = "irods_rule_engine_plugin-irods_rule_language-instance";
        String ruleText = String.format(
                "delay(\"<INST_NAME>%s</INST_NAME><PLUSET>1h</PLUSET>\") { writeLine(\"serverLog\", \"iRODS HTTP API\"); }",
                repInstance
        );

        // Schedule a delay rule to execute in the distant future.
        Response res = client.ruleOperations().execute(rodsToken, ruleText, Optional.of(repInstance));
        logger.debug(res.getBody());
        assertEquals("Executing rule code request failed", 200, res.getHttpStatusCode());
        assertEquals("Executing rule code failed", 0,
                getIrodsResponseStatusCode(res.getBody()));

        // Find the delay rule we just created.
        // This query assumes the test suite is running on a system where no other delay rules are being created.
        String query = "select max(RULE_EXEC_ID)";
        Response queryRes = client.queryOperations().executeGenQuery(rodsToken, query, null);
        logger.debug(queryRes.getBody());
        assertEquals("Executing genQuery request failed", 200, queryRes.getHttpStatusCode());
        assertEquals("Executing genQuery failed", 0,
                getIrodsResponseStatusCode(queryRes.getBody()));

        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(queryRes.getBody()),
                "JsonProcessingException was thrown");
        int rowsSize = rootNode.path("rows").size();
        int ruleId = rootNode.path("rows").get(0).get(0).asInt();
        assertEquals(1, rowsSize);

        // Remove the delay rule.
        res = client.ruleOperations().removeDelayRule(rodsToken, ruleId);
        logger.debug(res.getBody());
        assertEquals("Removing delay rule request failed", 200, res.getHttpStatusCode());
        assertEquals("Removing delay rule failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }
}