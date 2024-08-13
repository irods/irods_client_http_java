package org.irods.util;

/**
 * Enum representing various user types for the iRODS system.
 * <p>
 *     This enum is used to ensure that only valid permission values are passed to the {@code createUser()} and
 *     {@code setUserType()} methods used in the users and groups endpoint.
 * </p>
 *
 */
public enum UserType {
    RODSUSER("rodsuser"),
    GROUPADMIN("groupadmin"),
    RODSADMIN("rodsadmin");

    private final String value;

    /**
     * Constructs a {@code UserType} enum constant with the specified string value.
     *
     * @param value The string representation of the user type.
     */
    UserType(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this user type.
     *
     * @return The string representation of the user type.
     */
    public String getValue() {
        return value;
    }
}