package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectTouchParams {
    OptionalInt noCreate = OptionalInt.empty();
    OptionalInt replicaNum = OptionalInt.empty();
    Optional<String> leafResource = Optional.empty();
    OptionalInt secondsSinceEpoch = OptionalInt.empty();
    Optional<String> reference = Optional.empty();

    public OptionalInt getNoCreate() {
        return noCreate;
    }

    public void setNoCreate(int noCreate) {
        this.noCreate = OptionalInt.of(noCreate);
    }

    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    public void setReplicaNum(int replicaNum) {
        this.replicaNum = OptionalInt.of(replicaNum);
    }

    public Optional<String> getLeafResource() {
        return leafResource;
    }

    public void setLeafResource(String leafResource) {
        this.leafResource = Optional.of(leafResource);
    }

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
