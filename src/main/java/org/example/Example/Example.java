package org.example.Example;

import org.example.Collections.Permission;
import org.example.Manager;
import org.example.IrodsException;
import org.example.Mapper.Collections.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Collections.Serialize.ModifyPermissionsOperations;
import org.example.User;
import org.example.Util.Response;
import org.example.Util.IrodsErrorCodes;

import java.io.IOException;
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
        responseData(
                rods.collections().list(token, "/tempZone/home/rods", false, null)
        );

        // create
//        responseData(
//                rods.collections().create(token, "/tempZone/home/alice", false)
//        );

        // remove
//        responseData(
//                rods.collections().remove(token, "/tempZone/home/rods/test3", false, false)
//        );

        // stat
//        responseData(
//                rods.collections().stat(token, "/tempZone/home/rods/test1", null)
//        );

        // set_permissions
//        responseData(
//                rods.collections().set_permission(token, "/tempZone/home/rods/test","alice",
//                        Permission.NULL, true)
//        );

        // set_inheritance
//        responseData(
//                rods.collections().set_inheritance(token, "/tempZone/home/rods/test", false, true)
//        );

        // modify_permissions
        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
        jsonParam.add(new ModifyPermissionsOperations("alice", "read"));
        System.out.println(jsonParam);
        responseData(
                rods.collections().modify_permissions(token,"/tempZone/home/rods", jsonParam, true)
        );


//
//        // modify_metadata
//        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
//        jsonParam2.add(new ModifyMetadataOperations("add", "test", "test1", "null"));
//        System.out.println(jsonParam2);
//        responseData(
//                rods.collections().modify_metadata(token,"/tempZone/home/rods/test", jsonParam2, true)
//        );

//        client.collections().rename("/tempZone/home/rods/test1", "/tempZone/home/rods/test" );

//        client.collections().touch("/tempZone/home/rods/test", 0, null);
//        String expiration = LocalDateTime.now().plusHours(1).toString();
//        System.out.println(expiration);

    }

    private static <T> void responseData(Response<T> response) {
        Map<Integer, String> errorCodes = IrodsErrorCodes.getMap();
        if (response.getStatusCode() == 0) {
            System.out.println(response.getData());
        } else {
            System.err.println(response.getStatusCode() + ": " + errorCodes.get(response.getStatusCode()));
        }
    }
}
