package org.example.Collections;

import org.example.Mapper.Collections.ModifyMetadataOperations;
import org.example.User;

import java.io.IOException;
import java.util.List;

public class ModifyMetadataBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private final List<ModifyMetadataOperations> jsonParam;
    private boolean admin = false;

    public ModifyMetadataBuilder(CollectionOperations operations, User user,
                                 String lpath, List<ModifyMetadataOperations> jsonParam) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
        this.jsonParam = jsonParam;
    }

    public ModifyMetadataBuilder admin() {
        admin = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.modify_metadata(user, lpath, jsonParam, admin);
    }
}
