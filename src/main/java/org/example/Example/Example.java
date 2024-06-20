package org.example.Example;

import org.example.IrodsClient;
import org.example.User;

import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException {
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


        client.collections().create(rods, "/tempZone/home/alice/test2", true);

        client.collections().remove(rods, "/tempZone/home/alice/test2").noTrash().recurse().execute();
        client.collections().remove(rods, "/tempZone/home/alice/test2").execute();


        System.out.println("Token is: " + rods.getAuthToken());

    }
}
