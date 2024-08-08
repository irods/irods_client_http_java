package org.irods;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class IrodsResponseUtils {
    public static int getIrodsResponseStatusCode(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = assertDoesNotThrow(() -> mapper.readTree(jsonString),
                "JsonProcessingException was thrown");
        JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
        return statusCodeNode.asInt();
    }
}