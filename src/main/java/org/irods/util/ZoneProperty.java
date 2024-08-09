package org.irods.util;

/**
 * enum for modify() in zones endpoint
 */
public enum ZoneProperty {
    NAME("name"),
    CONNECTION_INFO("connection_info"),
    COMMENT("comment");

    private final String value;

    ZoneProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}