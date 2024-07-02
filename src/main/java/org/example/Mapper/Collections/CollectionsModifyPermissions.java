package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Mapper.Mapped;

import java.util.Objects;

public class CollectionsModifyPermissions extends Mapped {
    public static class Operation {
        @JsonProperty("entity_name")
        private String entity_name;
        @JsonProperty("acl")
        private String acl;

        public String getEntity_name() {
            return entity_name;
        }

        public void setEntity_name(String entity_name) {
            this.entity_name = entity_name;
        }

        public String getAcl() {
            return acl;
        }

        public void setAcl(String acl) {
            this.acl = acl;
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
            CollectionsModifyPermissions.Operation other = (CollectionsModifyPermissions.Operation) obj;
            return Objects.equals(this.entity_name, other.entity_name) &&
                    Objects.equals(this.acl, other.acl);
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

