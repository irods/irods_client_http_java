package org.example.Collections;

import org.example.IrodsException;
import org.example.User;

import java.io.IOException;

public class StatBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private boolean ticket = false;

    public StatBuilder(CollectionOperations operations, User user, String lpath) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
    }

    public StatBuilder ticket() {
        ticket = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException, IrodsException {
        operations.stat(user, lpath, ticket);
    }
}
