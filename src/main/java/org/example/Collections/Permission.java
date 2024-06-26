package org.example.Collections;

// for set_permission() method
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
