package org.example.Mapper.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Mapper.Mapped;
public class CollectionsModifyPermissions extends Mapped {
    public static class Operation extends IrodsResponse.FailedOperation {
        @JsonProperty("entity_name")
        private String entity_name;
        @JsonProperty("acl")
        private String acl;

        public String getEntity_name() {
            return entity_name;
        }
        public String getAcl() {
            return acl;
        }
    }
}