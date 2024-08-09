package org.irods.serialize;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.irods.util.Permission;

/**
 * For the modify_permissions() method.
 * Represents the JSON object that is passed into the operations parameter
 */
public class ModifyPermissionsOperations {
    @JsonProperty("")
    private String entity_name;

    @JsonProperty("acl")
    private String acl;

    public ModifyPermissionsOperations(String entity_name, Permission perm) throws IllegalArgumentException {
        this.entity_name = entity_name;
        switch (perm) {
            case NULL:
            case READ:
            case WRITE:
            case OWN:
                this.acl = perm.getValue();
                break;
            default:
                throw new IllegalArgumentException("Expected one of the following Permission enum values: " +
                        "OWN, WRITE, READ, NULL");
        }
    }
}