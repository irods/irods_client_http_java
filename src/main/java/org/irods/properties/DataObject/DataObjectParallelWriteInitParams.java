package org.irods.properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectParallelWriteInitParams {
    private OptionalInt truncate = OptionalInt.empty();
    private OptionalInt append = OptionalInt.empty();
    private Optional<String> ticket = Optional.empty();

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
