package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Mapped;

//public class CollectionsModifyMetadata {
//    private IrodsResponse irods_response;
//
//    public IrodsResponse getIrods_response() {
//        return irods_response;
//    }
//
//    public static class IrodsResponse {
//        private int status_code;
//        private String status_message;
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
//        @Override
//        public String toString() {
//            return "IrodsResponse{" +
//                    "statusCode=" + status_code +
//                    ", statusMessage='" + status_message + '\'' +
//                    ", failedOperation=" + failed_operation +
//                    '}';
//        }
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
//
//        public int getOperation_index() {
//            return operation_index;
//        }
//
//        public String getStatus_message() {
//            return status_message;
//        }
//
//        @Override
//        public String toString() {
//            return "FailedOperation{" +
//                    "operation=" + operation +
//                    ", operationIndex=" + operation_index +
//                    ", statusMessage='" + status_message + '\'' +
//                    '}';
//        }
//    }
//
//    public static class Operation {
//        private String operation;
//        private String attribute;
//        private String value;
//        private String units;
//
//        public String getOperation() {
//            return operation;
//        }
//
//        public String getAttribute() {
//            return attribute;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public String getUnits() {
//            return units;
//        }
//
//        @Override
//        public String toString() {
//            return "Operation{" +
//                    "operation='" + operation + '\'' +
//                    ", attribute='" + attribute + '\'' +
//                    ", value='" + value + '\'' +
//                    ", units='" + units + '\'' +
//                    '}';
//        }
//    }
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
public class CollectionsModifyMetadata extends Mapped {
    public static class Operation {
        private String operation;
        private String attribute;
        private String value;
        private String units;

        public String getOperation() {
            return operation;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getValue() {
            return value;
        }

        public String getUnits() {
            return units;
        }
    }
}