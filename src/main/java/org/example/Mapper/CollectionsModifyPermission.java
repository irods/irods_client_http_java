package org.example.Mapper;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionsModifyPermission {

    @JsonProperty("irods_response")
    private IrodsResponse irodsResponse;

    // Getters and setters
    public IrodsResponse getIrodsResponse() {
        return irodsResponse;
    }

    public void setIrodsResponse(IrodsResponse irodsResponse) {
        this.irodsResponse = irodsResponse;
    }

    public static class IrodsResponse {

        @JsonProperty("status_code")
        private int statusCode;

        @JsonProperty("status_message")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String statusMessage;

        @JsonProperty("failed_operation")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private FailedOperation failedOperation;

        // Getters and setters
        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }

        public FailedOperation getFailedOperation() {
            return failedOperation;
        }

        public void setFailedOperation(FailedOperation failedOperation) {
            this.failedOperation = failedOperation;
        }

        @Override
        public String toString() {
            return "IrodsResponse{" +
                    "statusCode=" + statusCode +
                    ", statusMessage='" + statusMessage + '\'' +
                    ", failedOperation=" + failedOperation +
                    '}';
        }
    }

    public static class FailedOperation {

        @JsonProperty("operation")
        private Operation operation;

        @JsonProperty("operation_index")
        private int operationIndex;

        @JsonProperty("status_message")
        private String statusMessage;

        // Getters and setters
        public Operation getOperation() {
            return operation;
        }

        public void setOperation(Operation operation) {
            this.operation = operation;
        }

        public int getOperationIndex() {
            return operationIndex;
        }

        public void setOperationIndex(int operationIndex) {
            this.operationIndex = operationIndex;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }

        @Override
        public String toString() {
            return "FailedOperation{" +
                    "operation=" + operation +
                    ", operationIndex=" + operationIndex +
                    ", statusMessage='" + statusMessage + '\'' +
                    '}';
        }
    }

    public static class Operation {

        @JsonProperty("entity_name")
        private String entityName;

        @JsonProperty("acl")
        private String acl;

        // Getters and setters
        public String getEntityName() {
            return entityName;
        }

        public void setEntityName(String entityName) {
            this.entityName = entityName;
        }

        public String getAcl() {
            return acl;
        }

        public void setAcl(String acl) {
            this.acl = acl;
        }

        @Override
        public String toString() {
            return "Operation{" +
                    "entityName='" + entityName + '\'' +
                    ", acl='" + acl + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CollectionsModifyPermission{" +
                "irodsResponse=" + irodsResponse +
                '}';
    }
}
