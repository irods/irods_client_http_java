package org.irods.properties.Query;

import org.irods.operations.QueryOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code executeSpecifcQuery()} in {@link QueryOperations}, which executes
 * a specific query and returns the results. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class QueryExecuteSpecifcQueryParams {
    private Optional<String> args = Optional.empty();
    private Optional<String> argsDelimiter = Optional.empty();
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt count = OptionalInt.empty();

    /**
     * Returns a list of arguments.
     *
     * @return an {@link Optional} containing the {@code args} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getArgs() {
        return args;
    }

    /**
     * Sets a list of arguments.
     *
     * @param args A list of arguments. Only the String value needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setArgs(String args) {
        this.args = Optional.of(args);
    }

    /**
     * Gets the delimiter used to separate arguments. Defaults to comma (,).
     *
     * @return an {@link Optional} containing the {@code argsDelimiter} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getArgsDelimiter() {
        return argsDelimiter;
    }

    /**
     * Sets the delimiter used to separate arguments.
     *
     * @param argsDelimiter The delimiter used to separate arguments. Defaults to comma (,). Only the String value needs
     *                      to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setArgsDelimiter(String argsDelimiter) {
        this.argsDelimiter = Optional.of(argsDelimiter);
    }

    /**
     * Gets the number of rows to skip.
     *
     * @return an {@link OptionalInt} containing the {@code offset} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getOffset() {
        return offset;
    }

    /**
     * Sets the number of rows to skip.
     *
     * @param offset The number of rows to skip. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setOffset(int offset) {
        this.offset = OptionalInt.of(offset);
    }

    /**
     * Gets the number of rows to return. Default set by administrator.
     *
     * @return an {@link OptionalInt} containing the {@code count} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getCount() {
        return count;
    }

    /**
     * Sets the number of rows to return.
     *
     * @param count The number of rows to return. Default set by administrator. Will be clamped to the range [1, N]
     *              where N represents the max number of rows that can be returned by any query. The max number of rows
     *              is defined by the administrator of the iRODS HTTP API and can be obtained by sending an HTTP GET
     *              request to the /info endpoint. See {@code InformationOperations} for more details.
     *              <p>
     *                  Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     *              </p>
     */
    public void setCount(int count) {
        this.count = OptionalInt.of(count);
    }
}