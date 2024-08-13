package org.irods.serialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.irods.util.MetadataOperation;

/**
 * Represents the JSON object for the {@code modifyMetadata()} method used in the collections, data-objects, resources,
 * and users-groups endpoints. This class encapsulates the parameters for modifying metadata.
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

    /**
     * Constructs a new {@code ModifyMetadataOperations} instance.
     *
     * @param metadataOperation The operation to be performed on the metadata, represented by a
     * {@link MetadataOperation} enumeration.
     * @param attribute The metadata attribute to modify.
     * @param value The new value to set for the metadata attribute.
     * @param units The units for the metadata value, which may be {@code null} if not applicable.
     */
    public ModifyMetadataOperations(MetadataOperation metadataOperation, String attribute, String value, String units) {
        this.operation = metadataOperation.getValue();
        this.attribute = attribute;
        this.value = value;
        this.units = units;
    }
}