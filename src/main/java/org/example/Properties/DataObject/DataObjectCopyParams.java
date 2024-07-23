package org.example.Properties.DataObject;

import java.util.Optional;
import java.util.OptionalInt;

public class DataObjectCopyParams {
    Optional<String> srcResource = Optional.empty();
    Optional<String> dstResource = Optional.empty();
    OptionalInt overwrite = OptionalInt.empty();

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
