package org.irods.Properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class CollectionsListParams {
    private OptionalInt recurse = OptionalInt.empty();
    private Optional<String> ticket = Optional.empty();

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
