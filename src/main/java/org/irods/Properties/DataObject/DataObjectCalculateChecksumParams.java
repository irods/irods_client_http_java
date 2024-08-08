package org.irods.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectCalculateChecksumParams {
    private Optional<String> resource = Optional.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private OptionalInt catalogOnly = OptionalInt.empty();
    private OptionalInt force = OptionalInt.empty();
    private OptionalInt all = OptionalInt.empty();
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

    public OptionalInt getCatalogOnly() {

        return catalogOnly;
    }

    public void setCatalogOnly(int catalogOnly) {

        this.catalogOnly = OptionalInt.of(catalogOnly);
    }

    public OptionalInt getForce() {
        return force;
    }

    public void setForce(int force) {

        this.force = OptionalInt.of(force);
    }

    public OptionalInt getAll() {
        return all;
    }

    public void setAll(int all) {

        this.all = OptionalInt.of(all);
    }

    public OptionalInt getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {

        this.admin = OptionalInt.of(admin);
    }
}
