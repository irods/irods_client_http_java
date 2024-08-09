package org.irods.properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class CollectionsTouchParams {
    private OptionalInt secondsSinceEpoch = OptionalInt.empty();
    private Optional<String> reference = Optional.empty();

    public OptionalInt getSecondsSinceEpoch() {
        return secondsSinceEpoch;
    }

    public void setSecondsSinceEpoch(int secondsSinceEpoch) {

        this.secondsSinceEpoch = OptionalInt.of(secondsSinceEpoch);
    }

    public Optional<String> getReference() {
        return reference;
    }

    public void setReference(String reference) {

        this.reference = Optional.of(reference);
    }
}
