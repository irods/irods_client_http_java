package org.irods.util;

/**
 * enum for modifyMetadata() method in collections, data objects, resources, and users groups endpoint
 */
public enum MetadataOperation {
    ADD("add"),
    REMOVE("remove");

    private final String value;

    MetadataOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}