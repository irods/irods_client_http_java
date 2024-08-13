package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code register()} in {@link DataObjectOperations},
 * which registers a new data object and/or replica into the catalog. Create an instance of this class and set the
 * optional fields as desired, not all fields have to be set.
 */
public class DataObjectRegisterParams {
    private OptionalInt asAdditionalReplica = OptionalInt.empty();
    private OptionalInt dataSize = OptionalInt.empty();
    private Optional<String> checksum = Optional.empty();

    /**
     * Gets the {@code asAdditionalReplica} flag, which decides whether to register as an additional replica for an
     * existing data object.
     *
     * @return an {@link OptionalInt} containing the {@code asAdditionalReplica} flag value if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getAsAdditionalReplica() {
        return asAdditionalReplica;
    }

    /**
     * Sets the {@code asAdditionalReplica} flag, which decides whether to register as an additional replica for an
     * existing data object.
     *
     * @param asAdditionalReplica 0 or 1. Defaults to 0. Register as an additional replica for an existing data object.
     *                            Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setAsAdditionalReplica(int asAdditionalReplica) {
        this.asAdditionalReplica = OptionalInt.of(asAdditionalReplica);
    }

    /**
     * Get the size of the replica in bytes.
     *
     * @return an {@link OptionalInt} containing the {@code dataSize} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getDataSize() {
        return dataSize;
    }

    /**
     * Sets the size of the replica in bytes.
     *
     * @param dataSize The size of the replica in bytes. Only the int value needs to be provided; it will be wrapped in
     *                an {@link OptionalInt}.
     */
    public void setDataSize(int dataSize) {
        this.dataSize = OptionalInt.of(dataSize);
    }

    /**
     * Get the checksum associated with the replica.
     *
     * @return an {@link Optional} containing the {@code checksum} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getChecksum() {
        return checksum;
    }

    /**
     * Sets the checksum to associate with the replica.
     *
     * @param checksum The checksum to associate with the replica. Only the String value needs to be provided; it will
     *                 be wrapped in an {@link Optional}.
     */
    public void setChecksum(String checksum) {
        this.checksum = Optional.of(checksum);
    }
}