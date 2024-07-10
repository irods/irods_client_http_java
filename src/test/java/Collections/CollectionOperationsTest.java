package Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.IrodsException;
import org.example.Manager;
import org.example.Mapper.Collections.CollectionsCreate;
import org.example.Mapper.Collections.CollectionsList;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CollectionOperationsTest {
    private Manager rods;
    private String token;
    @Before
    public void setup() throws IrodsException, IOException, InterruptedException {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // create client
        rods = new Manager(baseUrl, "rods", "rods");

        // generate token
        try {
            rods.authenticate();
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }
        token = rods.getAuthToken();
    }

    @Test
    public void listNoEntriesMapped() {
        Response response = null;
        CollectionsList actualMapped = null;
        try {
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

            // need to make sure the list is 0
            if (actualMapped.getEntries().size() != 0) {
                for(String lpath : actualMapped.getEntries()) {
                    response = rods.collections().remove(token, lpath, true, false);
                }
            }

            // update the response & mapped values now
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }

        // json string: {"entries":null,"irods_response":{"status_code":0}}

        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsList expectedMapped = new CollectionsList();
        expectedMapped.setEntries(null);
        expectedMapped.setIrods_response(expectedIrodsResponse);

        System.out.println(actualMapped);
        System.out.println(expectedMapped);
        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void addCollectionMapped() {
        Response response = null;
        CollectionsCreate mapped = null;
        try {
            response =  rods.collections().create(token, "/tempZone/home/rods/test", false);
            mapped = JsonUtil.fromJson(response.getBody(), CollectionsCreate.class);
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }

        // json string: {"created":true,"irods_response":{"status_code":0}}

//        System.out.println(response.getBody());
//        System.out.println(mapped);

        assertEquals("creaeted should be true", true, mapped.isCreated());
        assertNotNull("irods_response should not be null", mapped.getIrods_response());
        assertEquals("irods_response status_code should be 0", 0, mapped.getIrods_response().getStatus_code());

    }


}