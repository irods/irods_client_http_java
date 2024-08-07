package org.example;

import org.example.Operations.*;
import org.example.Util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class IrodsHttpClient {

    private String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private String openIdToken;
    public IrodsHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public IrodsHttpClient(String baseUrl, String openIdToken) {
        this.baseUrl = baseUrl;
        this.openIdToken = openIdToken;
    }

    /**
     * Authenticates user by sending the following POST reqeust
     * curl -X POST -u rods:rods http://localhost:8888/irods-http-api/0.3.0/authenticate
     */
    public Response authenticate(String username, String password) {
        // creating authentication header
        String auth = username + ":" + password;
        // encodes user and password into a suitable format for HTTP basic authentication
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        String url = baseUrl + "/authenticate";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Response(response.statusCode(), response.body());
    }

    public CollectionOperations collections() {
        return new CollectionOperations(this);
    }

    public DataObjectOperations dataObject() {
        return new DataObjectOperations(this);
    }

    public InformationOperations information() {
        return new InformationOperations(this);
    }

    public QueryOperations queryOperations() {
        return new QueryOperations(this);
    }

    public ResourceOperations resourceOperations() {
        return new ResourceOperations(this);
    }

    public RuleOperations ruleOperations() {
        return new RuleOperations(this);
    }

    public TicketOperations ticketOperations() {
        return new TicketOperations(this);
    }

    public UserGroupOperations userGroupOperations() {
        return new UserGroupOperations(this);
    }

    public ZoneOperations zoneOperations() {
        return new ZoneOperations(this);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public HttpClient getClient() {
        return client;
    }

}