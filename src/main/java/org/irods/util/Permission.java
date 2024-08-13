package org.irods.util;

/**
 * Enum representing various permissions for the iRODS system.
 * This enum is used to ensure that only valid permission values are passed to the {@code setPermission()} and
 * {@code modifyPermission()} methods used in the collections and data-objects endpoint.
 * <p> Extra notes:</p>
 * <ul>
 *     <li>{@link #MODIFY_OBJECT} - Equivalent to {@link #WRITE}</li>
 *     <li>{@link #READ_OBJECT} - Equivalent to {@link #READ}</li>
 * </ul>
 */
public enum Permission {
    OWN("own"),
    DELETE_OBJECT("delete_object"),
    WRITE("write"),
    MODIFY_OBJECT("modify_object"),
    CREATE_OBJECT("create_object"),
    DELETE_METADATA("delete_metadata"),
    MODIFY_METADATA("modify_metadata"),
    CREATE_METADATA("create_metadata"),
    READ("read"),
    READ_OBJECT("read_object"),
    READ_METADATA("read_metadata"),
    NULL("null");

    private final String value;

    /**
     * Constructs a {@code Permission} enum constant with the specified string value.
     *
     * @param value The string representation of the permission.
     */
    Permission(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this permission.
     *
     * @return The string representation of the permission.
     */
    public String getValue() {
        return value; 
    }
}