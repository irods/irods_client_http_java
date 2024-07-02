package org.example.Example;

import org.example.Collections.Permission;
import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.Mapper.Collections.CollectionsStat;
import org.example.Mapper.Collections.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Collections.Serialize.ModifyPermissionsOperations;
import org.example.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException, IrodsException {
        //http://52.91.145.195:8888/irods-http-api/0.3.0
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        // create client
        IrodsClient client = IrodsClient.newBuilder()
                .address(address)
                .port(port)
                .version(version)
                .build();

        // create users
        User rods = new User("rods", "rods");
        User alice = new User("alice", "alicepass");

        // authenticate users
//        client.authenticate(rods);
//        client.authenticate(alice);
//        rods.setAuthToken("ad9f2345-129d-40ac-b23e-2f3a77303d7f");

//        System.out.println(rods.getAuthToken());
//        List<String> entries = client.collections().list("/tempZone/home/rods", false, null);
//        System.out.println(entries + "\n");



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

        client.collections().touch("/tempZone/home/rods/test", 0, null);


    }
}
