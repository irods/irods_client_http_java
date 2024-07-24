package org.example.Properties.Ticket;

import java.util.Optional;
import java.util.OptionalInt;

public class TicketCreateParams {
    Optional<String> type = Optional.empty();
    OptionalInt useCount = OptionalInt.empty();
    OptionalInt writeDataObjectCount = OptionalInt.empty();
    OptionalInt writeByteCount = OptionalInt.empty();
    OptionalInt secondsUntilExpiration = OptionalInt.empty();
    Optional<String> users = Optional.empty();
    Optional<String> groups = Optional.empty();
    Optional<String> hosts = Optional.empty();

    public Optional<String> getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Optional.of(type);
    }

    public OptionalInt getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = OptionalInt.of(useCount);
    }

    public OptionalInt getWriteDataObjectCount() {
        return writeDataObjectCount;
    }

    public void setWriteDataObjectCount(int writeDataObjectCount) {
        this.writeDataObjectCount = OptionalInt.of(writeDataObjectCount);
    }

    public OptionalInt getWriteByteCount() {
        return writeByteCount;
    }

    public void setWriteByteCount(int writeByteCount) {
        this.writeByteCount = OptionalInt.of(writeByteCount);
    }

    public OptionalInt getSecondsUntilExpiration() {
        return secondsUntilExpiration;
    }

    public void setSecondsUntilExpiration(int secondsUntilExpiration) {
        this.secondsUntilExpiration = OptionalInt.of(secondsUntilExpiration);
    }

    public Optional<String> getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = Optional.of(users);
    }

    public Optional<String> getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = Optional.of(groups);
    }

    public Optional<String> getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = Optional.of(hosts);
    }
}
