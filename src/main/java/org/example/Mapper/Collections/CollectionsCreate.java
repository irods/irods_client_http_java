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

        //check if it is an instance because a runtime error occurs when casting if it's not
        if (!this.getClass().equals(obj.getClass())) {
            return false; //when obj is not an instance of Ride
        }

        //casting because obj is of type Object and does not have the following data fields
        CollectionsCreate other = (CollectionsCreate) obj;
        return this.created == other.created &&
                Objects.equals(this.getIrods_response(), other.getIrods_response());
    }

}
