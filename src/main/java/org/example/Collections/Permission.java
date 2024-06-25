package org.example.Collections;

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
