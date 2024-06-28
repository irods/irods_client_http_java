package org.example.Mapper.Collections;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Mapped;
//public class CollectionsModifyPermissions extends Mapped {
//    private FailedOperation failed_operation;
//
//    public FailedOperation getFailed_operation() {
//        return failed_operation;
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class FailedOperation {
//        private Operation operation;
//        private int operation_index;
//        private String status_message;
//
//        public Operation getOperation() {
//            return operation;
//        }
//        public int getOperation_index() {
//            return operation_index;
//        }
//        public String getStatus_message() {
//            return status_message;
//        }
//    }
//
//    public static class Operation {
//        private String entity_name;
//        private String acl;
//
//        public String getEntity_name() {
//            return entity_name;
//        }
//
//        public String getAcl() {
//            return acl;
//        }
//    }
//}

public class CollectionsModifyPermissions {
    private IrodsResponse irods_response;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    /**
     * Nested IrodsResponse
     * Different from main IrodsResponse class because includes another nested JSON caled failed_operation
     */
    public static class IrodsResponse {
        private int status_code;
        private String status_message;
        private FailedOperation failed_operation;

        public int getStatus_code() {
            return status_code;
        }
        public String getStatus_message() {
            return status_message;
        }

        public FailedOperation getFailed_operation() {
            return failed_operation;
        }

        /**
         * JSON that is nested within the IrodsResponse
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FailedOperation {
            private Operation operation;
            private int operation_index;
            private String status_message;

            public Operation getOperation() {
                return operation;
            }
            public int getOperation_index() {
                return operation_index;
            }
            public String getStatus_message() {
                return status_message;
            }

            /**
             * Nested JSON that is within the failed_operation JSON
             */
            public static class Operation {
                private String entity_name;
                private String acl;

                public String getEntity_name() {
                    return entity_name;
                }
                public String getAcl() {
                    return acl;
                }
            }
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
