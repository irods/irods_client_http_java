package org.example.Properties.Collection;

import java.util.Optional;
import java.util.OptionalInt;

public class TouchProperties {
    OptionalInt secondsSinceEpoch = OptionalInt.empty();
    Optional<String> reference = Optional.empty();

    public OptionalInt getSecondsSinceEpoch() {
        return secondsSinceEpoch;
    }

    public void setSecondsSinceEpoch(OptionalInt secondsSinceEpoch) {
        this.secondsSinceEpoch = secondsSinceEpoch;
    }

    public Optional<String> getReference() {
        return reference;
    }

    public void setReference(Optional<String> reference) {
        this.reference = reference;
    }
}
