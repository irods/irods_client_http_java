package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class irodsClient {

    private final String baseUrl;
    private static HttpClient client;
    private User user;

    public irodsClient(String address, String port, String version) {
        this.client = HttpClient.newHttpClient();
        this.baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
        //this.baseUrl = "http://52.91.145.195:8888/irods-http-api/0.3.0";
    }
    //private static final String url = "http://52.91.145.195:8888/irods-http-api/0.3.0";

    public irodsClient(String address, String port, String version, User user) throws IOException, InterruptedException {
        this(address, port, version);
        this.user = user;
        authenticate(user);
    }

    /** Replicates the following request:
     * curl -X POST -u rods:rods http://localhost:9000/irods-http-api/0.3.0/authenticate
     *
     * @param user The user objcet that is being authenticated
     * @throws IOException
     * @throws InterruptedException
     * @return authetnciation token of the user
     */
    private void authenticate (User user) throws IOException, InterruptedException {
        // creating authentication header
        String auth = user.getUsername() + ":" + user.getPassword();
        // encodes user and password into a suitable format for HTTP basic authentication
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/authenticate"))
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

    public Info info() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/info"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //parse JSON into objects
        ObjectMapper mapper = new ObjectMapper();
        Info info = mapper.readValue(response.body(), Info.class);
        return (info);
    }

    // response code of 401 means attempting to use an expired or invalid token
    // may need to reauthenticate










}