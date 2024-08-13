package org.irods.util;

/**
 * This enum is used to ensure that only valid operation values are passed to the {@code modifyMetadata()} method used
 in the collections, data objects, resources, and users groups endpoint.
 */
public enum MetadataOperation {
    ADD("add"),
    REMOVE("remove");

    private final String value;

    /**
     * Constructs a {@code MetadataOperation} enum constant with the specified value.
     *
     * @param value The string representation of the operation.
     */
    MetadataOperation(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this operation.
     *
     * @return The string representation of the operation.
     */
    public String getValue() {
        return value;
    }
}