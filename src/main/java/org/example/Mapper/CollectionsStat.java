package org.example.Mapper;

import java.security.Permission;
import java.util.List;

public class CollectionsStat {
    private IrodsResponse irods_response; // nested JSON
    private String type;
    private boolean inheritance_enabled;
    private List<Permissions> permissions; // nested JSON
    private boolean registered;
    private int modified_at;

    public IrodsResponse getIrods_response() {
        return irods_response;
    }

    public String getType() {
        return type;
    }

    public boolean isInheritance_enabled() {
        return inheritance_enabled;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public boolean isRegistered() {
        return registered;
    }

    public int getModified_at() {
        return modified_at;
    }

    @Override
    public String toString() {
        return "irods_response:\n" + irods_response +
                "\ntype: '" + type + '\'' +
                "\ninheritance_enabled: " + inheritance_enabled +
                "\npermissions: " + permissions +
                "\nregistered: " + registered +
                "\nmodified_at: " + modified_at;
    }
}