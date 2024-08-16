package org.irods.properties.collection;

import org.irods.operations.CollectionOperations;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code list()} in {@link CollectionOperations}, which lists the
 * contents of a collection. Create an instance of this class and set the optional fields as desired, not all fields
 * have to be set.
 */
public class CollectionsListParams {
    private OptionalInt recurse = OptionalInt.empty();
    private Optional<String> ticket = Optional.empty();

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
     * Sets the {@code recurse} flag.
     *
     * @param recurse 0 or 1. Defaults to 0. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setRecurse(int recurse) {
        this.recurse = OptionalInt.of(recurse);
    }

    /**
     * Gets the {@code ticket} flag.
     *
     * @return An {@link Optional} containing the {@code ticket} flag, or an empty {@code Optional} if no value is set.
     */
    public Optional<String> getTicket() {
        return ticket;
    }

    /**
     * Sets the ticket value.
     *
     * @param ticket The ticket value to set. Only the string value needs to be provided, it will be wrapped in an
     * {@link Optional}.
     */
    public void setTicket(String ticket) {
        this.ticket = Optional.of(ticket);
    }
}