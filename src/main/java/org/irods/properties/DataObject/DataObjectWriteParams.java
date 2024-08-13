package org.irods.properties.DataObject;

import org.irods.operations.DataObjectOperations;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code write()} in {@link DataObjectOperations}, which
 * writes bytes to a data object. Create an instance of this class and set the optional fields as desired, not all
 * fields have to be set.
 */
public class DataObjectWriteParams {
    private Optional<String> resource = Optional.empty();
    private OptionalInt offset = OptionalInt.empty();
    private OptionalInt truncate = OptionalInt.empty();
    private OptionalInt append = OptionalInt.empty();
    private Optional<String> parallelWriteHandle = Optional.empty();
    private OptionalInt streamIndex = OptionalInt.empty();

    /**
     * Gets the root resource to write to.
     *
     * @return an {@link Optional} containing the {@code resource} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getResource() {
        return resource;
    }

    /**
     * Sets the root resource to write to.
     *
     * @param resource The root resource to write to. Only the String value needs to be provided; it will be wrapped
     *                 in an {@link Optional}.
     */
    public void setResource(String resource) {
        this.resource = Optional.of(resource);
    }

    /**
     * Gets the number of bytes to skip.
     *
     * @return an {@link OptionalInt} containing the {@code offset} value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getOffset() {
        return offset;
    }

    /**
     * Sets the number of bytes to skip.
     *
     * @param offset Number of bytes to skip. Defaults to 0. Only the int value needs to be provided; it will be
     *               wrapped in an {@link OptionalInt}.
     */
    public void setOffset(int offset) {
        this.offset = OptionalInt.of(offset);
    }

    /**
     * Gets the {@code truncate} flag, which decides whether to truncate the data object before writing.
     *
     * @return an {@link OptionalInt} containing the {@code truncate} flag value if set, otherwise an empty {@code OptionalInt}.
     */
    public OptionalInt getTruncate() {
        return truncate;
    }

    /**
     * Sets the {@code truncate} flag, which decides whether to truncate the data object before writing.
     *
     * @param truncate 0 or 1. Defaults to 1. Truncates the data object before writing. Only the int value needs to be
     *                 provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setTruncate(int truncate) {
        this.truncate = OptionalInt.of(truncate);
    }

    /**
     * Gets the {@code append} flag, which decides whether to append the bytes to the data object.
     *
     * @return an {@link OptionalInt} containing the {@code append} flag value if set, otherwise an empty {@code OptionalInt}.
     **/
    public OptionalInt getAppend() {
        return append;
    }

    /**
     * Sets the {@code append} flag, which decides whether to append the bytes to the data object.
     *
     * @param append 0 or 1. Defaults to 0. Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setAppend(int append) {
        this.append = OptionalInt.of(append);
    }

    /**
     * Gets the handle to use when writing in parallel.
     *
     * @return an {@link Optional} containing the {@code parallelWriteHandle} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getParallelWriteHandle() {
        return parallelWriteHandle;
    }

    /**
     * Sets the handle to use when writing in parallel. Only applies when writing to a replica in parallel.
     *
     * @param parallelWriteHandle The handle to use when writing in parallel. Only the String value needs to be
     *                            provided; it will be wrapped in an {@link Optional}.
     */
    public void setParallelWriteHandle(String parallelWriteHandle) {
        this.parallelWriteHandle = Optional.of(parallelWriteHandle);
    }

    /**
     * Gets the stream to use when writing in parallel.
     *
     * @return an {@link OptionalInt} containing the {@code streamIndex} value if set, otherwise an empty {@code OptionalInt}.
     */

    public OptionalInt getStreamIndex() {
        return streamIndex;
    }

    /**
     * Sets the stream to use when writing in parallel. Only applies when writing to a replica in parallel.
     *
     * @param streamIndex the stream to use when writing in parallel. Only the int value needs to be provided; it will
     *                    be wrapped in an {@link OptionalInt}.
     */
    public void setStreamIndex(int streamIndex) {
        this.streamIndex = OptionalInt.of(streamIndex);
    }
}
