package org.example;

import java.io.IOException;

public class Example {

    public static void main(String[] args) throws IOException, InterruptedException {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        //http://52.91.145.195:8888/irods-http-api/0.3.0
        User rods = new User("rods", "rods");
        irodsClient client = new irodsClient(address, port, version, rods);


        System.out.println("Token is: " + rods.getAuthToken());


    }
}
