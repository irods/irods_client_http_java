package org.irods.util;

/**
 * Enum representing various properties for the iRODS system.
 * <p>
 *     This enum is used to ensure that only valid {@code property} values are passed to the modify() method used in
 *     the resource endpoint.
 * </p>
 */
public enum ResourceProperty {
    NAME("name"),
    TYPE("type"),
    HOST("host"),
    VAULT_PATH("vault_path"),
    CONTEXT("context"),
    STATUS("status"),
    FREE_SPACE("free_space"),
    COMMENTS("comments"),
    INFORMATION("information");

    private final String value;

    /**
     * Constructs a {@code ResourceProperty} enum constant with the specified string value.
     *
     * @param value The string representation of the user type.
     */
    ResourceProperty(String value) {
        this.value = value;
    }

    /**
     * Returns the string representation of this property.
     */
    public String getValue() {
        return value;
    }
}