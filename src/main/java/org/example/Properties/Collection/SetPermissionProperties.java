package org.example.Properties.Collection;

import java.util.OptionalInt;

public class SetPermissionProperties {
    OptionalInt admin = OptionalInt.empty();

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(OptionalInt admin) {
        this.admin = admin;
    }
}
