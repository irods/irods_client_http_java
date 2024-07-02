package org.example.Properties.Collection;
import java.util.OptionalInt;

public class RemoveProperties {
    OptionalInt recurse = OptionalInt.empty();
    OptionalInt noTrash = OptionalInt.empty();

    public OptionalInt getRecurse() {
        return recurse;
    }

    public void setRecurse(OptionalInt recurse) {
        this.recurse = recurse;
    }

    public OptionalInt getNoTrash() {
        return noTrash;
    }

    public void setNoTrash(OptionalInt noTrash) {
        this.noTrash = noTrash;
    }
}
