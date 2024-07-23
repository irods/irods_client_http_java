package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectWriteParams {
    Optional<String> resource = Optional.empty();
    OptionalInt offset = OptionalInt.empty();
    OptionalInt truncate = OptionalInt.empty();
    OptionalInt append = OptionalInt.empty();
    Optional<String> parallelWriteHandle = Optional.empty();
    OptionalInt streamIndex = OptionalInt.empty();

    public Optional<String> getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = Optional.of(resource);
    }

    public OptionalInt getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = OptionalInt.of(offset);
    }

    public OptionalInt getTruncate() {
        return truncate;
    }

    public void setTruncate(int truncate) {
        this.truncate = OptionalInt.of(truncate);
    }

    public OptionalInt getAppend() {
        return append;
    }

    public void setAppend(int append) {
        this.append = OptionalInt.of(append);
    }

    public Optional<String> getParallelWriteHandle() {
        return parallelWriteHandle;
    }

    public void setParallelWriteHandle(String parallelWriteHandle) {
        this.parallelWriteHandle = Optional.of(parallelWriteHandle);
    }

    public OptionalInt getStreamIndex() {
        return streamIndex;
    }

    public void setStreamIndex(int streamIndex) {
        this.streamIndex = OptionalInt.of(streamIndex);
    }
}
