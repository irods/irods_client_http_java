package org.example.Collections;

import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.Mapper.CollectionsCreate;
import org.example.Mapper.CollectionsList;
import org.example.Mapper.CollectionsStat;
import org.example.User;
import org.example.Util.HttpRequestUtil;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

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
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param intermediates Whether to create intermediate directories. Optional parameter
     * @throws IOException
     * @throws InterruptedException
     */
    protected void create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        CollectionsCreate mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);

        String message = mapped.getIrods_response().getStatus_message();
        boolean created = mapped.isCreated();

        if (created) {
            System.out.println("Collection '" + lpath + "' created successfully");
        } else {
            throw new IrodsException("Failed to create collection: " + message);
        }
    }

    /**
     * Initiates the creation of a collection
     * @param user The user making the request
     * @param lpath The logical path
     * @return CreateBuilder instance that allows for the user to chain optional parameters
     */
    public CreateBuilder create(User user, String lpath) {
        return new CreateBuilder(this, user, lpath);
    }


    /**
     * Removes a collection
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param recurse If true, contents of the collection will be removed. Optional parameter
     * @param noTrash If true, collection is permanently removed. Optional parameter
     * @throws IOException
     * @throws InterruptedException
     */
    protected void remove(User user, String lpath, boolean recurse, boolean noTrash) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );

        CollectionsCreate mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);

        int statusCode = mapped.getIrods_response().getStatus_code();
        String statusMessage = mapped.getIrods_response().getStatus_message();

        // throws errors if found
        statusCodeMessage(statusCode, statusMessage, "Could not remove collection");

        if (statusCode == 0) {
            System.out.println("'" + lpath +"' removed successfully");
        }
    }

    //TODO: See if there's a way to throw an error if user forgets .execute()
    /**
     * Initiates the removal of a collection
     * @param user The user making the request
     * @param lpath The logical path
     * @return RemoveBuilder instance that allows for the user to chain optional parameters
     */
    public RemoveBuilder remove(User user, String lpath) {
        return  new RemoveBuilder(this, user, lpath);
    }

    /**
     * Returns information about a collection
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param ticket An optional parameter
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
     */
    protected void stat(User user, String lpath, String ticket) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "stat",
                "lpath", lpath
        );

        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsStat mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsStat.class);

        System.out.println(mapped);

    }

    /**
     * Initiates the stat operation of a collection
     * @param user The user making the request
     * @param lpath The logical path
     * @return StatBuilder instance that allows for the user to chain optional parameters
     */
    public StatBuilder stat(User user, String lpath) {
        return new StatBuilder(this, user, lpath);
    }

    protected void list(User user, String lpath, boolean recurse, String ticket) throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "list",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0"
        );

        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsList mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsList.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("Entries for '" + lpath + "':\n " + mapped.getEntries());
        } else {
            System.out.println(mapped.getIrods_response());
        }
    }

    public ListBuilder list(User user,String lpath) {
        return new ListBuilder(this, user, lpath);
    }

    /**
     * Helper method to give status code message if JSON displays it as null
     * @param statusCode The status code number
     * @param statusMessage The status message (may be null)
     * @param errorMessage The error message that will be displayed
     * @throws IrodsException
     */
    private void statusCodeMessage(int statusCode, String statusMessage, String errorMessage) throws IrodsException {

        if (statusCode == -170000 && statusMessage == null) {
            throw new IrodsException(errorMessage + ":  NOT_A_COLLECTION");
        } else if (statusCode  == -170000) { // if statusCode does have a message
            throw new IrodsException(errorMessage +  ": " + statusMessage);
        }
    }

}
