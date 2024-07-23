package org.example.Properties.Resource;

import java.util.Optional;

public class ResourceAddChildParams {
    Optional<String> context = Optional.empty();

    public Optional<String> getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = Optional.of(context);
    }
}
