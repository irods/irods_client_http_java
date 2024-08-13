package org.irods.properties.Collection;

import org.irods.operations.CollectionOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code touch()} in {@link CollectionOperations}, which updates the
 * mtime of an existing collection. Create an instance of this class and set the optional fields as desired, not all
 * fields have to be set.
 */
public class CollectionsTouchParams {
    private OptionalInt secondsSinceEpoch = OptionalInt.empty();
    private Optional<String> reference = Optional.empty();

    /**
     * Gets the mtime that will be assigned to the collection.
     *
     * @return An {@link OptionalInt} containing the mtime being assigned if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getSecondsSinceEpoch() {
        return secondsSinceEpoch;
    }

    /**
     * Sets the mtime to assign to the collection.
     *
     * @param secondsSinceEpoch The mtime to assign to the collection. Only the int value needs to be provided; it will
     *                          be wrapped in an {@link OptionalInt}.
     */
    public void setSecondsSinceEpoch(int secondsSinceEpoch) {

        this.secondsSinceEpoch = OptionalInt.of(secondsSinceEpoch);
    }

    /**
     * Gets the reference for the {@code touch} operation.
     *
     * @return An {@link Optional} containing the reference if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getReference() {
        return reference;
    }

    /**
     * Sets the absolute logical path of an object whose mtime will be copied to the collection.
     *
     * @param reference The absolute logical path of an object whose mtime will be copied to the collection. Only the
     *                  String value needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setReference(String reference) {
        this.reference = Optional.of(reference);
    }
}
