package org.example.Collections;

import org.example.User;

import java.io.IOException;

public class CreateBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private boolean intermediates = false;

    public CreateBuilder(CollectionOperations operations, User user, String lpath) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
    }

    public CreateBuilder intermediates() {
        intermediates = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.create(user, lpath, intermediates);
    }

}
