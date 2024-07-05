package org.example.Mapper.Collections;

import org.example.Mapper.Mapped;
public class CollectionsModifyMetadata {
    public static class Operation {
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