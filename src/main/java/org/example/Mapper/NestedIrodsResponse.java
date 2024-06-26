package org.example.Mapper;

/**
 * For JSON responses that ONLY include a nested irods_response and no other items
 * Includes:
 *  - collections.remove()
 *  - collections.set_permission()
 */
//TODO: see it's possible to transfer the other mapped classes into this one
public class NestedIrodsResponse {
    private IrodsResponse irods_response; // nested JSON

    public IrodsResponse getIrods_response() {
        return irods_response;
    }


    @Override
    public String toString() {
        return "irods_response: " + irods_response;
    }
}
