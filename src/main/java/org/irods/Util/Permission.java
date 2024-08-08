package org.irods.Util;

/**
 * enum for setPermission() method in collections and data objects endpoint
 */
public enum Permission {
    NULL("null"),
    READ("read"),
    WRITE("write"),
    OWN("own");
    
    private final String value; 
    
    Permission(String value) {
        this.value = value;
    }
    public String getValue() {
        return value; 
    }
}

