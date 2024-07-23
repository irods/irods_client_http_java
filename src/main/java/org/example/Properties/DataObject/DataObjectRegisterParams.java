package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectRegisterParams {
    OptionalInt asAdditionalReplica = OptionalInt.empty();
    OptionalInt dataSize = OptionalInt.empty();
    Optional<String> checksum = Optional.empty();

    public OptionalInt getAsAdditionalReplica() {
        return asAdditionalReplica;
    }

    public void setAsAdditionalReplica(int asAdditionalReplica) {
        this.asAdditionalReplica = OptionalInt.of(asAdditionalReplica);
    }

    public OptionalInt getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {

        this.dataSize = OptionalInt.of(dataSize);
    }

    public Optional<String> getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {

        this.checksum = Optional.of(checksum);
    }
}
