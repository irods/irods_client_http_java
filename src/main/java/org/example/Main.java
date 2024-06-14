package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;

public class Main {

    private static final String url = "http://52.91.145.195:8888/irods-http-api/0.3.0";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void info(String url) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/info"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println("Response status code: " + response.statusCode());
        //System.out.println(response.body());

        //parse JSON into objects
        ObjectMapper mapper = new ObjectMapper();
        Status status = mapper.readValue(response.body(), Status.class);
        //System.out.println("API Version: " + status.getVersion());
        System.out.println(response.body());
    }




    /** Replicates the following request:
     * curl -X POST -u rods:rods http://localhost:9000/irods-http-api/0.3.0/authenticate
     *
     * @param url of the request
     * @param user username of the user
     * @param pass password of the user
     * @throws IOException
     * @throws InterruptedException
     * @return authetnciation token of the user
     */
    public static String authenticate (String url, String user, String pass) throws IOException, InterruptedException {
        // creating authentication header
        String auth = user + ":" + pass;
        // encodes user and password into a suitable format for HTTP basic authentication
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/authenticate"))
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Token:" + response.body());
        System.out.println(response.statusCode());
        return response.body();
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        info(url);
        String token = authenticate(url, "alice", "alicepass");
        System.out.println("Token is: " + token);

    }
}