package org.irods.util;

/**
 * Enum representing properties supported for the {@code modify()} method in the zones endpoint.
 */
public enum ZoneProperty {
    NAME("name"),
    /**
     * The value for {@code CONNECTION_INFO} must have the following structure: {@code <host>:<port>}
     */
    CONNECTION_INFO("connection_info"),
    COMMENT("comment");

    private final String value;

    /**
     * Constructs a {@code ZoneProperty} enum constant with the specified string value.
     *
     * @param value The string representation of the property.
     */
    ZoneProperty(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of the property.
     *
     * @return The string representation of the property.
     */
    public String getValue() {
        return value;
    }
}