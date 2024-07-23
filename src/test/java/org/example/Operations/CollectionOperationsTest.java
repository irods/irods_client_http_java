//package org.example.Operations;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.Mapper.Serialize.ModifyMetadataOperations;
//import org.example.Mapper.Serialize.ModifyPermissionsOperations;
//import org.example.Util.Permission;
//import org.example.Wrapper;
//import org.example.Util.JsonUtil;
//import org.example.Util.Response;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class CollectionOperationsTest {
//    private Wrapper rods;
//    private String token;
//    private List<String> entries;
//    private Response response;
//    @Before
//    public void setup() {
//        String address = "52.91.145.195";
//        String port = "8888";
//        String version = "0.3.0";
//
//        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
//
//        // create client
//        rods = new Wrapper(baseUrl, "rods", "rods");
//        rods.authenticate();
//        token = rods.getAuthToken();
//
//        // create alice user
//        rods.userGroupOperations().create_user(token, "alice", "tempZone", "rodsuser");
//
//        resetCollections();
//        rods.collections().create(token, "/tempZone/home/rods/test");
//        updateList();
//    }
//
//    @Test
//    public void create_functionality() {
//        response = rods.collections().create(token, "/tempZone/home/rods/test2");
//        rods.collections().create(token, "/tempZone/home/rods/test3");
//
//        CollectionsCreate mapped = JsonUtil.fromJson(response.getBody(), CollectionsCreate.class);
//
//        updateList();
//
//        List<String> expectedList = new ArrayList<>();
//        expectedList.add("/tempZone/home/rods/test"); // made in @Before
//        expectedList.add("/tempZone/home/rods/test2");
//        expectedList.add("/tempZone/home/rods/test3");
//
//        assertEquals(expectedList, entries);
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//    }
//
//    @Test
//    public void remove_functionality() {
//        rods.collections().create(token, "/tempZone/home/rods/test2"); // test to be removed
//        updateList();
//
//        List<String> expectedList = new ArrayList<>();
//        expectedList.add("/tempZone/home/rods/test");
//        expectedList.add("/tempZone/home/rods/test2");
//
//        // ensure collections were created successfully
//        if (!entries.equals(expectedList)) {
//            fail("Something went wrong when attempting to create a collection");
//        }
//
//        response = rods.collections().remove(token, "/tempZone/home/rods/test2");
//        CollectionsRemove mapped = JsonUtil.fromJson(response.getBody(), CollectionsRemove.class);
//
//        updateList();
//        expectedList.remove("/tempZone/home/rods/test2");
//
//        assertEquals(expectedList, entries);
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//    }
//
//    @Test
//    public void stat_functionality() {
//        response = rods.collections().stat(token,"/tempZone/home/rods/test");
//        CollectionsStat mapped = JsonUtil.fromJson(response.getBody(), CollectionsStat.class);
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//    }
//
//    @Test
//    public void list_functionality() {
//        response = rods.collections().list(token, "/tempZone/home/rods");
//        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
//
//        List<String> expectedList = new ArrayList<>();
//        expectedList.add("/tempZone/home/rods/test");
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//        assertEquals(expectedList, mapped.getEntries());
//    }
//
//    @Test
//    public void set_permission_functionality() {
//        response = rods.collections().set_permission(token, "/tempZone/home/rods/test", "alice",
//                Permission.READ);
//        CollectionsSetPermission mapped = JsonUtil.fromJson(response.getBody(), CollectionsSetPermission.class);
//
//        Response responseStat = rods.collections().stat(token,"/tempZone/home/rods/test");
//        CollectionsStat mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);
//
//        // expected permission
//        CollectionsStat.Permissions expectedPermission = new CollectionsStat.Permissions();
//        expectedPermission.setZone("tempZone");
//        expectedPermission.setType("rodsuser");
//        expectedPermission.setPerm("read_object");
//        expectedPermission.setName("alice");
//
//
//        assertTrue("Permission should contain: \n" + expectedPermission,
//                mappedStat.getPermissions().contains(expectedPermission));
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//
//        // now try and change the permission type to see if it still works
//        response = rods.collections().set_permission(token, "/tempZone/home/rods/test", "alice",
//                Permission.WRITE);
//
//        responseStat = rods.collections().stat(token,"/tempZone/home/rods/test");
//        mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);
//        expectedPermission.setPerm("modify_object");
//        assertTrue("Permission should contain: \n" + expectedPermission,
//                mappedStat.getPermissions().contains(expectedPermission));
//    }
//
//    @Test
//    public void set_inheritance_functionality() {
//        response = rods.collections().set_inheritance(token, "/tempZone/home/rods/test", 1);
//        CollectionsSetInheritance mapped = JsonUtil.fromJson(response.getBody(), CollectionsSetInheritance.class);
//
//        Response responseStat = rods.collections().stat(token,"/tempZone/home/rods/test");
//        CollectionsStat mappedStat = JsonUtil.fromJson(responseStat.getBody(), CollectionsStat.class);
//
//        assertEquals(true, mappedStat.isInheritance_enabled());
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//    }
//
//    @Test
//    public void modify_permissions_functionality() {
//        // creating parameter
//        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
//        jsonParam.add(new ModifyPermissionsOperations("alice", "write"));
//
//
//        response =  rods.collections().modify_permissions(token,"/tempZone/home/rods", jsonParam);
//        CollectionsModifyPermissions mapped = JsonUtil.fromJson(response.getBody(), CollectionsModifyPermissions.class);
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//
//        // expected failed_operation
//        Mapped.IrodsResponse.FailedOperation expectedFailed = new Mapped.IrodsResponse.FailedOperation();
//        expectedFailed.setOperation(null);
//        expectedFailed.setOperation_index(0);
//        expectedFailed.setError_message(null);
//        expectedFailed.setStatus_message(null);
//        assertEquals(expectedFailed, mapped.getIrods_response().getFailed_operation());
//    }
//
//    @Test
//    public void modify_metadata_functionality() {
//        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
//        jsonParam2.add(new ModifyMetadataOperations("add", "attr", "value", "unit"));
//        response = rods.collections().modify_metadata(token,"/tempZone/home/rods/test", jsonParam2);
//        CollectionsModifyMetadata mapped = JsonUtil.fromJson(response.getBody(), CollectionsModifyMetadata.class);
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//
//        // expected failed_operation
//        Mapped.IrodsResponse.FailedOperation expectedFailed = new Mapped.IrodsResponse.FailedOperation();
//        expectedFailed.setOperation(null);
//        expectedFailed.setOperation_index(0);
//        expectedFailed.setError_message(null);
//        expectedFailed.setStatus_message(null);
//        assertEquals(expectedFailed, mapped.getIrods_response().getFailed_operation());
//    }
//
//    @Test
//    public void rename_functionality() {
//        response = rods.collections().rename(token, "/tempZone/home/rods/test",
//                "/tempZone/home/rods/test1" );
//        CollectionsRename mapped = JsonUtil.fromJson(response.getBody(), CollectionsRename.class);
//
//        List<String> expectedList = new ArrayList<>();
//        expectedList.add("/tempZone/home/rods/test1");
//        updateList();
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//        assertEquals(expectedList, entries);
//    }
//
//    @Test
//    public void touch_functionality() {
//        response = rods.collections().touch(token, "/tempZone/home/rods/test");
//        CollectionsTouch mapped = JsonUtil.fromJson(response.getBody(), CollectionsTouch.class);
//
//        assertEquals(0, mapped.getIrods_response().getStatus_code());
//    }
//
//    // helper methods
//    private void updateList() {
//        Response response = rods.collections().list(token, "/tempZone/home/rods");
//        CollectionsList mapped = JsonUtil.fromJson(response.getBody(), CollectionsList.class);
//
//        entries = mapped.getEntries();
//    }
//
//    private void resetCollections() {
//        updateList();
//
//        // Ensure the list is empty
//        if (entries != null && !entries.isEmpty()) {
//            for (String lpath : entries) {
//                rods.collections().remove(token, lpath);
//                rods.dataObject().remove(token, lpath, 0);
//            }
//        }
//        updateList();
//        assertEquals("There should be nothing listed in entries when running .list()", null, entries);
//    }
//
//    private int getIrodsResponseStatusCode(String jsonString) {
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            JsonNode rootNode = objectMapper.readTree(jsonString);
//
//            JsonNode statusCodeNode = rootNode.path("irods_response").path("status_code");
//            return statusCodeNode.asInt();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//
//
//}