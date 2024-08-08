package org.irods.Util;

/**
 * enum for setPermission() method in collections and data objects endpoint
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
    
    Permission(String value) {
        this.value = value;
    }
    public String getValue() {
        return value; 
    }
}

