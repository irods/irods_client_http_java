package org.example.Mapper.Collections;

//import org.example.Mapper.IrodsResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Mapped;

import java.security.Permission;
import java.util.List;
import java.util.Objects;

public class CollectionsStat extends Mapped {
    private String type;
    private boolean inheritance_enabled;
    private List<Permissions> permissions; // nested JSON
    private boolean registered;
    private int modified_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInheritance_enabled() {
        return inheritance_enabled;
    }

    public void setInheritance_enabled(boolean inheritance_enabled) {
        this.inheritance_enabled = inheritance_enabled;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public int getModified_at() {
        return modified_at;
    }

    public void setModified_at(int modified_at) {
        this.modified_at = modified_at;
    }

    public static class Permissions {
        private String name;
        private String zone;
        private String type;
        private String perm;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPerm() {
            return perm;
        }

        public void setPerm(String perm) {
            this.perm = perm;
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

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            //check if it is an instance because a runtime error occurs when casting if it's not
            if (!this.getClass().equals(obj.getClass())) {
                return false; //when obj is not an instance of Ride
            }

            //casting because obj is of type Object and does not have the following data fields
            CollectionsStat.Permissions other = (CollectionsStat.Permissions) obj;
            return Objects.equals(this.name, other.name) &&
                    Objects.equals(this.zone, other.zone) &&
                    Objects.equals(this.type, other.type) &&
                    Objects.equals(this.perm, other.perm);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        //check if it is an instance because a runtime error occurs when casting if it's not
        if (!this.getClass().equals(obj.getClass())) {
            return false; //when obj is not an instance of Ride
        }

        //casting because obj is of type Object and does not have the following data fields
        CollectionsStat other = (CollectionsStat) obj;
        return Objects.equals(this.type, other.type) &&
                this.inheritance_enabled == other.inheritance_enabled &&
                Objects.equals(this.permissions, other.permissions) &&
                this.registered == other.registered &&
                this.modified_at == other.modified_at;
    }
}