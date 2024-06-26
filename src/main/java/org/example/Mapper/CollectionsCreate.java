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

    @Override
    public String toString() {
        return "irods_response:\n" + irods_response +
                "\ncreated:" + created;
    }
}
