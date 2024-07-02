package org.example.Properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class ListProperties {
    OptionalInt recurse = OptionalInt.empty();
    Optional<String> ticket = Optional.empty();

    public OptionalInt getRecurse() {
        return recurse;
    }

    public void setRecurse(OptionalInt recurse) {
        this.recurse = recurse;
    }

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(Optional<String> ticket) {
        this.ticket = ticket;
    }
}
