package org.example.Operations;

import org.example.Mapper.Collections.CollectionsList;
import org.example.Mapper.DataObjects.DataObjectCalculateSum;
import org.example.Mapper.DataObjects.DataObjectRemove;
import org.example.Mapper.DataObjects.DataObjectTouch;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.example.Wrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DataObjectOperationsTest {
    private Wrapper rods;
    private String token;
    private List<String> entries;
    private Response response;
    @Before
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // create client
        rods = new Wrapper(baseUrl, "rods", "rods");
        rods.authenticate();
        token = rods.getAuthToken();

        // create alice user
        rods.userGroupOperations().create_user(token, "alice", "tempZone", "rodsuser");

        resetCollections();
        rods.dataObject().touch(token, "/tempZone/home/rods/dataTest", false, -1,
                null, -1, null);
        updateList();
    }

    @Test
    public void touch_functionality() {
        response = rods.dataObject().touch(token, "/tempZone/home/rods/dataTest2", false, -1,
                null, -1, null);
        DataObjectTouch mapped = JsonUtil.fromJson(response.getBody(), DataObjectTouch.class);
        updateList();

        assertTrue(entries.contains("/tempZone/home/rods/dataTest2"));
        assertEquals(0, mapped.getIrodsResponse().getStatus_code());
    }

    @Test
    public void remove_functionality() {
        response = rods.dataObject().remove(token, "/tempZone/home/rods/dataTest", false,
                false, true);
        DataObjectRemove mapped = JsonUtil.fromJson(response.getBody(), DataObjectRemove.class);
        updateList();

        assertNull(entries);
        assertEquals(0, mapped.getIrodsResponse().getStatus_code());
    }

    @Test
    public void calculate_sum_functionality() {
        response = rods.dataObject().calculate_checksum(token, "/tempZone/home/rods/dataTest", null,
                -1, false, false, true);
        DataObjectCalculateSum mapped = JsonUtil.fromJson(response.getBody(), DataObjectCalculateSum.class);
        System.out.println(mapped);

        assertEquals(0, mapped.getIrodsResponse().getStatus_code());
    }

    @Test
    public void verify_checksum() {
        rods.dataObject().touch(token, "/tempZone/home/rods/dataTest.txt", false, -1,
                null, -1, null);
        response = rods.dataObject().verify_checksum(token, "/tempZone/home/rods/dataTest.txt", null,
                -1, true, true);
        System.out.println(response);

    }



    private void updateList() {
        Response response = rods.collections().list(token, "/tempZone/home/rods");
        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        entries = mapped.getEntries();
    }

    private void resetCollections() {
        updateList();

        // Ensure the list is empty
        if (entries != null && !entries.isEmpty()) {
            for (String lpath : entries) {
                rods.collections().remove(token, lpath);
                rods.dataObject().remove(token, lpath, false, false, true);
            }
        }
        updateList();
        assertEquals(null, entries);
    }

}