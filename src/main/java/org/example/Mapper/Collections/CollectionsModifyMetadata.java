package org.example.Mapper.Collections;

import org.example.Mapper.Mapped;

import java.util.Objects;
public class CollectionsModifyMetadata extends Mapped {
    public static class Operation {
        private String operation;
        private String attribute;
        private String value;
        private String units;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (!this.getClass().equals(obj.getClass())) {
                return false;
            }

            CollectionsModifyMetadata.Operation other = (CollectionsModifyMetadata.Operation) obj;
            return Objects.equals(this.operation, other.operation) &&
                    Objects.equals(this.attribute, other.attribute) &&
                    Objects.equals(this.value, other.value) &&
                    Objects.equals(this.units, other.units);
        }
    }
}