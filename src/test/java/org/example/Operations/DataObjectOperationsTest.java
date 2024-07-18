package org.example.Operations;

import junit.framework.TestCase;
import org.example.Mapper.Collections.CollectionsList;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.example.Wrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
        rods.collections().create(token, "/tempZone/home/rods/test", false);
        updateList();
    }

    @Test
    public void touch_functionality() {

    }

    private void updateList() {
        Response response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        entries = mapped.getEntries();
    }

    private void resetCollections() {
        updateList();

        // Ensure the list is empty
        if (entries != null && !entries.isEmpty()) {
            for (String lpath : entries) {
                rods.collections().remove(token, lpath, true, false);
                rods.dataObject().remove(token, lpath, false, false, true);
            }
        }
        updateList();
        assertEquals(null, entries);
    }

}