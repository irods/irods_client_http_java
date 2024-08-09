package org.irods.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> className)  {
        try {
            return mapper.readValue(json, className);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
