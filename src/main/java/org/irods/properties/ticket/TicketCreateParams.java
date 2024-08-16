package org.irods.properties.ticket;

import org.irods.operations.TicketOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code create()} in {@link TicketOperations}, which creates a new
 * ticket for a collection or data object. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 */
public class TicketCreateParams {
    private Optional<String> type = Optional.empty();
    private OptionalInt useCount = OptionalInt.empty();
    private OptionalInt writeDataObjectCount = OptionalInt.empty();
    private OptionalInt writeByteCount = OptionalInt.empty();
    private OptionalInt secondsUntilExpiration = OptionalInt.empty();
    private Optional<String> users = Optional.empty();
    private Optional<String> groups = Optional.empty();
    private Optional<String> hosts = Optional.empty();

    /**
     * Gets the type of the ticket.
     *
     * @return an {@link Optional} containing the {@code type} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getType() {
        return type;
    }

    /**
     * Sets the type of the ticket.
     *
     * @param type read or write. Defaults to read. Only the String value needs to be provided; it will be wrapped in
     *             an {@link Optional}.
     */
    public void setType(String type) {
        this.type = Optional.of(type);
    }

    /**
     * Gets the number of times the ticket can be used.
     *
     * @return an {@link OptionalInt} containing the {@code useCount} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getUseCount() {
        return useCount;
    }

    /**
     * Sets the number of times the ticket can be used.
     *
     * @param useCount Number of times the ticket can be used. Only the int value needs to be provided; it will be
     *                 wrapped in an {@link OptionalInt}.
     */
    public void setUseCount(int useCount) {
        this.useCount = OptionalInt.of(useCount);
    }

    /**
     * Gets the max number of writes that can be performed.
     *
     * @return an {@link OptionalInt} containing the {@code writeDataObjectCount} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getWriteDataObjectCount() {
        return writeDataObjectCount;
    }

    /**
     * Sets the max number of writes that can be performed.
     *
     * @param writeDataObjectCount Max number of writes that can be performed. Only the int value needs to be provided;
     *                             it will be wrapped in an {@link OptionalInt}.
     */
    public void setWriteDataObjectCount(int writeDataObjectCount) {
        this.writeDataObjectCount = OptionalInt.of(writeDataObjectCount);
    }

    /**
     * Gets the max number of bytes that can be written.
     *
     * @return an {@link OptionalInt} containing the {@code writeByteCount} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getWriteByteCount() {
        return writeByteCount;
    }

    /**
     * Sets the max number of bytes that can be written.
     *
     * @param writeByteCount Max number of bytes that can be written. Only the int value needs to be provided; it will
     *                       be wrapped in an {@link OptionalInt}.
     */
    public void setWriteByteCount(int writeByteCount) {
        this.writeByteCount = OptionalInt.of(writeByteCount);
    }

    /**
     * Gets the number of seconds before the ticket expires.
     *
     * @return an {@link OptionalInt} containing the {@code secondsUntilExpiration} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getSecondsUntilExpiration() {
        return secondsUntilExpiration;
    }

    /**
     * Sets the number of seconds before the ticket expires.
     *
     * @param secondsUntilExpiration Number of seconds before the ticket expires. Only the int value needs to be
     *                               provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setSecondsUntilExpiration(int secondsUntilExpiration) {
        this.secondsUntilExpiration = OptionalInt.of(secondsUntilExpiration);
    }

    /**
     * Gets the comma-delimited list of users allowed to use the ticket.
     *
     * @return an {@link Optional} containing the {@code users} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getUsers() {
        return users;
    }

    /**
     * Sets the comma-delimited list of users allowed to use the ticket.
     *
     * @param users Comma-delimited list of users allowed to use the ticket. Only the String value needs to be provided;
     *              it will be wrapped in an {@link Optional}.
     */
    public void setUsers(String users) {
        this.users = Optional.of(users);
    }

    /**
     * Gets the comma-delimited list of groups allowed to use the ticket.
     *
     * @return an {@link Optional} containing the {@code groups} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getGroups() {
        return groups;
    }

    /**
     * Sets the comma-delimited list of groups allowed to use the ticket.
     *
     * @param groups Comma-delimited list of groups allowed to use the ticket. Only the String value needs to be
     *               provided; it will be wrapped in an {@link Optional}.
     */
    public void setGroups(String groups) {
        this.groups = Optional.of(groups);
    }

    /**
     * Gets the comma-delimited list of hosts allowed to use the ticket.
     *
     * @return an {@link Optional} containing the {@code hosts} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getHosts() {
        return hosts;
    }

    /**
     * Sets the comma-delimited list of hosts allowed to use the ticket.
     *
     * @param hosts Comma-delimited list of hosts allowed to use the ticket. Only the String value needs to be provided;
     *              it will be wrapped in an {@link Optional}.
     */
    public void setHosts(String hosts) {
        this.hosts = Optional.of(hosts);
    }
}