package org.example.Util;

/**
 * enum for set_permission() method to ensure user only inputs valid data
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

