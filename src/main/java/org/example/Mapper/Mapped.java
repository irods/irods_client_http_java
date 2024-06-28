package org.example.Mapper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for mapped classes that represent JSON responses from HTTP requests.
 * All JSON responses will contain an instance of IrodsResponse.
 * Includes toString method to serialize the object to a pretty-printed JSON string.
 */
public abstract class Mapped {
    private IrodsResponse irods_response;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{ \"error\": \"Unable to serialize to JSON\" }";
        }
    }

    /**
     * Nested class representing common structure for all HTTP requests.
     */
    public static class IrodsResponse {
        private int status_code;
        private String status_message;

        public int getStatus_code() {
            return status_code;
        }

        public String getStatus_message() {
            return status_message;
        }
    }
}
