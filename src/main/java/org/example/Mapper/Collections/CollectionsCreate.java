package org.example.Mapper.Collections;

import org.example.Mapper.Mapped;

import java.util.Objects;

public class CollectionsCreate extends Mapped {
    private boolean created;
    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }

        CollectionsCreate other = (CollectionsCreate) obj;
        return this.created == other.created &&
                Objects.equals(this.getIrodsResponse(), other.getIrodsResponse());
    }
}