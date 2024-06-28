package org.example.Mapper.Collections.Serialize;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Collections.Permission;

/**
 * For the modify_permissions() method.
 * Represents the JSON object that is passed into the operations parameter
 */
public class ModifyPermissionsOperations {
    @JsonProperty("entity_name")
    private String entity_name;

    @JsonProperty("acl")
    private String acl;

    public ModifyPermissionsOperations(String entity_name, Permission acl) {
        this.entity_name = entity_name;
        this.acl = acl.getValue();
    }
}
