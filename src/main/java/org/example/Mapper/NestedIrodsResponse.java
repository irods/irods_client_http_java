package org.example.Mapper;

/**
 * For JSON responses that only include a nested irods_response
 * Includes:
 *  - collections.create()
 *  - collections.remove()
 *  - collections.set_permission()
 */
//TODO: see it's possible to transfer the other mapped classes into this one
public class NestedIrodsResponse {
    private IrodsResponse irods_response; // nested JSON
    private boolean created;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    public boolean isCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "irods_response: " + irods_response +
                "\ncreated: " + created;
    }
}
