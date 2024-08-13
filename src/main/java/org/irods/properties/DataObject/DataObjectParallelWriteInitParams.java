package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code parallelWriteInit()} in {@link DataObjectOperations},
 * which initializes server-side state used for writing to a data object in parallel. Create an instance of this class
 * and set the optional fields as desired, not all fields have to be set.
 */
public class DataObjectParallelWriteInitParams {
    private OptionalInt truncate = OptionalInt.empty();
    private OptionalInt append = OptionalInt.empty();
    private Optional<String> ticket = Optional.empty();

    /**
     * Gets the {@code truncate} flag, which decides whether to truncate the data object before writing.
     *
     * @return An {@link OptionalInt} containing the {@code truncate} flag value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getTruncate() {
        return truncate;
    }

    /**
     * Sets the {@code truncate} flag, which decides whether to truncate the data object before writing.
     *
     * @param truncate 0 or 1. Defaults to 1. Truncates the data object before writing. Only the int value needs to be
     *                 provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setTruncate(int truncate) {

        this.truncate = OptionalInt.of(truncate);
    }

    /**
     * Gets the {@code append} flag, which decides whether to append the bytes to the data object.
     *
     * @return An {@link OptionalInt} containing the {@code append} flag value if set, otherwise an empty {@code OptionalInt}.
     **/
    public OptionalInt getAppend() {
        return append;
    }

    /**
     * Sets the {@code append} flag, which decides whether to append the bytes to the data object.
     *
     * @param append 0 or 1. Defaults to 0. Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setAppend(int append) {

        this.append = OptionalInt.of(append);
    }

    /**
     * Gets the ticket to enable for all streams.
     *
     * @return An {@link Optional} containing the {@code ticket} value if set, otherwise an empty {@code Optional}.

     */
    public Optional<String> getTicket() {
        return ticket;
    }

    /**
     * Sets the ticket to enable for all streams.
     * @param ticket The ticket to enable for all streams. Only the String value needs to be provided; it will be
     *               wrapped in an {@link Optional}.
     */
    public void setTicket(String ticket) {

        this.ticket = Optional.of(ticket);
    }
}
