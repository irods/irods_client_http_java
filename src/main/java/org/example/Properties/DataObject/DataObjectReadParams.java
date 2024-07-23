package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectReadParams {
    OptionalInt offset = OptionalInt.empty();
    OptionalInt count = OptionalInt.empty();
    Optional<String> ticket = Optional.empty();

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

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {

        this.ticket = Optional.of(ticket);
    }
}
