package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModifyMetadataOperations {
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("attribute")
    private String attribute;
    @JsonProperty("value")
    private String value;
    @JsonProperty("units")
    private String units; // optional

    public ModifyMetadataOperations(String operation, String attribute, String value, String units) {
        this.operation = operation;
        this.attribute = attribute;
        this.value = value;
        this.units = units;
    }
}
