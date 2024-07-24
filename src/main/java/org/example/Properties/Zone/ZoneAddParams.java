package org.example.Properties.Zone;

import java.util.Optional;

public class ZoneAddParams {
    Optional<String> connectionInfo = Optional.empty();
    Optional<String> comment = Optional.empty();

    public Optional<String> getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = Optional.of(connectionInfo);
    }

    public Optional<String> getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = Optional.of(comment);
    }
}
