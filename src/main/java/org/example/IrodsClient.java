package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Collections.CollectionOperations;
import org.example.Mapper.Info;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

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
    protected void authenticate(User user) throws IOException, InterruptedException {
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

        if (response.statusCode() == 200) {
            user.setAuthToken(response.body());
        } else {
            throw new RuntimeException("Failed to authenticate: " + response.statusCode());
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

    // response code of 401 means attempting to use an expired or invalid token
    // may need to reauthenticate

}