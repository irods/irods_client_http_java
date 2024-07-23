package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectParallelWriteInitParams {
    OptionalInt truncate = OptionalInt.empty();
    OptionalInt append = OptionalInt.empty();
    Optional<String> ticket = Optional.empty();

    public OptionalInt getTruncate() {
        return truncate;
    }

    public void setTruncate(int truncate) {

        this.truncate = OptionalInt.of(truncate);
    }

    public OptionalInt getAppend() {
        return append;
    }

    public void setAppend(int append) {

        this.append = OptionalInt.of(append);
    }

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {

        this.ticket = Optional.of(ticket);
    }
}
