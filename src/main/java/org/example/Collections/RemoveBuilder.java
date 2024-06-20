package org.example.Collections;

import org.example.User;
import java.io.IOException;

// allows for user to chain the optional parameters if needed
public class RemoveBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private boolean recurse = false;
    private boolean noTrash = false;

    public RemoveBuilder(CollectionOperations operations, User user, String lpath) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
    }
    public RemoveBuilder recurse() {
        this.recurse = true;
        return this;
    }

    public RemoveBuilder noTrash() {
        this.noTrash = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.remove(user, lpath, recurse, noTrash);
    }

}
