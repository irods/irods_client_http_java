package org.irods.properties.data_object;

import org.irods.operations.DataObjectOperations;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code modifyReplica()} in {@link DataObjectOperations}, which
 * modifies properties of a single replica. Create an instance of this class and set the optional fields as desired,
 * not all fields have to be set.
 * <p> {@code resource-hierarchy} and {@code replica-number} are mutually exclusive parameters. </p>
 * <p>
 *     All parameters having a prefix of {@code new-} represent modifiable properties of the target replica. At least one
 *     modifiable property is required for the operation to succeed. This operation allows multiple properties to be
 *     modified in a single call.
 * </p>
 */
public class DataObjectModifyReplicaParams {
    private Optional<String> resourceHierarchy = Optional.empty();
    private OptionalInt replicaNum = OptionalInt.empty();
    private Optional<String> newDataChecksum =  Optional.empty();
    private Optional<String> newDataComments =  Optional.empty();
    private OptionalInt newDataCreateTime = OptionalInt.empty();
    private OptionalInt newDataExpiry = OptionalInt.empty();
    private Optional<String> newDataMode =  Optional.empty();
    private Optional<String> newDataModifyTime =  Optional.empty();
    private OptionalInt newDataReplicaNum = OptionalInt.empty();
    private OptionalInt newDataRepliaStatus = OptionalInt.empty();
    private OptionalInt newDataResourceId = OptionalInt.empty();
    private OptionalInt newDataSize = OptionalInt.empty();
    private Optional<String> newDataStatus =  Optional.empty();
    private Optional<String> newDataTypeName =  Optional.empty();
    private Optional<String> newDataVersion =  Optional.empty();
    private Optional<String> newDataPath =  Optional.empty();

    public Optional<String> getResourceHierarchy() {
        return resourceHierarchy;
    }

    public void setResourceHierarchy(String resourceHierarchy) {

        this.resourceHierarchy = Optional.of(resourceHierarchy);
    }

    public OptionalInt getReplicaNum() {
        return replicaNum;
    }

    public void setReplicaNum(int replicaNum) {
        this.replicaNum = OptionalInt.of(replicaNum);
    }

    public Optional<String> getNewDataChecksum() {
        return newDataChecksum;
    }

    public void setNewDataChecksum(String newDataChecksum) {

        this.newDataChecksum = Optional.of(newDataChecksum);
    }

    public Optional<String> getNewDataComments() {
        return newDataComments;
    }

    public void setNewDataComments(String newDataComments) {

        this.newDataComments = Optional.of(newDataComments);
    }

    public OptionalInt getNewDataCreateTime() {

        return newDataCreateTime;
    }

    public void setNewDataCreateTime(int newDataCreateTime) {

        this.newDataCreateTime = OptionalInt.of(newDataCreateTime);
    }

    public OptionalInt getNewDataExpiry() {
        return newDataExpiry;
    }

    public void setNewDataExpiry(int newDataExpiry) {

        this.newDataExpiry = OptionalInt.of(newDataExpiry);
    }

    public Optional<String> getNewDataMode() {
        return newDataMode;
    }

    public void setNewDataMode(String newDataMode) {

        this.newDataMode = Optional.of(newDataMode);
    }

    public Optional<String> getNewDataModifyTime() {
        return newDataModifyTime;
    }

    public void setNewDataModifyTime(String newDataModifyTime) {

        this.newDataModifyTime = Optional.of(newDataModifyTime);
    }

    public Optional<String> getNewDataPath() {
        return newDataPath;
    }

    public void setNewDataPath(String newDataPath) {

        this.newDataPath = Optional.of(newDataPath);
    }

    public OptionalInt getNewDataReplicaNum() {
        return newDataReplicaNum;
    }

    public void setNewDataReplicaNum(int newDataReplicaNum) {

        this.newDataReplicaNum = OptionalInt.of(newDataReplicaNum);
    }

    public OptionalInt getNewDataRepliaStatus() {
        return newDataRepliaStatus;
    }

    public void setNewDataRepliaStatus(int newDataRepliaStatus) {
        this.newDataRepliaStatus = OptionalInt.of(newDataRepliaStatus);
    }

    public OptionalInt getNewDataResourceId() {
        return newDataResourceId;
    }

    public void setNewDataResourceId(int newDataResourceId) {

        this.newDataResourceId = OptionalInt.of(newDataResourceId);
    }

    public OptionalInt getNewDataSize() {
        return newDataSize;
    }

    public void setNewDataSize(int newDataSize) {

        this.newDataSize = OptionalInt.of(newDataSize);
    }

    public Optional<String> getNewDataStatus() {
        return newDataStatus;
    }

    public void setNewDataStatus(String newDataStatus) {

        this.newDataStatus = Optional.of(newDataStatus);
    }

    public Optional<String> getNewDataTypeName() {
        return newDataTypeName;
    }

    public void setNewDataTypeName(String newDataTypeName) {

        this.newDataTypeName = Optional.of(newDataTypeName);
    }

    public Optional<String> getNewDataVersion() {
        return newDataVersion;
    }

    public void setNewDataVersion(String newDataVersion) {
        this.newDataVersion = Optional.of(newDataVersion);
    }
}
