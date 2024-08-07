package org.irods.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectVerifyChecksumParams {
    private Optional<String> resource = Optional.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private OptionalInt computeChecksums = OptionalInt.empty();
    private OptionalInt admin = OptionalInt.empty();

    public Optional<String> getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = Optional.of(resource);
    }

    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    public void setReplicaNum(int replicaNum) {
        this.replicaNum = OptionalInt.of(replicaNum);
    }

    public OptionalInt getComputeChecksums() {
        return computeChecksums;
    }

    public void setComputeChecksums(int computeChecksums) {
        this.computeChecksums = OptionalInt.of(computeChecksums);
    }

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = OptionalInt.of(admin);
    }
}
