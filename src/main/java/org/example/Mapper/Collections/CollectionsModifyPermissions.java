package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Mapper.Mapped;

import java.util.Objects;

public class CollectionsModifyPermissions extends Mapped {
    public static class Operation {
        @JsonProperty("entity_name")
        private String entity_name;
        @JsonProperty("acl")
        private String acl;

        public String getEntity_name() {
            return entity_name;
        }

        public void setEntity_name(String entity_name) {
            this.entity_name = entity_name;
        }

        public String getAcl() {
            return acl;
        }

        public void setAcl(String acl) {
            this.acl = acl;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!this.getClass().equals(obj.getClass())) {
                return false;
            }

            CollectionsModifyPermissions.Operation other = (CollectionsModifyPermissions.Operation) obj;
            return Objects.equals(this.entity_name, other.entity_name) &&
                    Objects.equals(this.acl, other.acl);
        }
    }
}