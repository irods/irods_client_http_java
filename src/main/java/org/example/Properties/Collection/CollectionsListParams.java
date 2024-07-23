package org.example.Properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class CollectionsListParams {
    OptionalInt recurse = OptionalInt.empty();
    Optional<String> ticket = Optional.empty();

    public OptionalInt getRecurse() {
        return recurse;
    }

    public void setRecurse(int recurse) {

        this.recurse = OptionalInt.of(recurse);
    }

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {

        this.ticket = Optional.of(ticket);
    }
}
