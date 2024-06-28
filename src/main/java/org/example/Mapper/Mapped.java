package org.example.Mapper;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Collections.CollectionsModifyPermissions;

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
        @JsonProperty("status_code")
        private int status_code;
        @JsonProperty("status_message")
        private String status_message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("failed_operation")
        private FailedOperation failed_operation; // if not included in the JSON response, it's ignored

        public int getStatus_code() {
            return status_code;
        }

        public String getStatus_message() {
            return status_message;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FailedOperation {
            private Object operation;
            private int operation_index;
            private String status_message;

            public Object getOperation() {
                return operation;
            }
            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public int getOperation_index() {
                return operation_index;
            }
            public String getStatus_message() {
                return status_message;
            }
        }
    }
}
