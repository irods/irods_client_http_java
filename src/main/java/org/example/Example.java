package org.example;

import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException {
        //http://52.91.145.195:8888/irods-http-api/0.3.0
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        // Create a new user
        User rods = new User("rods", "rods");

        // original way:
        //IrodsClient client = new IrodsClient(address, port, version, rods);

        IrodsClient client = new IrodsClient.Builder()
                .address(address)
                .port(port)
                .version(version)
                .user(rods)
                .build();


        System.out.println("Token is: " + rods.getAuthToken());


    }
}
