package org.example.Operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.IrodsHttpClient;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.example.IrodsResponseUtils.getIrodsResponseStatusCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class InformationOperationsTest {
    private IrodsHttpClient client;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(UserGroupOperationsTest.class);

    @Before
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // Create client
        client = new IrodsHttpClient(baseUrl);
    }

    @Test
    public void testExpectedPropertiesExistInJsonStructure() {
        Response res = client.information().get();
        logger.debug(res.getBody());
        assertEquals("Getting information about the iRODS HTTP API server request failed ",
                200, res.getHttpStatusCode());
        assertEquals("Getting information about the iRODS HTTP API server failed", 0,
                getIrodsResponseStatusCode(res.getBody()));


        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(res.getBody()),
                "JsonProcessingException was thrown");
        assertTrue("'api_version' field should be present in the response JSON", rootNode.has("api_version"));
        assertTrue("'build' field should be present in the response JSON", rootNode.has("build"));
        assertTrue("'genquery2_enabled' field should be present in the response JSON", rootNode.has("genquery2_enabled"));

        assertTrue("'irods_zone' field should be present in the response JSON", rootNode.has("irods_zone"));
        assertTrue("'max_number_of_parallel_write_streams' field should be present in the response JSON",
                rootNode.has("max_number_of_parallel_write_streams"));
        assertTrue("'max_number_of_rows_per_catalog_query' field should be present in the response JSON",
                rootNode.has("max_number_of_rows_per_catalog_query"));
        assertTrue("'max_size_of_request_body_in_bytes' field should be present in the response JSON",
                rootNode.has("max_size_of_request_body_in_bytes"));
        assertTrue("'openid_connect_enabled' field should be present in the response JSON",
                rootNode.has("openid_connect_enabled"));
    }
}