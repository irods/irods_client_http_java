package org.example.Properties.Collection;

import java.util.Optional;

public class CollectionsStatParams {
    Optional<String> ticket = Optional.empty();

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = Optional.of(ticket);
    }
}
