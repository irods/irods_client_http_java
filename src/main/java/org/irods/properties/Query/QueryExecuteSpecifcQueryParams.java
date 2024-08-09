package org.irods.properties.Query;

import java.util.Optional;
import java.util.OptionalInt;

public class QueryExecuteSpecifcQueryParams {
    private Optional<String> args = Optional.empty();
    private Optional<String> argsDelimiter = Optional.empty();
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt count = OptionalInt.empty();

    public Optional<String> getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = Optional.of(args);
    }

    public Optional<String> getArgsDelimiter() {
        return argsDelimiter;
    }

    public void setArgsDelimiter(String argsDelimiter) {
        this.argsDelimiter = Optional.of(argsDelimiter);
    }

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
}
