package org.irods.properties.Collection;

import org.irods.operations.CollectionOperations;

import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code remove()} in {@link CollectionOperations}, which removes a
 * collection. Create an instance of this class and set the optional fields as desired, not all fields have to be set.
 */
public class CollectionsRemoveParams {
    private OptionalInt recurse = OptionalInt.empty();
    private OptionalInt noTrash = OptionalInt.empty();

    /**
     * Gets the {@code recurse} flag.
     *
     * @return An {@link OptionalInt} containing the {@code recurse} flag, or an empty {@code OptionalInt} if no value
     * is set.
     */
    public OptionalInt getRecurse() {
        return recurse;
    }

    /**
     * Sets the {@code recurse} flag for removing collections.
     *
     * @param recurse 0 or 1. Defaults to 0. If set to 1, the contents of the collection will be removed. Only the int
     *                value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setRecurse(int recurse) {

        this.recurse = OptionalInt.of(recurse);
    }

    /**
     * Returns the {@code noTrash} flag.
     *
     * @return An {@link OptionalInt} containing the {@code noTrash} flag, or an empty {@code OptionalInt} if no
     * value is set.
     */
    public OptionalInt getNoTrash() {
        return noTrash;
    }

    /**
     * Sets the {@code noTrash} flag for removing collections.
     *
     * @param noTrash The recurse value to set. 0 or 1. Defaults to 0. If set to 1, the collection is permanently
     *                removed. Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setNoTrash(int noTrash) {

        this.noTrash = OptionalInt.of(noTrash);
    }
}