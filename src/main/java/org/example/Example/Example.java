package org.example.Example;

import org.example.Collections.Permission;
import org.example.Mapper.Collections.ModifyMetadataOperations;
import org.example.Mapper.Collections.ModifyPermissionsOperations;
import org.example.IrodsClient;
import org.example.IrodsException;
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
        client.authenticate(rods);
        client.authenticate(alice);

        // client.info();

        List<String> entries = client.collections().list(rods, "/tempZone/home/rods", false, null);
        System.out.println(entries + "\n");

//        client.collections().list(rods, "/tempZone/home/rods").execute();
        client.collections().remove(rods, "/tempZone/home/rods/test3", false, false);
        client.collections().create(rods, "/tempZone/home/rods/test3", false);



//        client.collections().set_permission(rods, "/tempZone/home/rods/test","alice",
//                Permission.NULL, true);


//        client.collections().set_permission(rods, "/tempZone/home/rods/test","alice", Permission.READ).execute();

//        entries = client.collections().list(rods, "/tempZone/home/rods").execute();
//        System.out.println(entries + "\n");
//
//        client.collections().set_inheritance(rods, "/tempZone/home/rods/test", false, true);
//        client.collections().stat(rods, "/tempZone/home/rods/test", null);



//        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
//        jsonParam.add(new ModifyPermissionsOperations("alice", Permission.READ));
//        client.collections().modify_permissions(rods, "/tempZone/home/rods/test", jsonParam, true);

//        List<ModifyMetadataOperations> jsonParam2 = new ArrayList<>();
//        jsonParam2.add(new ModifyMetadataOperations("add", "test", "test1", "null"));
//        client.collections().modify_metadata(rods, "/tempZone/home/rods/test", jsonParam2, true);


//        client.collections().rename(rods, "/tempZone/home/rods/test3", "/tempZone/home/rods/test1" );

        client.collections().touch(rods, "/tempZone/home/rods/test", 0, null);
    }
}
