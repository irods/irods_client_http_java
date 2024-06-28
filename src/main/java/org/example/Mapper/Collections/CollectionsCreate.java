package org.example.Mapper.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;

public class CollectionsCreate extends Mapped {
//    private IrodsResponse irods_response;
    private boolean created;

//    public IrodsResponse getIrods_response() {
//        return irods_response;
//    }

    public boolean isCreated() {
        return created;
    }

}
