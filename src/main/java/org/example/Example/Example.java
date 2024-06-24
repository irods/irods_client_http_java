package org.example.Example;

import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.User;

import java.io.IOException;

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
        User rods = new User("rods", "rods", client);
        User alice = new User("alice", "alicepass", client);

        // client.info();


        //client.collections().create(rods, "/tempZone/home/rods/test").intermediates().execute();

        //"/tempZone/home/rods/test"
        // test2
        // test3

//        client.collections().remove(rods, "/tempZone/home/rods/test").noTrash().recurse().execute();


//        System.out.println("Token is: " + rods.getAuthToken());



        client.collections().list(rods, "/tempZone/home/rods").execute();
        //client.collections().remove(rods, "/tempZone/home/rods/test").noTrash().recurse().execute();

        System.out.println();
        client.collections().stat(rods, "/tempZone/home/rods/test").execute();
        System.out.println();
        client.collections().set_permission(rods, "/tempZone/home/rods/test","alice", "read").execute();
        System.out.println();
        client.collections().stat(rods, "/tempZone/home/rods/test").execute();
//        client.collections().list(rods, "/tempZone/home/rods").execute();

    }
}
