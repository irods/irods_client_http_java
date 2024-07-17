package Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Collections.*;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Serialize.ModifyPermissionsOperations;
import org.example.Util.Permission;
import org.example.Wrapper;
import org.example.Mapper.Mapped;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionOperationsTest {
    private Wrapper rods;
    private String token;
    private List<String> entries;
    private Response response;
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
    public void create_functionality() {
        response = rods.collections().create(token, "/tempZone/home/rods/test2", false);
        rods.collections().create(token, "/tempZone/home/rods/test3", false);

        CollectionsCreate mapped = JsonUtil.fromJson(response.getBody(), CollectionsCreate.class);

        updateList();

        List<String> expectedList = new ArrayList<>();
        expectedList.add("/tempZone/home/rods/test"); // made in @Before
        expectedList.add("/tempZone/home/rods/test2");
        expectedList.add("/tempZone/home/rods/test3");

        assertEquals(expectedList, entries);
        assertEquals(0, mapped.getIrods_response().getStatus_code());
    }

    @Test
    public void remove_functionality() {
        rods.collections().create(token, "/tempZone/home/rods/test2", false);
        updateList();

        List<String> expectedList = new ArrayList<>();
        expectedList.add("/tempZone/home/rods/test");
        expectedList.add("/tempZone/home/rods/test2");

        // ensure collections were created successfully
        if (!entries.equals(expectedList)) {
            fail("Something went wrong when attempting to create a collection");
        }

        response = rods.collections().remove(token, "/tempZone/home/rods/test2", false, false);
        CollectionsRemove mapped = JsonUtil.fromJson(response.getBody(), CollectionsRemove.class);

        updateList();
        expectedList.remove("/tempZone/home/rods/test2");

        assertEquals(expectedList, entries);
        assertEquals(0, mapped.getIrods_response().getStatus_code());
    }

    @Test
    public void stat_functionality() {
        response = rods.collections().stat(token,"/tempZone/home/rods/test", null);
        CollectionsStat mapped = JsonUtil.fromJson(response.getBody(), CollectionsStat.class);

        assertEquals(0, mapped.getIrods_response().getStatus_code());
    }

    @Test
    public void list_functionality() {
        response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        List<String> expectedList = new ArrayList<>();
        expectedList.add("/tempZone/home/rods/test");

        assertEquals(0, mapped.getIrods_response().getStatus_code());
        assertEquals(expectedList, mapped.getEntries());
    }

    @Test
    public void set_permission_functionality() {
        response = rods.collections().set_permission(token, "/tempZone/home/rods/test", "alice",
                Permission.READ, true);
        CollectionsSetPermission mapped = JsonUtil.fromJson(response.getBody(), CollectionsSetPermission.class);

        Response responseStat = rods.collections().stat(token,"/tempZone/home/rods/test", null);
        CollectionsStat mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);

        // expected permission
        CollectionsStat.Permissions expectedPermission = new CollectionsStat.Permissions();
        expectedPermission.setZone("tempZone");
        expectedPermission.setType("rodsuser");
        expectedPermission.setPerm("read_object");
        expectedPermission.setName("alice");


        assertTrue("Permission should contain: \n" + expectedPermission,
                mappedStat.getPermissions().contains(expectedPermission));
        assertEquals(0, mapped.getIrods_response().getStatus_code());

        // now try and change the permission type to see if it still works
        response = rods.collections().set_permission(token, "/tempZone/home/rods/test", "alice",
                Permission.WRITE, true);

        responseStat = rods.collections().stat(token,"/tempZone/home/rods/test", null);
        mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);
        expectedPermission.setPerm("modify_object");
        assertTrue("Permission should contain: \n" + expectedPermission,
                mappedStat.getPermissions().contains(expectedPermission));
    }

    @Test
    public void set_inheritance_functionality() {
        response = rods.collections().set_inheritance(token, "/tempZone/home/rods/test", true, true);
        CollectionsSetInheritance mapped = JsonUtil.fromJson(response.getBody(), CollectionsSetInheritance.class);

        Response responseStat = rods.collections().stat(token,"/tempZone/home/rods/test", null);
        CollectionsStat mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);

        assertEquals(true, mappedStat.isInheritance_enabled());
        assertEquals(0, mapped.getIrods_response().getStatus_code());
    }

    @Test
    public void modify_permissions_functionality() {
        // creating parameter
        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
        jsonParam.add(new ModifyPermissionsOperations("alice", "write"));


        response =  rods.collections().modify_permissions(token,"/tempZone/home/rods", jsonParam, true);
        CollectionsModifyPermissions mapped = JsonUtil.fromJson(response.getBody(), CollectionsModifyPermissions.class);

        assertEquals(0, mapped.getIrods_response().getStatus_code());

        // expected failed_operation
        Mapped.IrodsResponse.FailedOperation expectedFailed = new Mapped.IrodsResponse.FailedOperation();
        expectedFailed.setOperation(null);
        expectedFailed.setOperation_index(0);
        expectedFailed.setError_message(null);
        expectedFailed.setStatus_message(null);
        assertEquals(expectedFailed, mapped.getIrods_response().getFailed_operation());
    }

    @Test
    public void modify_metadata_functionality() {
        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
        jsonParam2.add(new ModifyMetadataOperations("add", "attr", "value", "unit"));
        response = rods.collections().modify_metadata(token,"/tempZone/home/rods/test", jsonParam2, true);
        CollectionsModifyMetadata mapped = JsonUtil.fromJson(response.getBody(), CollectionsModifyMetadata.class);

        assertEquals(0, mapped.getIrods_response().getStatus_code());

        // expected failed_operation
        Mapped.IrodsResponse.FailedOperation expectedFailed = new Mapped.IrodsResponse.FailedOperation();
        expectedFailed.setOperation(null);
        expectedFailed.setOperation_index(0);
        expectedFailed.setError_message(null);
        expectedFailed.setStatus_message(null);
        assertEquals(expectedFailed, mapped.getIrods_response().getFailed_operation());
    }

    @Test
    public void rename_functionality() {
        response = rods.collections().rename(token, "/tempZone/home/rods/test",
                "/tempZone/home/rods/test1" );
        CollectionsRename mapped = JsonUtil.fromJson(response.getBody(), CollectionsRename.class);

        List<String> expectedList = new ArrayList<>();
        expectedList.add("/tempZone/home/rods/test1");
        updateList();

        assertEquals(0, mapped.getIrods_response().getStatus_code());
        assertEquals(expectedList, entries);
    }

    @Test
    public void touch_functionality() {
        response = rods.collections().touch(token, "/tempZone/home/rods/test", 0, null);
        CollectionsTouch mapped = JsonUtil.fromJson(response.getBody(), CollectionsTouch.class);

        assertEquals(0, mapped.getIrods_response().getStatus_code());
    }


    // below are tests to make sure everything is being mapped correctly
    @Test
    public void list_no_entries_mapped() {
        resetCollections();
        response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);

        // json string: {"entries":null,"irods_response":{"status_code":0}}

        // creating expected irods_response
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        // creating expected object
        CollectionsList expectedMapped = new CollectionsList();
        expectedMapped.setEntries(null);
        expectedMapped.setIrods_response(expectedIrodsResponse);

        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void create_mapped() {
        resetCollections();
        response =  rods.collections().create(token, "/tempZone/home/rods/test", false);
        CollectionsCreate actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsCreate.class);

        // json string: {"created":true,"irods_response":{"status_code":0}}

        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsCreate expectedMapped = new CollectionsCreate();
        expectedMapped.setCreated(true);
        expectedMapped.setIrods_response(expectedIrodsResponse);

        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void remove_mapped() {
        response = rods.collections().list(token, "/tempZone/home/rods", false, null);
        CollectionsList listMapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
        if (listMapped.getEntries() == null || !listMapped.getEntries().contains("/tempZone/home/rods/test3")) {
            rods.collections().create(token, "/tempZone/home/rods/test3", false);
        }

        response = rods.collections().remove(token, "/tempZone/home/rods/test3", false, false);
        CollectionsRemove actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsRemove.class);

        // json string: {"irods_response":{"status_code":0}}

        // creating expected object
        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsRemove expectedMapped = new CollectionsRemove();
        expectedMapped.setIrods_response(expectedIrodsResponse);

        assertEquals(expectedMapped, actualMapped);
    }

    @Test
    public void stat_mapped() {
        rods.collections().create(token, "/tempZone/home/rods/test", false);
        response = rods.collections().stat(token,"/tempZone/home/rods/test", null);
        CollectionsStat actualMapped = JsonUtil.fromJson(response.getBody(), CollectionsStat.class);

        System.out.println(response.getBody());

        Mapped.IrodsResponse expectedIrodsResponse = new Mapped.IrodsResponse();
        expectedIrodsResponse.setStatus_code(0);
        expectedIrodsResponse.setStatus_message(null);
        expectedIrodsResponse.setFailed_operation(null);

        CollectionsStat expectedMapped = new CollectionsStat();
        expectedMapped.setInheritance_enabled(false);
        expectedMapped.setRegistered(true);
        expectedMapped.setType("collection");
        expectedMapped.setIrods_response(expectedIrodsResponse);

        // set permissions
        CollectionsStat.Permissions permissions = new CollectionsStat.Permissions();
        permissions.setName("rods");
        permissions.setPerm("own");
        permissions.setType("rodsadmin");
        permissions.setZone("tempZone");
        List<CollectionsStat.Permissions> permissionsList = new ArrayList<>();
        permissionsList.add(permissions);
        expectedMapped.setPermissions(permissionsList);

        // set modified at
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        long modifiedAt = rootNode.get("modified_at").asLong();
        expectedMapped.setModified_at((int) modifiedAt);

        assertEquals(expectedMapped, actualMapped);
    }

    // helper methods
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