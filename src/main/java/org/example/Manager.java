package org.example;

import org.example.Mapper.Operations.*;
import org.example.Operations.*;
import org.example.Util.IrodsException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

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