package org.irods.Properties.Resource;

import java.util.Optional;

public class ResourceCreateParams {
    private Optional<String> host = Optional.empty();
    private Optional<String> vaultPath = Optional.empty();
    private Optional<String> context = Optional.empty();

    public Optional<String> getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = Optional.of(host);
    }

    public Optional<String> getVaultPath() {
        return vaultPath;
    }

    public void setVaultPath(String vaultPath) {
        this.vaultPath = Optional.of(vaultPath);
    }

    public Optional<String> getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = Optional.of(context);
    }
}
