package org.example.Properties.Collection;

import java.util.OptionalInt;

public class CollectionsSetPermissionParams {
    OptionalInt admin = OptionalInt.empty();

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {

        this.admin = OptionalInt.of(admin);
    }
}
