package org.irods.properties.data_object;

import org.irods.operations.DataObjectOperations;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code verifyChecksum()} in {@link DataObjectOperations}, which
 * verifies the checksum information for a data object. Create an instance of this class and set the optional fields as
 * desired, not all fields have to be set.
 */
public class DataObjectVerifyChecksumParams {
    private Optional<String> resource = Optional.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private OptionalInt computeChecksums = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    /**
     * Gets the resource holding the target replica.
     *
     * @return an {@link Optional} containing the {@code resource} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getResource() {
        return resource;
    }

    /**
     * Sets the resource holding the target replica.
     *
     * @param resource The resource holding the target replica. Only the String value needs to be provided; it will be
     *                 wrapped in an {@link Optional}.
     */
    public void setResource(String resource) {
        this.resource = Optional.of(resource);
    }

    /**
     * Gets the replica number of the target replica.
     *
     * @return The replica number of the target replica.
     */
    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    /**
     * Sets the replica number of the target replica.
     *
     * @param replicaNum The replica number of the target replica. Only the int value needs to be provided; it will be
     *                   wrapped in an {@link OptionalInt}.
     */
    public void setReplicaNum(int replicaNum) {
        this.replicaNum = OptionalInt.of(replicaNum);
    }

    /**
     * Gets the {@code computeChecksums} flag, which decides whether to skip the checksum calculation step.
     *
     * @return an {@link OptionalInt} containing the {@code computeChecksums} value being assigned if set, otherwise an
     * empty {@code OptionalInt}.
     */
    public OptionalInt getComputeChecksums() {
        return computeChecksums;
    }

    /**
     * Sets the {@code computeChecksums} flag, which decides whether to skip the checksum calculation step.
     *
     * @param computeChecksums 0 or 1. Defaults to 1. Can be used to skip the checksum calculation step. Only the int
     *                         value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setComputeChecksums(int computeChecksums) {
        this.computeChecksums = OptionalInt.of(computeChecksums);
    }

    /**
     * Gets the {@code admin} flag, which decides whether to execute as rodsadmin.
     *
     * @return an {@link OptionalInt} containing the {@code admin} flag value if set, otherwise an empty {@code OptionalInt}.
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