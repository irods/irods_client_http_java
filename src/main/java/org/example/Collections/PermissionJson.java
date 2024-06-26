package org.example.Collections;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * For the modify_permissions() method.
 * Represents the JSON object that is passed into the operations parameter
 */
public class PermissionJson {
    @JsonProperty("entity_name")
    private String entityName;

    @JsonProperty("acl")
    private String acl;

    public PermissionJson(String entityName, Permission acl) {
        this.entityName = entityName;
        this.acl = acl.getValue();
    }
}
