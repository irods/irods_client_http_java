package org.example.Mapper;

public class CollectionsCreate {
    private IrodsResponse irods_response;
    private boolean created;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    public boolean isCreated() {
        return created;
    }

    /**
     * Nested class for the nested JSON
     */
    public class IrodsResponse {
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
