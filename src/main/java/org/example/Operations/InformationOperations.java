package org.example.Operations;


import org.example.IrodsHttpClient;
import org.example.Util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InformationOperations {
    private final IrodsHttpClient client;
    private String baseUrl;


    public InformationOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/info";
    }

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
