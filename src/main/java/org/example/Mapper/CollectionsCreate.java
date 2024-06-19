package org.example.Mapper;

public class CollectionsCreate {
    private createInner irods_response;
    private boolean created;

    public createInner getIrods_response() {
        return irods_response;
    }

    public boolean isCreated() {
        return created;
    }

    public class createInner {
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
