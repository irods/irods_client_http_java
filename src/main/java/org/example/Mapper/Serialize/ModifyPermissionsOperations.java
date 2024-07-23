package org.example.Mapper.Serialize;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * For the modify_permissions() method.
 * Represents the JSON object that is passed into the operations parameter
 */
public class ModifyPermissionsOperations {
    @JsonProperty("")
    private String entity_name;

    @JsonProperty("acl")
    private String acl;

    public ModifyPermissionsOperations(String entity_name, String acl) {
        this.entity_name = entity_name;
        this.acl = acl;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{ \"error\": \"Unable to serialize to JSON\" }";
        }
    }
}
