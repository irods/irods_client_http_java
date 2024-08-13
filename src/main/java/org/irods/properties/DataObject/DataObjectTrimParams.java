package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code trim()} in {@link DataObjectOperations}, which trims an
 * existing replica or removes its catalog entry. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class DataObjectTrimParams {
    private OptionalInt catalogOnly = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    /**
     * Gets the {@code catalogOnly} flag, which decides whether to remove only the catalog entry.
     *
     * @return an {@link OptionalInt} containing the {@code catalogOnly} value being assigned if set, otherwise an
     * empty {@code OptionalInt}.
     */
    public OptionalInt getCatalogOnly() {
        return catalogOnly;
    }

    /**
     * Sets the {@code computeChecksums} flag, which decides whether to remove only the catalog entry.
     *
     * @param catalogOnly 0 or 1. If set to 1, removes only the catalog entry. Only the int value needs to be provided;
     *                    it will be wrapped in an {@link OptionalInt}.
     */
    public void setCatalogOnly(int catalogOnly) {
        this.catalogOnly = OptionalInt.of(catalogOnly);
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
