package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.CollectionsCreate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    //TODO: don't forget that intermediates is an optional parameter

    /**
     * Creates a new collection
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param intermediates Whether to create intermediate directories
     * @throws IOException
     * @throws InterruptedException
     */
    public void create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException {
        String url = client.getBaseUrl() + "/collections";
        String token = client.getUser().getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        // creating the request body
        String form = formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString()
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> response = client.getClient().send(request, HttpResponse.BodyHandlers.ofString());

        // parse the JSON
        ObjectMapper mapper = new ObjectMapper();
        CollectionsCreate collectionsCreate = mapper.readValue(response.body(), CollectionsCreate.class);

        String message = collectionsCreate.getIrods_response().getStatus_message();
        boolean created = collectionsCreate.isCreated();

        if (created) {
            System.out.println("System created successfully");
        } else {
            System.out.println("Failed to create collection: " + message);
        }
    }

    /**
     * Overloaded method to handle the optional parameter for intermediates
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @throws IOException
     * @throws InterruptedException
     */
    public void create(User user, String lpath) throws IOException, InterruptedException {
        create(user, lpath, false);
    }

}
