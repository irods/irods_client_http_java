package org.example.Mapper.Collections;


//import org.example.Mapper.IrodsResponse;

import org.example.Mapper.Mapped;

import java.util.Collections;
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

        //check if it is an instance because a runtime error occurs when casting if it's not
        if (!this.getClass().equals(obj.getClass())) {
            return false; //when obj is not an instance of Ride
        }

        //casting because obj is of type Object and does not have the following data fields
        CollectionsList other = (CollectionsList) obj;
        return Objects.equals(this.entries, other.entries) &&
                Objects.equals(this.getIrods_response(), other.getIrods_response());
    }
}
