package org.example.Properties.UserGroup;

import java.util.Optional;

public class UserGroupCreateUserParams {
    Optional<String> userType = Optional.empty();

    public Optional<String> getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = Optional.of(userType);
    }
}
