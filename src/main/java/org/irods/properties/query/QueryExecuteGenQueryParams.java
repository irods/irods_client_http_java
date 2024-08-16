package org.irods.properties.query;

import org.irods.operations.QueryOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code executeGenQuery()} in {@link QueryOperations}, which executes
 * a GenQuery string and returns the results. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class QueryExecuteGenQueryParams {
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt count = OptionalInt.empty();
    private OptionalInt caseSensitive = OptionalInt.empty();
    private OptionalInt distinct = OptionalInt.empty();
    private Optional<String> parser = Optional.empty();
    private OptionalInt sqlOnly = OptionalInt.empty();
    private Optional<String> zone = Optional.empty();

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

    /**
     * Gets the {@code caseSensitive} flag, which decides whether to execute a case-sensitive/insensitive query.
     * Defaults to 1. Only supported by GenQuery1.
     *
     * @return an {@link OptionalInt} containing the {@code caseSensitive} flag value if set, otherwise an empty
     * {@code OptionalInt}.
     */
    public OptionalInt getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Sets the {@code caseSensitive} flag, which decides whether to execute a case-sensitive/insensitive query.
     * Defaults to 1. Only supported by GenQuery1.
     *
     * @param caseSensitive 0 or 1. Defaults to 1. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setCaseSensitive(int caseSensitive) {
        this.caseSensitive = OptionalInt.of(caseSensitive);
    }

    /**
     * Gets the {@code distinct} flag, which decides whether to collapse duplicate rows. Only supported by GenQuery1.
     *
     * @return an {@link OptionalInt} containing the {@code distinct} flag value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getDistinct() {
        return distinct;
    }

    /**
     * Sets the {@code distinct} flag, which decides whether to collapse duplicate rows. Only supported by GenQuery1.
     *
     * @param distinct 0 or 1. Defaults to 1. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setDistinct(int distinct) {
        this.distinct = OptionalInt.of(distinct);
    }

    /**
     * Gets the {@code parser} value which indicates whether genquery1 or genquery2 is being used.
     *
     * @return an {@link Optional} containing the {@code parser} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getParser() {
        return parser;
    }

    /**
     * Sets the {@code parser} value which indicates whether genquery1 or genquery2 is being used.

     * @param parser genquery1 or genquery2. Defaults to genquery1. Only the String value needs to be provided; it will
     *               be wrapped in an {@link Optional}.
     */
    public void setParser(String parser) {
        this.parser = Optional.of(parser);
    }

    /**
     * Gets the {@code sqlOnly} flag. Only supported by GenQuery2.
     *
     * @return an {@link OptionalInt} containing the {@code sqlOnly} flag value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getSqlOnly() {
        return sqlOnly;
    }

    /**
     * Sets the {@code sqlOnly} flag. Only supported by GenQuery2.
     *
     * @param sqlOnly 0 or 1. Defaults to 0. Only the int value needs to be provided; it will be wrapped in an
     * {@link OptionalInt}.
     */
    public void setSqlOnly(int sqlOnly) {
        this.sqlOnly = OptionalInt.of(sqlOnly);
    }

    /**
     * Gets the zone name.
     *
     * @return an {@link Optional} containing the {@code zone} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getZone() {
        return zone;
    }

    /**
     * Sets the zone name.
     *
     * @param zone The zone name. Defaults to the local zone. Only the String value needs to be provided; it will be
     *             wrapped in an {@link Optional}.
     */
    public void setZon(String zone) {
        this.zone = Optional.of(zone);
    }
}