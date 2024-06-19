package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for all the Collections Operations
 */
public class CollectionOperations {

    private final IrodsClient client;

    public CollectionOperations(IrodsClient client) {
        this.client = client;
    }
    public void create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException {
        String url = client.getBaseUrl() + "/collections";
        String token = client.getUser().getAuthToken();
       // String intermediates = createIntermediates ? "1" : "0";

        // creating the paramters
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        // creating the request body
        String form = formData.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer" + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> response = client.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("System created successfully");
        } else {
            throw new RuntimeException("Failed to create collection: " + response.statusCode());
        }



        //System.out.println(client.getUser().getUsername());
    }


}
