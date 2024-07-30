package org.example.Properties.DataObject;

import java.util.OptionalInt;

public class DataObjectRemoveParams {
    private  OptionalInt noTrash = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    public OptionalInt getNoTrash() {
        return noTrash;
    }

    public void setNoTrash(int noTrash) {

        this.noTrash = OptionalInt.of(noTrash);
    }

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {

        this.admin = OptionalInt.of(admin);
    }
}
