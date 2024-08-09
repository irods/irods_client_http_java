package org.irods.serialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.irods.util.MetadataOperation;


/**
 * For the modify_metadata() method.
 * Represents the JSON object that is passed into the operations parameter
 */
public class ModifyMetadataOperations {
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("attribute")
    private String attribute;
    @JsonProperty("value")
    private String value;
    @JsonProperty("units")
    private String units; // optional

    public ModifyMetadataOperations(MetadataOperation metadataOperation, String attribute, String value, String units) {
        this.operation = metadataOperation.getValue();
        this.attribute = attribute;
        this.value = value;
        this.units = units;
    }
}