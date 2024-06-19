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

        // original way:
        //IrodsClient client = new IrodsClient(address, port, version, rods);

        //TODO: Think about how to better switch users instead of having to create new instances of the class for each user
        IrodsClient client = new IrodsClient.Builder()
                .address(address)
                .port(port)
                .version(version)
                .user(rods)
                .build();

        client.collections().create(rods, "test", false);
        System.out.println("Token is: " + rods.getAuthToken());


    }
}
