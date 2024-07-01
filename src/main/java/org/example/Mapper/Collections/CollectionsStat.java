package org.example.Mapper.Collections;

//import org.example.Mapper.IrodsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Mapped;

import java.security.Permission;
import java.util.List;

public class CollectionsStat extends Mapped {
    private String type;
    private boolean inheritance_enabled;
    private List<Permissions> permissions; // nested JSON
    private boolean registered;
    private int modified_at;

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
    public static class Permissions {
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
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return "{ \"error\": \"Unable to serialize to JSON\" }";
            }
        }
    }
}
