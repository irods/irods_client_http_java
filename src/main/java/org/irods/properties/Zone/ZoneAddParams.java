package org.irods.properties.Zone;

import org.irods.operations.ZoneOperations;

import java.util.Optional;

/**
 * This class represents the optional parameters for {@code add()} in {@link ZoneOperations}, which adds a remote zone to
 * the local zone. Create an instance of this class and set the optional fields as desired, not all fields have to be set.
 */
public class ZoneAddParams {
    private Optional<String> connectionInfo = Optional.empty();
    private Optional<String> comment = Optional.empty();

    /**
     * Gets the host and port to connect to.
     *
     * @return an {@link Optional} containing the {@code connectionInfo} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getConnectionInfo() {
        return connectionInfo;
    }

    /**
     * Sets the host and port to connect to.
     *
     * @param connectionInfo The port and host to connect to. Only the String value needs to be provided; it will be
     *                       wrapped in an {@link Optional}.
     */
    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = Optional.of(connectionInfo);
    }

    /**
     * Gets the comment attached to the remote zone.
     *
     * @return an {@link Optional} containing the {@code comment} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getComment() {
        return comment;
    }

    /**
     * Sets the comment to attach to the remote zone.
     *
     * @param comment The comment to attach to the remote zone. Only the String value needs to be provided; it will be
     *                wrapped in an {@link Optional}.
     */
    public void setComment(String comment) {
        this.comment = Optional.of(comment);
    }
}