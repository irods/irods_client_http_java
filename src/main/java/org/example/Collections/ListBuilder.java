package org.example.Collections;

import org.example.User;

import java.io.IOException;
import java.util.List;

public class ListBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private boolean recurse = false;
    private String ticket = null;

    public ListBuilder(CollectionOperations operations, User user, String lpath) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
    }

    public ListBuilder recurse() {
        recurse = true;
        return this;
    }

    public ListBuilder ticket(String ticket) {
        this.ticket = ticket;
        return this;
    }

    public List<String> execute() throws IOException, InterruptedException {
        return operations.list(user, lpath, recurse, ticket);
    }
}
