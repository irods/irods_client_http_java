package org.irods.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectCopyParams {
    private Optional<String> srcResource = Optional.empty();
    private Optional<String> dstResource = Optional.empty();
    private OptionalInt overwrite = OptionalInt.empty();

    public Optional<String> getSrcResource() {
        return srcResource;
    }

    public void setSrcResource(String srcResource) {

        this.srcResource = Optional.of(srcResource);
    }

    public Optional<String> getDstResource() {
        return dstResource;
    }

    public void setDstResource(String dstResource) {

        this.dstResource = Optional.of(dstResource);
    }

    public OptionalInt getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(int overwrite) {

        this.overwrite = OptionalInt.of(overwrite);
    }
}
