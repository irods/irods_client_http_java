package Collections;

import org.example.Util.IrodsException;
import org.example.Manager;
import org.example.Mapper.Collections.CollectionsCreate;
import org.example.Mapper.Collections.CollectionsList;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionOperationsTest {
    private Manager rods;
    private String token;
    private List<String> entries;
    @Before
    /**
     * tests assume that .list() is working correctly :(
     */
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // create client
        rods = new Manager(baseUrl, "rods", "rods");

        try {
            // generate token
            rods.authenticate();
            token = rods.getAuthToken();

            resetCollections();
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e);
        }
    }

    private void resetCollections() throws IOException, InterruptedException, IrodsException {
        Response response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        entries = mapped.getEntries();

        // Ensure the list is empty
        if (entries != null && !entries.isEmpty()) {
            for (String lpath : mapped.getEntries()) {
                rods.collections().remove(token, lpath, true, false);
            }
        }
    }

    @Test
    public void listNoEntriesMapped() throws IOException, IrodsException, InterruptedException {
        updateList();
        System.out.println(entries);

        Response response;
        CollectionsList actualMapped = null;
        try {
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

            // need to make sure the list is 0
            if (actualMapped.getEntries() != null && !actualMapped.getEntries().isEmpty()) {
                for(String lpath : actualMapped.getEntries()) {
                    rods.collections().remove(token, lpath, true, false);
                }
            }

            // update the response & mapped values now
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e);
        }

//        response = rods.collections().list(token, "/tempZone/home/rods", false, null);
//        actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
//
//            // need to make sure the list is 0
//            if (!actualMapped.getEntries().isEmpty()) {
//                for(String lpath : actualMapped.getEntries()) {
//                    rods.collections().remove(token, lpath, true, false);
//                }
//            }
//
//            // update the response & mapped values now
//            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
//            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);


        // json string: {"entries":null,"irods_response":{"status_code":0}}

        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsList expectedMapped = new CollectionsList();
        expectedMapped.setEntries(null);
        expectedMapped.setIrods_response(expectedIrodsResponse);

        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void createMapped() throws IrodsException, IOException, InterruptedException {
        updateList();
        System.out.println(entries);

        Response response;
        CollectionsCreate actualMapped = null;
        try {
            response =  rods.collections().create(token, "/tempZone/home/rods/test", false);
            actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsCreate.class);
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }

        // json string: {"created":true,"irods_response":{"status_code":0}}

//        System.out.println(mapped);

        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsCreate expectedMapped = new CollectionsCreate();
        expectedMapped.setCreated(true);
        expectedMapped.setIrods_response(expectedIrodsResponse);

//        System.out.println(expectedMapped);
//        System.out.println(actualMapped);
        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void createFunctionality() throws IrodsException, IOException, InterruptedException {
        updateList();
        System.out.println(entries);

        Response response;
        CollectionsList listMapped = null;
        try {
            rods.collections().create(token, "/tempZone/home/rods/test", false);
            rods.collections().create(token, "/tempZone/home/rods/test2", false);
            rods.collections().create(token, "/tempZone/home/rods/test3", false);
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            listMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e);
        }

        List<String> expectedList = new ArrayList<>();
        expectedList.add("/tempZone/home/rods/test");
        expectedList.add("/tempZone/home/rods/test2");
        expectedList.add("/tempZone/home/rods/test3");

        assertEquals(listMapped.getEntries(), expectedList);
    }

    @Test
    public void removeMapped() throws IrodsException, IOException, InterruptedException {
        updateList();
//        System.out.println(entries);

        Response response;
        IrodsResponse actualMapped = null;
        try {
            response = rods.collections().list(token, "/tempZone/home/rods", false, null);
            CollectionsList listMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
            if (listMapped.getEntries() == null || !listMapped.getEntries().contains("/tempZone/home/rods/test3")) {
                rods.collections().create(token, "/tempZone/home/rods/test3", false);
            }

            response = rods.collections().remove(token, "/tempZone/home/rods/test3", false, false);
            actualMapped = JsonUtil.fromJson(response.getBody(), IrodsResponse.class);

            System.out.println(actualMapped);
            System.out.println(response.getBody());
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }

        // json string: {"irods_response":{"status_code":0}}


        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        IrodsResponse expectedMapped = new IrodsResponse();
        expectedMapped.setIrods_response(expectedIrodsResponse);

//        System.out.println(expectedMapped);
//        System.out.println(actualMapped);
        assertEquals(expectedMapped, actualMapped);
    }

    public void updateList() throws IOException, IrodsException, InterruptedException {
        Response response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        entries = mapped.getEntries();
    }


}