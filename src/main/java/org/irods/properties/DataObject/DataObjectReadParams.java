package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code read()} in {@link DataObjectOperations},
 * which reads bytes from a data object. Create an instance of this class and set the optional fields as desired, not
 * all fields have to be set.
 */
public class DataObjectReadParams {
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt count = OptionalInt.empty();
    private Optional<String> ticket = Optional.empty();

    /**
     * Gets the number of bytes to skip.
     *
     * @return An {@link OptionalInt} containing the {@code offset} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getOffset() {
        return offset;
    }

    /**
     * Sets the number of bytes to skip.
     *
     * @param offset Number of bytes to skip. Defaults to 0. Only the int value needs to be provided; it will be
     *               wrapped in an {@link OptionalInt}.
     */
    public void setOffset(int offset) {

        this.offset = OptionalInt.of(offset);
    }

    /**
     * Gets the number of bytes to read.
     *
     * @return An {@link OptionalInt} containing the {@code count} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getCount() {
        return count;
    }

    /**
     * Sets the number of bytes to read.
     *
     * @param count Number of bytes to read. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setCount(int count) {

        this.count = OptionalInt.of(count);
    }

    /**
     * Gets the ticket to enable before reading the data object.
     *
     * @return An {@link Optional} containing the {@code ticket} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getTicket() {
        return ticket;
    }

    /**
     * Sets the ticket to enable before reading the data object.
     *
     * @param ticket The ticket to enable before reading the data object. Only the String value needs to be provided;
     *               it will be wrapped in an {@link Optional}.
     */
    public void setTicket(String ticket) {

        this.ticket = Optional.of(ticket);
    }
}
