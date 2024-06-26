package org.example.Collections;

import org.example.Mapper.Collections.ModifyPermissionsOperations;
import org.example.User;

import java.io.IOException;
import java.util.List;

public class ModifyPermissionsBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private final List<ModifyPermissionsOperations> jsonParam;
    private boolean admin = false;

    public ModifyPermissionsBuilder(CollectionOperations operations, User user,
                                    String lpath, List<ModifyPermissionsOperations> jsonParam) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
        this.jsonParam = jsonParam;
    }

    public ModifyPermissionsBuilder admin() {
        admin = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.modify_permissions(user, lpath, jsonParam, admin);
    }
}
