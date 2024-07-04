package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Collections.CollectionOperations;
import org.example.Mapper.Info;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Manager {

    private String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private String user;
    private String password;
    private String authToken;
    private String openIdToken;
    public Manager(String baseUrl, String user, String password) {
        this.baseUrl = baseUrl;
        this.user = user;
        this.password = password;
    }

    public Manager(String baseUrl, String openIdToken) {
        this.baseUrl = baseUrl;
        this.openIdToken = openIdToken;
    }

    /**
     * Allows clients to use the builder pattern to create instances of the IrodsClient
     * @return new instance of the IrodsBuilder class.
     */
//    public static IrodsBuilder newBuilder() {
//        return new IrodsBuilder();
//    }


    /**
     * Authenticates user by sending the following POST reqeust
     * curl -X POST -u rods:rods http://localhost:8888/irods-http-api/0.3.0/authenticate
     */
    public void authenticate() throws IOException, InterruptedException, IrodsException {
        //TODO: consider what happens with proxies and if you can concatenate like this

        // creating authentication header
        String auth = user + ":" + password;
        // encodes user and password into a suitable format for HTTP basic authentication
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        String url = baseUrl + "/authenticate";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String token = response.body();

        if (response.statusCode() == 200) {
            this.authToken = token;
        } else {
            throw new IrodsException("Failed to authenticate: " + response.statusCode());
        }
    }

    /**
     * Sends request to /info endpoint and parses the response
     * @return Info objected parsed from the response JSON
     * @throws IOException
     * @throws InterruptedException
     */
    public Info info() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/info"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        //parse JSON into objects
        ObjectMapper mapper = new ObjectMapper();
        Info info = mapper.readValue(response.body(), Info.class);
        return (info);
    }

    public CollectionOperations collections() {
        return new CollectionOperations(this);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public HttpClient getClient() {
        return client;
    }

    public String getAuthToken() {
        return authToken;
    }
}