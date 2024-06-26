package org.example.Example;

import org.example.Collections.Permission;
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

        // Create users
        //TODO think about the way authentication is being done
        User rods = new User("rods", "rods", client);
        User alice = new User("alice", "alicepass", client);

        // client.info();

        // TODO think about builder notation
//        client.collections().list(rods, "/tempZone/home/rods").execute();
//        client.collections().remove(rods, "/tempZone/home/rods/test").noTrash().recurse().execute();
//        client.collections().create(rods, "/tempZone/home/rods/test").intermediates().execute();

//        client.collections().stat(rods, "/tempZone/home/rods/test").execute();

//        client.collections().set_permission(rods, "/tempZone/home/rods/test","alice", Permission.READ).execute();
        List<String> entries = client.collections().list(rods, "/tempZone/home/rods").execute();
        System.out.println(entries + "\n");

//        client.collections().set_permission(rods, "/tempZone/home/rods/test","alice", Permission.READ).execute();

//        entries = client.collections().list(rods, "/tempZone/home/rods").execute();
//        System.out.println(entries + "\n");
//
//        client.collections().set_inheritance(rods, "/tempZone/home/rods/test", false).admin().execute();


        List<ModifyPermissionsOperations> jsonParam = new ArrayList<>();
        jsonParam.add(new ModifyPermissionsOperations("alice", Permission.READ));
        client.collections().modify_permissions(rods, "/tempZone/home/rods/test", jsonParam).execute();

//        client.collections().rename(rods, "/tempZone/home/rods/test4", "/tempZone/home/rods/test2" );

//        client.collections().touch(rods, "/tempZone/home/rods/test").mtime(1).execute();
    }
}
