package org.example.Mapper.Collections;


import org.example.Mapper.Mapped;

import java.util.List;
import java.util.Objects;

public class CollectionsList extends Mapped {
    private List<String> entries;

    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        CollectionsList other = (CollectionsList) obj;
        return Objects.equals(this.entries, other.entries) &&
                Objects.equals(this.getIrodsResponse(), other.getIrodsResponse());
    }
}
