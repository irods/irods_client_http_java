package org.example.Collections;

import org.example.User;

import java.io.IOException;

public class TouchBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private int mtime = 0;
    private String reference = null;

    public TouchBuilder(CollectionOperations operations, User user, String lpath) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
    }

    public TouchBuilder mtime(int mtime) {
        this.mtime = mtime;
        return this;
    }

    public TouchBuilder reference(String reference) {
        this.reference = reference;
        return this;
    }


    public void execute() throws IOException, InterruptedException {
        operations.touch(user, lpath, mtime, reference);
    }
}
