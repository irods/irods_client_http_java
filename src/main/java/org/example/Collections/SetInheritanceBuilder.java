package org.example.Collections;

import org.example.User;

import java.io.IOException;
import java.util.Set;

public class SetInheritanceBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private final boolean enable;
    private boolean admin = false;

    public SetInheritanceBuilder (CollectionOperations operations, User user, String lpath, boolean enable) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
        this.enable = enable;
    }

    public SetInheritanceBuilder admin() {
        admin = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.set_inheritance(user, lpath, enable, admin);
    }
}
