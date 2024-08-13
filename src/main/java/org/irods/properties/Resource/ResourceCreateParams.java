package org.irods.properties.Resource;

import org.irods.operations.ResourceOperations;

import java.util.Optional;

/**
 * This class represents the optional parameters for {@code create()} in {@link ResourceOperations}, which creates a new
 * resource. Create an instance of this class and set the optional fields as desired, not all fields have to be set.
 */
public class ResourceCreateParams {
    private Optional<String> host = Optional.empty();
    private Optional<String> vaultPath = Optional.empty();
    private Optional<String> context = Optional.empty();

    /**
     * Gets the host for the resource. Depends on the resource's type. May be required.
     *
     * @return an {@link Optional} containing the {@code host} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getHost() {
        return host;
    }

    /**
     * Sets the host for the resource. Depends on the resource's type. May be required.
     *
     * @param host The host for the resource. Depends on the resource's type. May be required. Only the String value
     *             needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setHost(String host) {
        this.host = Optional.of(host);
    }

    /**
     * Gets the vault path for the resource. Depends on the resource's type. May be required.
     *
     * @return an {@link Optional} containing the {@code vaultPath} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getVaultPath() {
        return vaultPath;
    }

    /**
     * Sets the vault path for the resource. Depends on the resource's type. May be required.
     *
     * @param vaultPath The vault path for the resource. Depends on the resource's type. May be required. Only the
     *                  String value needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setVaultPath(String vaultPath) {
        this.vaultPath = Optional.of(vaultPath);
    }

    /**
     * Gets the context of the resource. Depends on the resource's type. May be required.
     *
     * @return an {@link Optional} containing the {@code context} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getContext() {
        return context;
    }

    /**
     * Sets the context of the resource. Depends on the resource's type. May be required.
     *
     * @param context The context of the resource. Depends on the resource's type. May be required. Only the String
     *                value needs to be provided; it will be wrapped in an {@link Optional}.
     */
    public void setContext(String context) {
        this.context = Optional.of(context);
    }
}