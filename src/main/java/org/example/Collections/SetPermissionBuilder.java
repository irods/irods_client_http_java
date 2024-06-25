package org.example.Collections;

import org.example.User;

import java.io.IOException;

public class SetPermissionBuilder {
    private final CollectionOperations operations;
    private final User user;
    private final String lpath;
    private final String entityName;
    private final Permission permission;
    private boolean admin = false;

    public SetPermissionBuilder(CollectionOperations operations, User user, String lpath, String entityName,
                                Permission permission) {
        this.operations = operations;
        this.user = user;
        this.lpath = lpath;
        this.entityName = entityName;
        this.permission = permission;
    }

    public SetPermissionBuilder admin() {
        admin = true;
        return this;
    }

    public void execute() throws IOException, InterruptedException {
        operations.set_permission(user, lpath, entityName, permission, admin);
    }
}

