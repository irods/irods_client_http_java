package org.example.Properties.Collection;

import java.util.OptionalInt;

public class CollectionsCreateParams {
    OptionalInt intermediates = OptionalInt.empty();

    public OptionalInt getIntermediates() {
        return intermediates;
    }

    public void setIntermediates(int intermediates) {

        this.intermediates = OptionalInt.of(intermediates);
    }
}
