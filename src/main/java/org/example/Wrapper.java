package org.example;

import org.example.Operations.*;
import org.example.Util.IrodsException;
import org.example.Util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class Wrapper {

    private String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private String user;
    private String password;
    private String authToken;
    private String openIdToken;
    public Wrapper(String baseUrl, String user, String password) {
        this.baseUrl = baseUrl;
        this.user = user;
        this.password = password;
    }

    public Wrapper(String baseUrl, String openIdToken) {
        this.baseUrl = baseUrl;
        this.openIdToken = openIdToken;
    }

    /**
     * Authenticates user by sending the following POST reqeust
     * curl -X POST -u rods:rods http://localhost:8888/irods-http-api/0.3.0/authenticate
     */
    public Response authenticate() throws IOException, InterruptedException, IrodsException {
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

        if (response.statusCode() == 200) {
            this.authToken = response.body();
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

    public String getAuthToken() {
        return authToken;
    }
}