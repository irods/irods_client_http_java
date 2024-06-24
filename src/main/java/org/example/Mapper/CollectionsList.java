package org.example.Mapper;


import java.util.List;

public class CollectionsList {
    private IrodsResponse irods_response;
    private List<String> entries;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    public List<String> getEntries() {
        return entries;
    }

    @Override
    public String toString() {
        return "irods_response:\n" + irods_response +
                "\nentries: " + entries;
    }
}
