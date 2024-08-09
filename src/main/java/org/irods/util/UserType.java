package org.irods.util;

/**
 * enum for createUser() and setUserType() in users and groups endpoint
 */
public enum UserType {
    RODSUSER("rodsuser"),
    GROUPADMIN("groupadmin"),
    RODSADMIN("rodsadmin");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}