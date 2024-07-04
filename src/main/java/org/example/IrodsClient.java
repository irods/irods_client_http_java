package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Collections.CollectionOperations;
import org.example.Mapper.Info;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class IrodsClient {

    private String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Enforces the use of the builder. Making it package-private ensures that users cannot create an instance of this.
     * @param address The server address
     * @param port The server port
     * @param version The API version
     */
    IrodsClient(String address, String port, String version) {
        this.baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
    }

    /**
     * Allows clients to use the builder pattern to create instances of the IrodsClient
     * @return new instance of the IrodsBuilder class.
     */
    public static IrodsBuilder newBuilder() {
        return new IrodsBuilder();
    }


    /**
     * Authenticates user by sending the following POST reqeust
     * curl -X POST -u rods:rods http://localhost:8888/irods-http-api/0.3.0/authenticate
     *
     * @param user The user object that is being authenticated
     * @throws IOException
     * @throws InterruptedException
     */
    public void authenticate(User user) throws IOException, InterruptedException, IrodsException {
        //TODO: consider what happens with proxies and if you can concatenate like this

        // creating authentication header
        String auth = user.getUsername() + ":" + user.getPassword();
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
            saveToken(token);
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

    private void saveToken(String token) throws IOException {
        // the time one hour from now
        String expiration = LocalDateTime.now().plusHours(1).toString();

        Map<String, Object > tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("expiration", expiration);

        // save token and expiration as a JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("token.json"), tokenData);
    }

}