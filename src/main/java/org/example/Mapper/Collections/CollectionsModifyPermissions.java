package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
public class CollectionsModifyPermissions extends Mapped {
    public static class Operation {
        @JsonProperty("entity_name")
        private String entity_name;
        @JsonProperty("acl")
        private String acl;

        public String getEntity_name() {
            return entity_name;
        }
        public String getAcl() {
            return acl;
        }
    }
}


//public class CollectionsModifyPermissions {
//    private IrodsResponse irods_response;
//    public IrodsResponse getIrods_response() {
//        return irods_response;
//    }
//
//    public static class IrodsResponse {
//        @JsonProperty("status_code")
//        private int status_code;
//        @JsonProperty("status_message")
//        private String status_message;
//
//
//        @JsonProperty("failed_operation")
//        private FailedOperation failed_operation;
//
//        public int getStatus_code() {
//            return status_code;
//        }
//
//        public String getStatus_message() {
//            return status_message;
//        }
//
//        public FailedOperation getFailed_operation() {
//            return failed_operation;
//        }
//
//        @JsonInclude(JsonInclude.Include.NON_NULL)
//        public static class FailedOperation {
//            private Object operation;
//            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
//            private int operation_index;
//            private String status_message;
//            public Object getOperation() {
//                return operation;
//            }
//            public void setOperation(Object operation) {
//                this.operation = operation;
//            }
//
//            public int getOperation_index() {
//                return operation_index;
//            }
//            public String getStatus_message() {
//                return status_message;
//            }
//
//            public static class Operation {
//                @JsonProperty("entity_name")
//                private String entity_name;
//                @JsonProperty("acl")
//                private String acl;
//
//                public String getEntity_name() {
//                    return entity_name;
//                }
//                public String getAcl() {
//                    return acl;
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public String toString() {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            return "{ \"error\": \"Unable to serialize to JSON\" }";
//        }
//    }
//}

