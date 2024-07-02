package org.example.Properties.Collection;

import java.util.Optional;

public class StatProperties {
    Optional<String> ticket = Optional.empty();

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(Optional<String> ticket) {
        this.ticket = ticket;
    }
}
