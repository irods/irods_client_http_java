package org.example.Properties.DataObject;

import java.util.OptionalInt;

public class DataObjectTrimParams {
    OptionalInt catalogOnly = OptionalInt.empty();
    OptionalInt admin = OptionalInt.empty();

    public OptionalInt getCatalogOnly() {
        return catalogOnly;
    }

    public void setCatalogOnly(int catalogOnly) {
        this.catalogOnly = OptionalInt.of(catalogOnly);
    }

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = OptionalInt.of(admin);
    }
}
