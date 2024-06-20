package org.example;

import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException {
        //http://52.91.145.195:8888/irods-http-api/0.3.0
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        // Create users
        User rods = new User("rods", "rods");
        User alice = new User("alice", "alicepass");

        // original way:
        //IrodsClient client = new IrodsClient(address, port, version, rods);

        //TODO: Think about how to better switch users instead of having to create new instances of the class for each user
        IrodsClient client = new IrodsClient.Builder()
                .address(address)
                .port(port)
                .version(version)
                .user(rods)
                .build();

        IrodsClient clientAlice = new IrodsClient.Builder()
                .address(address)
                .port(port)
                .version(version)
                .user(rods)
                .build();

        //TODO: can probably take out User parameter if we insantiate a new client for each user
        client.collections().create(rods, "/tempZone/home/alice/test2", true);
        client.collections().remove(rods, "/tempZone/home/alice/test2", true, false);
        //clientAlice.collections().create(alice, "/tempZone/home/alice/test2", true);
        //client.collections().create(rods, "/tempZone/home/alice/test");
        //System.out.println("Token is: " + rods.getAuthToken());

    }
}
