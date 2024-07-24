package org.example.Properties.Rule;

import java.util.Optional;
public class RuleExecuteParams {
    Optional<String> repInstance = Optional.empty();

    public Optional<String> getRepInstance() {
        return repInstance;
    }

    public void setRepInstance(String repInstance) {
        this.repInstance = Optional.of(repInstance);
    }
}
