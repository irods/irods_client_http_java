package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code touch()} in {@link DataObjectOperations}, which updates the
 * mtime of an existing data object or creates a new data object if one doesn't exist. Create an instance of this class
 * and set the optional fields as desired, not all fields have to be set.
 */
public class DataObjectTouchParams {
    private OptionalInt noCreate = OptionalInt.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private Optional<String> leafResource = Optional.empty();
    private OptionalInt secondsSinceEpoch = OptionalInt.empty();
    private Optional<String> reference = Optional.empty();

    /**
     * Gets the {@code noCreate} flag, which decides whether a data object will be created (value of 0) or not (value of 1).
     *
     * @return an {@link OptionalInt} containing the {@code noCreate} value being assigned if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getNoCreate() {
        return noCreate;
    }

    /**
     * Sets the {@code noCreate} flag, which decides whether a data object will be created.
     *
     * @param noCreate 0 or 1. Defaults to 0. If set to 1, no data objects will be created. Only the int value needs to
     *                 be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setNoCreate(int noCreate) {
        this.noCreate = OptionalInt.of(noCreate);
    }

    /**
     * Gets the value that {@code replicaNum} is set to.
     *
     * @return an {@link OptionalInt} containing the {@code replicaNum} value being assigned if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    /**
     * Sets the replica to update. The replica must exist.
     *
     * @param replicaNum The replica number to set. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setReplicaNum(int replicaNum) {
        this.replicaNum = OptionalInt.of(replicaNum);
    }

    /**
     * Gets the value that {@code leafResource} is set to.
     *
     * @return an {@link Optional} containing the {@code leafResource} value being assigned if set, otherwise an empty
     * {@code Optional}.
     */
    public Optional<String> getLeafResource() {
        return leafResource;
    }

    /**
     * Sets the resource holding an existing replica. If it does not exist, it will be created on the specified resource.
     *
     * @param leafResource The resource holding the replica. Only the String value needs to be provided; it will be
     *                     wrapped in an {@link Optional}.
     */
    public void setLeafResource(String leafResource) {
        this.leafResource = Optional.of(leafResource);
    }

    /**
     * Gets the value that {@code secondsSinceEpoch} is set to.
     *
     * @return an {@link OptionalInt} containing the {@code secondsSinceEpoch} value being assigned if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getSecondsSinceEpoch() {
        return secondsSinceEpoch;
    }

    /**
     * Sets the mtime to assign to the replica.
     *
     * @param secondsSinceEpoch The mtime being assigned to the replica. Only the int value needs to be provided; it
     *                          will be wrapped in an {@link OptionalInt}.
     */
    public void setSecondsSinceEpoch(int secondsSinceEpoch) {
        this.secondsSinceEpoch = OptionalInt.of(secondsSinceEpoch);
    }

    /**
     * Gets the value that {@code reference} is set to.
     *
     * @return an {@link Optional} containing the {@code reference} value being assigned if set, otherwise an empty
     * {@code Optional}.
     */
    public Optional<String> getReference() {
        return reference;
    }

    /**
     * Sets the absolute logical path of an object whose mtime will be copied to the data object.
     *
     * @param reference The absolute logical path of an object whose mtime will be copied to the data object. Only the
     *                  String value needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setReference(String reference) {
        this.reference = Optional.of(reference);
    }
}