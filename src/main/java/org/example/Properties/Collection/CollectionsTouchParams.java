package org.example.Properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class CollectionsTouchParams {
    OptionalInt secondsSinceEpoch = OptionalInt.empty();
    Optional<String> reference = Optional.empty();

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
