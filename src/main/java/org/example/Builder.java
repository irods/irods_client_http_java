package org.example;

import java.io.IOException;

public class Builder {
    private String address;
    private String port;
    private String version;
    private User user;

    public Builder address(String address) {
        this.address = address;
        return this;
    }

    public Builder port(String port) {
        this.port = port;
        return this;
    }

    public Builder version(String version) {
        this.version = version;
        return this;
    }

    public Builder user(User user) {
        this.user = user;
        return this;
    }

    public IrodsClient build() throws IOException, InterruptedException {
        IrodsClient client = new IrodsClient(this);
        if (this.user != null) {
            client.authenticate(this.user);
        }
        return client;
    }


}