package org.example.Mapper;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Collections.CollectionsModifyPermissions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public void setIrods_response(IrodsResponse irods_response) {
        this.irods_response = irods_response;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        //check if it is an instance because a runtime error occurs when casting if it's not
        if (!this.getClass().equals(obj.getClass())) {
            return false; //when obj is not an instance of Ride
        }

        //casting because obj is of type Object and does not have the following data fields
        Mapped other = (Mapped) obj;
        return Objects.equals(this.irods_response, other.irods_response);
    }


    /**
     * Nested class representing common structure for all HTTP requests.
     */
    public static class IrodsResponse {
        @JsonProperty("status_code")
        private int status_code;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("status_message")
        private String status_message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private FailedOperation failed_operation; // if not included in the JSON response, it's ignored

        // getter and setters
        public int getStatus_code() {
            return status_code;
        }
        public void setStatus_code(int status_code) {
            this.status_code = status_code;
        }
        public String getStatus_message() {
            return status_message;
        }
        public void setStatus_message(String status_message) {
            this.status_message = status_message;
        }
        public FailedOperation getFailed_operation() {
            return failed_operation;
        }
        public void setFailed_operation(FailedOperation failed_operation) {
            this.failed_operation = failed_operation;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class FailedOperation {
            private Object operation;
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            private int operation_index;
            private String status_message;
            @JsonProperty("error_message")
            private String error_message;

            public Object getOperation() {
                return operation;
            }
            public void setOperation(Object operation) {
                this.operation = operation;
            }
            public int getOperation_index() {
                return operation_index;
            }
            public void setOperation_index(int operation_index) {
                this.operation_index = operation_index;
            }
            public String getStatus_message() {
                return status_message;
            }public void setStatus_message(String status_message) {
                this.status_message = status_message;
            }
            public String getError_message() {
                return error_message;
            }
            public void setError_message(String error_message) {
                this.error_message = error_message;
            }

            @Override
            /**
             * For failed_operation stanza
             */
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }

                //check if it is an instance because a runtime error occurs when casting if it's not
                if (!this.getClass().equals(obj.getClass())) {
                    return false; //when obj is not an instance of Ride
                }

                //casting because obj is of type Object and does not have the following data fields
                Mapped.IrodsResponse.FailedOperation other = (Mapped.IrodsResponse.FailedOperation) obj;
                return Objects.equals(this.operation, other.operation) &&
                        this.operation_index == other.operation_index &&
                        Objects.equals(this.status_message, other.status_message) &&
                        Objects.equals(this.error_message, other.error_message);
            }
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

        @Override
        /**
         * For Irods_response
         */
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            //check if it is an instance because a runtime error occurs when casting if it's not
            if (!this.getClass().equals(obj.getClass())) {
                return false; //when obj is not an instance of Ride
            }

            //casting because obj is of type Object and does not have the following data fields
            Mapped.IrodsResponse other = (Mapped.IrodsResponse) obj;
            return this.status_code == other.status_code &&
                    Objects.equals(this.status_message, other.status_message) &&
                    Objects.equals(this.failed_operation,other.failed_operation);
        }

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

}
