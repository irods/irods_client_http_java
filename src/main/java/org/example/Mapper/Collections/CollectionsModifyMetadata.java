package org.example.Mapper.Collections;

import org.example.Mapper.Mapped;
public class CollectionsModifyMetadata extends Mapped {
    public static class Operation extends IrodsResponse.FailedOperation {
        private String operation;
        private String attribute;
        private String value;
        private String units;

        public String getOperation() {
            return operation;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getValue() {
            return value;
        }

        public String getUnits() {
            return units;
        }
    }
}