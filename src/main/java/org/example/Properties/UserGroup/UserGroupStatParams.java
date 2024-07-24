package org.example.Properties.UserGroup;

import java.util.Optional;

public class UserGroupStatParams {
    Optional<String> zone = Optional.empty();

    public Optional<String> getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = Optional.of(zone);
    }
}
