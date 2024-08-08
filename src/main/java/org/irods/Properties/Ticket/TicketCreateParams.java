package org.irods.Properties.Ticket;

import java.util.Optional;
import java.util.OptionalInt;

public class TicketCreateParams {
    private Optional<String> type = Optional.empty();
    private OptionalInt useCount = OptionalInt.empty();
    private OptionalInt writeDataObjectCount = OptionalInt.empty();
    private OptionalInt writeByteCount = OptionalInt.empty();
    private OptionalInt secondsUntilExpiration = OptionalInt.empty();
    private Optional<String> users = Optional.empty();
    private Optional<String> groups = Optional.empty();
    private Optional<String> hosts = Optional.empty();

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
