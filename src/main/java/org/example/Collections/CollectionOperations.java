package org.example.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.IrodsClient;
import org.example.Mapper.CollectionsCreate;
import org.example.Mapper.IrodsResponse;
import org.example.User;
import org.example.Util.HttpRequestUtil;

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
    private String baseUrl;

    public CollectionOperations(IrodsClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/collections";
    }

    /**
     * Creates a new collection.
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param intermediates Whether to create intermediate directories
     * @throws IOException
     * @throws InterruptedException
     */
    public void create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException {
        String token = user.getAuthToken();
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        CollectionsCreate collectionsCreate = HttpRequestUtil.sendAndParse(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);

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

    // Main method to handle the remove operation. Protected so it can only be accessed from this package. Enforces
    // use of builder
    protected void remove(User user, String lpath, boolean recurse, boolean noTrash) throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );


        CollectionsCreate mapped = HttpRequestUtil.sendAndParse(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);
        System.out.println(mapped.getIrods_response());
//        System.out.println("recurse: " + recurse);
//        System.out.println("no_trash: " + noTrash);

    }

    // how the user will actually call the remove method
    //TODO: See if there's a way to throw an error if user forgets .execute()
    public RemoveBuilder remove(User user, String lpath) {
        return  new RemoveBuilder(this, user, lpath);
    }
}
