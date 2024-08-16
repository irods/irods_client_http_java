package org.irods.properties.data_object;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * This class represents the optional parameters for {@code copy()} in {@link DataObjectOperations}, which copies a data
 * object. Create an instance of this class and set the optional fields as desired, not all fields have to be set.
 */
public class DataObjectCopyParams {
    private Optional<String> srcResource = Optional.empty();
    private Optional<String> dstResource = Optional.empty();
    private OptionalInt overwrite = OptionalInt.empty();

    /**
     * Gets the name of the root resource to copy from.
     *
     * @return An {@link Optional} containing the {@code srcResource} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getSrcResource() {
        return srcResource;
    }

    /**
     * Sets the name of the root resource to copy from.
     *
     * @param srcResource The name of the root resource to copy from. Only the String value needs to be provided;
     *                    it will be wrapped in an {@link Optional}.
     */
    public void setSrcResource(String srcResource) {

        this.srcResource = Optional.of(srcResource);
    }

    /**
     * Gets the name of the root resource to copy to.
     *
     * @return An {@link Optional} containing the {@code dstResource} value if set, otherwise an empty {@code Optional}.
     */
    public Optional<String> getDstResource() {
        return dstResource;
    }

    /**
     * Sets the name of the root resource to copy to.
     *
     * @param dstResource The name of the root resource to copy to. Only the String value needs to be provided; it will
     *                    be wrapped in an {@link Optional}.
     */
    public void setDstResource(String dstResource) {
        this.dstResource = Optional.of(dstResource);
    }

    /**
     * Gets the {@code overwrite} flag, which instructs whether the server replaces the existing data object.
     *
     * @return An {@link OptionalInt} containing the {@code overwrite} value being assigned if set, otherwise an
     * empty {@code OptionalInt}.
     */
    public OptionalInt getOverwrite() {
        return overwrite;
    }

    /**
     * Sets the {@code overwrite} flag, which instructs whether the server replaces the existing data object.
     *
     * @param overwrite 0 or 1. Defaults to 0. Optional. Instructs the server to replace the existing data object.
     *                  Only the int value needs to be provided; it will be wrapped in an {@link OptionalInt}.
     */
    public void setOverwrite(int overwrite) {

        this.overwrite = OptionalInt.of(overwrite);
    }
}