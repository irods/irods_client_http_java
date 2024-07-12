package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Info;
import org.example.Util.Response;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class InformationOperations {
    private final Manager client;
    private String baseUrl;


    public InformationOperations(Manager client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/info";
    }
    /**
     * Sends request to /info endpoint and parses the response
     * @return Info objected parsed from the response JSON
     * @throws IOException
     * @throws InterruptedException
     */
    public Response info() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .build();

        HttpResponse<String> response = client.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        return new Response(response.statusCode(), response.body());
    }
}
