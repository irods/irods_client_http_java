package org.irods;

import org.irods.operations.*;
import org.irods.util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * This class provides a client for interacting with the iRODS HTTP API.
 * <p>
 *     It allows developers to perform various iRODS operations by constructing HTTP requests and sending them to the
 *     iRODS server. The client utilizes Java's built-in `HttpClient` class for making HTTP requests.
 * </p>
 */
public class IrodsHttpClient {
    private String baseUrl;

    /**
     * The internal HTTP client used for making requests.
     */
    private final HttpClient client = HttpClient.newHttpClient();
    private String openIdToken;

    /**
     * Constructs a new {@code IrodsHttpClient} instance with the provided base URL. This constructor does not set an OpenID
     * token for authentication.
     *
     * @param baseUrl The base URL of the iRODS server
     */
    public IrodsHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Constructs a new {@code IrodsHttpClient} instance with the provided base URL and OpenID token. This constructor
     * allows for OpenID token based authentication.
     *
     * @param baseUrl The base URL of the iRODS server
     * @param openIdToken The OpenID token for authentication
     */
    public IrodsHttpClient(String baseUrl, String openIdToken) {
        this.baseUrl = baseUrl;
        this.openIdToken = openIdToken;
    }

    /**
     * Authenticates a user with the iRODS server using username and password. Sends a POST request to the
     * `/authenticate` endpoint of the iRODS server with basic authentication credentials.
     *
     * @param username The username for authentication
     * @param password The password for authentication
     * @return A {@link Response} object containing the status and body of the HTTP response.
     * @throws RuntimeException if an {@link IOException} or {@link InterruptedException} occurs during the request
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

    /**
     * Returns an instance of the {@link CollectionOperations} class to execute methods on the collections endpoint.
     *
     * @return A {@code CollectionOperations} object
     */
    public CollectionOperations collections() {
        return new CollectionOperations(this);
    }

    /**
     * Returns an instance of the {@link DataObjectOperations} class to execute methods on the data-objects endpoint.
     *
     * @return A {@code DataObjectOperations} object
     */
    public DataObjectOperations dataObject() {
        return new DataObjectOperations(this);
    }

    /**
     * Returns an instance of the {@link InformationOperations} class to execute methods on the information endpoint.
     *
     * @return An {@code InformationOperations} object
     */
    public InformationOperations information() {
        return new InformationOperations(this);
    }

    /**
     * Returns an instance of the {@link QueryOperations} class to execute methods on the query endpoint.
     *
     * @return A {@code QueryOperations} object
     */
    public QueryOperations queryOperations() {
        return new QueryOperations(this);
    }

    /**
     * Returns an instance of the {@link ResourceOperations} class to execute methods on the resources endpoint.
     *
     * @return A {@code ResourceOperations} object
     */
    public ResourceOperations resourceOperations() {
        return new ResourceOperations(this);
    }

    /**
     * Returns an instance of the {@link RuleOperations} class to execute methods on the rules endpoint.
     *
     * @return A {@code RuleOperations} object
     */
    public RuleOperations ruleOperations() {
        return new RuleOperations(this);
    }

    /**
     * Returns an instance of the {@link TicketOperations} class to execute methods on the tickets endpoint.
     *
     * @return A {@code TicketOperations} object
     */
    public TicketOperations ticketOperations() {
        return new TicketOperations(this);
    }

    /**
     * Returns an instance of the {@link UserGroupOperations} class to execute methods on the users-groups endpoint.
     *
     * @return A {@code UserGroupOperations} object
     */
    public UserGroupOperations userGroupOperations() {
        return new UserGroupOperations(this);
    }

    /**
     * Returns an instance of the {@link ZoneOperations} class to execute methods on the zones endpoint.
     *
     * @return A {@code ZoneOperations} object
     */
    public ZoneOperations zoneOperations() {
        return new ZoneOperations(this);
    }

    /**
     * Returns the base URL used by the client.
     *
     * @return The base URL.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Returns the internal HttpClient instance used for making requests.
     *
     * @return The internal HttpClient object
     */
    public HttpClient getClient() {
        return client;
    }
}