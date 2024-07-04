package org.example.Example;

import org.example.Manager;
import org.example.IrodsException;
import org.example.User;
import org.example.Util.Response;
import org.example.Util.IrodsErrorCodes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException, IrodsException {
        //http://52.91.145.195:8888/irods-http-api/0.3.0
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
        Map<Integer, String> errorCodes = IrodsErrorCodes.getMap();

        // create client
        Manager rods = new Manager(baseUrl, "rods", "rods");

        // create users
//        User rods = new User("rods", "rods");
//        User alice = new User("alice", "alicepass");

        // authenticate users
//        client.authenticate(rods);
//        client.authenticate(alice);
//        rods.setAuthToken("ad9f2345-129d-40ac-b23e-2f3a77303d7f");
        rods.authenticate();
        String token = rods.getAuthToken();
//        System.out.println(token);


//        System.out.println(rods.getAuthToken());
        Response<List<String>> listResponse = rods.collections().list(token, "/tempZone//home/rods",
                false, null);
        if (listResponse.getStatusCode() == 0) {
            System.out.println(listResponse.getData());
        } else {
            System.err.println(listResponse.getStatusCode() + ": " + errorCodes.get(listResponse.getStatusCode()));
        }




//        List<String> entries = client.collections().list(alice, "/tempZone/home/alice", false, null);
//        System.out.println(entries + "\n");

//        client.collections().create(alice, "/tempZone/home/alice/test1", false);

//        client.collections().remove("/tempZone/home/rods/test3", false, false);
//        client.collections().create("/tempZone/home/rods/test3", false);
//        System.out.println(test);

//        client.collections().set_permission("/tempZone/home/rods/test","alice", Permission.NULL, true);

//        client.collections().set_inheritance("/tempZone/home/rods/test", false, true);
//        client.collections().stat("/tempZone/home/rods/test", null);


//        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
//        jsonParam.add(new ModifyPermissionsOperations("test", Permission.READ));
//        client.collections().modify_permissions("/tempZone/home/rods/test1", jsonParam, true);
//
//        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
//        jsonParam2.add(new ModifyMetadataOperations("add", "test", "test1", "null"));
//        client.collections().modify_metadata("/tempZone/home/rods/test", jsonParam2, true);

//        client.collections().rename("/tempZone/home/rods/test1", "/tempZone/home/rods/test" );

//        client.collections().touch("/tempZone/home/rods/test", 0, null);
//        String expiration = LocalDateTime.now().plusHours(1).toString();
//        System.out.println(expiration);

    }
}
