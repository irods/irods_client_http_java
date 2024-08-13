package org.irods.serialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.irods.util.Permission;

/**
 * Represents the JSON object for the {@code modifyPermissions()} method used in the collections and data-objects
 * endpoint. This class encapsulates the parameters for modifying permissions.
 */
public class ModifyPermissionsOperations {
    @JsonProperty("entity_name")
    private String entity_name;

    @JsonProperty("acl")
    private String acl;

    /**
     * Constructs a new instance of {@code ModifyPermissionsOperations}.
     *
     * @param entity_name The name of the user or group.
     * @param perm The permission level to set for the entity. Must be one of the following values:
     *             <ul>
     *                  <li>{@code Permission.NULL}</li>
     *                  <li>{@code Permission.READ}</li>
     *                  <li>{@code Permission.WRITE}</li>
     *                  <li>{@code Permission.OWN}</li>
     *             </ul>
     *
     * @throws IllegalArgumentException If the provided {@code perm} is not one of the accepted {@link Permission} values.
     */
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