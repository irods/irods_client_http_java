package org.example.Properties.DataObject;

import java.util.Optional;

public class DataObjectStatParams {
    Optional<String> ticket = Optional.empty();

    public Optional<String> getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = Optional.of(ticket);
    }
}
