package org.example.Mapper;

public class Permissions {
    private String name;
    private String zone;
    private String type;
    private String perm;

    public String getName() {
        return name;
    }

    public String getZone() {
        return zone;
    }

    public String getType() {
        return type;
    }

    public String getPerm() {
        return perm;
    }

    @Override
    public String toString() {
        return " name: '" + name + '\'' +
                ", zone='" + zone + '\'' +
                ", type='" + type + '\'' +
                ", perm='" + perm + '\'' +
                '}';
    }
}
