package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code calculateChecksum()} in {@link DataObjectOperations}, which
 * calculates the checksum for a data object. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class DataObjectCalculateChecksumParams {
    private Optional<String> resource = Optional.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private OptionalInt force = OptionalInt.empty();
    private OptionalInt all = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    /**
     * Gets the resource holding the target option.
     *
     * @return An {@link Optional} containing the resource holding the target option if set, otherwise an empty
     * {@code Optional}.
     */
    public Optional<String> getResource() {
        return resource;
    }

    /**
     * Sets the resource holding the target option.
     *
     * @param resource The resource holding the target option. Only the String value needs to be provided; it will be
     *                 wrapped in an {@link Optional}.
     */
    public void setResource(String resource) {

        this.resource = Optional.of(resource);
    }

    /**
     * Gets the replica number of the target replica.
     *
     * @return An {@link OptionalInt} containing the {@code replicaNum} value, or an empty {@code OptionalInt} if no
     * recursion value is set.
     */
    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    /**
     * Sets the replica number to target.
     *
     * @param replicaNum The replica number of the target replica. Only the int value needs to be provided; it will be
     *                   wrapped in an {@link OptionalInt}.
     */
    public void setReplicaNum(int replicaNum) {

        this.replicaNum = OptionalInt.of(replicaNum);
    }

    /**
     * Gets the {@code force} flag for the {@code calculateChecksum()} method to overwrite the existing checksum.
     *
     * @return An {@link OptionalInt} containing the {@code force} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getForce() {
        return force;
    }

    /**
     * Sets the {@code force} flag for the {@code calculateChecksum()} method to overwrite the existing checksum.
     *
     * @param force 0 or 1. Defaults to 0. Overwrite the existing checksum. Only the int value needs to be provided;
     *              it will be wrapped in an {@link OptionalInt}.
     */
    public void setForce(int force) {

        this.force = OptionalInt.of(force);
    }

    /**
     * Gets the {@code all} flag for the {@code calculateChecksum()} method, which decides whether to calculate checksums
     * for all replicas.
     *
     * @return An {@link OptionalInt} containing the {@code all} value being assigned if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getAll() {
        return all;
    }

    /**
     * Sets the {@code all} flag, which decides whether to calculate checksums for all replicas.
     *
     * @param all 0 or 1. Defaults to 0. Calculate checksums for all replicas. Only the int value needs to be provided;
     *            it will be wrapped in an {@link OptionalInt}.
     */
    public void setAll(int all) {

        this.all = OptionalInt.of(all);
    }

    /**
     * Gets the {@code admin} flag for the {@code calculateChecksum()} method, which decides whether to execute as
     * rodsadmin.
     *
     * @return An {@link OptionalInt} containing the {@code admin} flag value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getAdmin() {
        return admin;
    }

    /**
     * Sets the {@code admin} flag, which decides whether to execute as rodsadmin.
     *
     * @param admin 0 or 1. Defaults to 0. Execute as a rodsadmin. Only the int value needs to be provided; it will be
     *              wrapped in an {@link OptionalInt}.
     */
    public void setAdmin(int admin) {
        this.admin = OptionalInt.of(admin);
    }
}