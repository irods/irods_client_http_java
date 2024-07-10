import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Collections.Permission;
import org.example.Manager;
import org.example.IrodsException;
import org.example.Mapper.Collections.CollectionsCreate;
import org.example.Mapper.Collections.CollectionsList;
import org.example.Mapper.Collections.CollectionsModifyMetadata;
import org.example.Mapper.Collections.CollectionsModifyPermissions;
import org.example.Mapper.Collections.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Collections.Serialize.ModifyPermissionsOperations;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
import org.example.Util.JsonUtil;
import org.example.Util.Response;
import org.example.Util.Response2;
import org.example.Util.IrodsErrorCodes;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException, IrodsException {
        //http://52.91.145.195:8888/irods-http-api/0.3.0
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;

        // create client
        Manager rods = new Manager(baseUrl, "rods", "rods");


        // create users
//        User rods = new User("rods", "rods");
//        User alice = new User("alice", "alicepass");

        // authenticate
        rods.authenticate();
        String token = rods.getAuthToken();

        // list
        // demonstrates how all methods return a Response object which contains an HTTP status code and response body
        Response list = rods.collections().list(token, "/tempZone/home/rods", false, null);
        System.out.println("Response object: \n" + list);

        // demonstrate how someone can opt to map the JSON response
        CollectionsList listMapped = JsonUtil.fromJson(list.getBody(), CollectionsList.class);
        System.out.println("\nMapped JSON: \n" + listMapped);

        System.out.println("\n######################################################################\n");

        // create
        compare(
                rods.collections().create(token, "/tempZone/home/rods/test3", false),
                CollectionsCreate.class
        );

        // remove
//        compare(
//                rods.collections().remove(token, "/tempZone/home/rods/test3", false, false),
//                IrodsResponse.class
//        );
//        rods.collections().remove(token, "/tempZone/home/rods/test", false, false);
//                rods.collections().remove(token, "/tempZone/home/rods/test1", false, false);
//                rods.collections().remove(token, "/tempZone/home/rods/test2", false, false);




        // stat
//        responseData(
//                rods.collections().stat(token, "/tempZone/home/rods/test1", null)
//        );

        // set_permissions
//        responseData(
//                rods.collections().set_permission(token, "/tempZone/home/rods/test","alice",
//                        Permission.NULL, true)
//        );
//        System.out.println(
//                rods.collections().set_permission(token, "/tempZone/home/rods/test","alice",
//                        Permission.NULL, true)
//        );

        // set_inheritance
//        responseData(
//                rods.collections().set_inheritance(token, "/tempZone/home/rods/test", false, true)
//        );

//        // modify_permissions
//        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
//        jsonParam.add(new ModifyPermissionsOperations("alice", "read"));
//        Response response =  rods.collections().modify_permissions(token,"/tempZone/home/rods", jsonParam, true);
//        System.out.println(response.getBody());
//        printMapped(response, CollectionsModifyPermissions.class);
//
//
//        // modify_metadata
        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
        jsonParam2.add(new ModifyMetadataOperations("add", "test", "test1", "null"));
        Response response2 = rods.collections().modify_metadata(token,"/tempZone/home/rods/test3", jsonParam2, true);


        System.out.println(response2.getBody());
        printMapped(response2, CollectionsModifyMetadata.class);


//        client.collections().rename("/tempZone/home/rods/test1", "/tempZone/home/rods/test" );

//        client.collections().touch("/tempZone/home/rods/test", 0, null);
//        String expiration = LocalDateTime.now().plusHours(1).toString();
//        System.out.println(expiration);

    }

    private static <T> void responseData(Response2<T> response2) {
        Map<Integer, String> errorCodes = IrodsErrorCodes.getMap();
        if (response2.getStatusCode() == 0) {
            System.out.println(response2.getData());
        } else {
            System.err.println(response2.getStatusCode() + ": " + errorCodes.get(response2.getStatusCode()));
        }
    }

    private static <T> void printMapped(Response response, Class<T> responseType) throws JsonProcessingException {
        T mapped = JsonUtil.fromJson(response.getBody(), responseType);
        System.out.println(mapped);
    }

    private static <T> void compare(Response response, Class<T> responseType) throws JsonProcessingException {
        System.out.println(response.getBody());
        printMapped(response, responseType);
    }
}
