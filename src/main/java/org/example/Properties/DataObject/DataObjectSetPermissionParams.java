package org.example.Properties.DataObject;

import java.util.OptionalInt;

public class DataObjectSetPermissionParams {
    OptionalInt admin = OptionalInt.empty();

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {

        this.admin = OptionalInt.of(admin);
    }
}
