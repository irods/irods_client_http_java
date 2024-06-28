package org.example.Mapper.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Mapped;
//import org.example.Mapper.IrodsResponse;

/**
 * For JSON responses that ONLY include a nested irods_response and no other items
 * Includes:
 *  - collections.remove()
 *  - collections.set_permission()
 *  - collections.set_inheritance()
 */
//TODO: see it's possible to transfer the other mapped classes into this one
public class NestedIrodsResponse extends Mapped {
//    private IrodsResponse irods_response; // nested JSON
//
//    public IrodsResponse getIrods_response() {
//        return irods_response;
//    }
//

//    @Override
//    public String toString() {
//        return "{\n\tirods_response: " +
//                irods_response +
//                "\n}";
//    }

//    @Override
//    public String toString() {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            return "{ " +
//                    "\"error\": \"Unable to serialize to JSON\" " +
//                    "}";
//        }
//    }
}
