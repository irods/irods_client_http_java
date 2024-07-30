package org.example.Operations;


import org.example.Wrapper;
import org.example.Util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InformationOperations {
    private final Wrapper client;
    private String baseUrl;


    public InformationOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/info";
    }
    /**
     * Sends request to /info endpoint and parses the response
     * @return Info objected parsed from the response JSON
     */
    public Response info() {
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
