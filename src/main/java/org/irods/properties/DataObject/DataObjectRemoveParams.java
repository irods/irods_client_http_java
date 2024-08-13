package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code remove()} in {@link DataObjectOperations}, which removes a
 * data object or unregisters all replicas. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class DataObjectRemoveParams {
    private  OptionalInt noTrash = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    /**
     * Gets the {@code noTrash} flag, which decides whether to permanently delete the data object.
     *
     * @return an {@link OptionalInt} containing the {@code noTrash} flag, or an empty {@code OptionalInt} if no
     * value is set.
     */
    public OptionalInt getNoTrash() {
        return noTrash;
    }

    /**
     * Sets the {@code noTrash} flag, which decides whether to permanently delete the data object.
     *
     * @param noTrash 0 or 1. Defaults to 0. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setNoTrash(int noTrash) {

        this.noTrash = OptionalInt.of(noTrash);
    }

    /**
     * Gets the {@code admin} flag for the {@code remove()} method, which decides whether to execute as rodsadmin.
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