package org.example.Properties.Query;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.OptionalInt;

public class QueryExecuteGenqueryParams {
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt count = OptionalInt.empty();
    private OptionalInt caseSensitive = OptionalInt.empty();
    private OptionalInt distinct = OptionalInt.empty();
    private Optional<String> parser = Optional.empty();
    private OptionalInt sqlOnly = OptionalInt.empty();
    private Optional<String> zone = Optional.empty();

    public OptionalInt getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = OptionalInt.of(offset);
    }

    public OptionalInt getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = OptionalInt.of(count);
    }

    public OptionalInt getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(int caseSensitive) {
        this.caseSensitive = OptionalInt.of(caseSensitive);
    }

    public OptionalInt getDistinct() {
        return distinct;
    }

    public void setDistinct(int distinct) {
        this.distinct = OptionalInt.of(distinct);
    }

    public Optional<String> getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = Optional.of(parser);
    }

    public OptionalInt getSqlOnly() {
        return sqlOnly;
    }

    public void setSqlOnly(int sqlOnly) {
        this.sqlOnly = OptionalInt.of(sqlOnly);
    }

    public Optional<String> getZone() {
        return zone;
    }

    public void setZon(String zone) {
        this.zone = Optional.of(zone);
    }
}
