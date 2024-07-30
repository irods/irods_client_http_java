package org.example.Properties.Collection;
import java.util.OptionalInt;

public class CollectionsRemoveParams {
    private OptionalInt recurse = OptionalInt.empty();
    private OptionalInt noTrash = OptionalInt.empty();

    public OptionalInt getRecurse() {
        return recurse;
    }

    public void setRecurse(int recurse) {

        this.recurse = OptionalInt.of(recurse);
    }

    public OptionalInt getNoTrash() {
        return noTrash;
    }

    public void setNoTrash(int noTrash) {

        this.noTrash = OptionalInt.of(noTrash);
    }
}
