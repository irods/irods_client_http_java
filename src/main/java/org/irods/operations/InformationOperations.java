package org.irods.operations;


import org.irods.IrodsHttpClient;
import org.irods.util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class provides methods to interact with the information endpoint.
 */
public class InformationOperations {
    private final IrodsHttpClient client;
    private String baseUrl;


    /**
     * Constructs an {@code InformationOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public InformationOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/info";
    }

    /**
     * Returns general information about the iRODS HTTP API server.
     *
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response get() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .build();

        HttpResponse<String> response;
        try {
            response = client.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Response(response.statusCode(), response.body());
    }
}