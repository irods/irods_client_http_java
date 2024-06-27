package org.example.Collections;

import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.Mapper.Collections.*;
import org.example.Mapper.IrodsResponse;
import org.example.User;
import org.example.Util.HttpRequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public void create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException, IrodsException {
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

        // checks status code number given by irods_response JSON
        statusCodeMessage(mapped.getIrods_response(), "Unable to create ' " + lpath + "' colleciton");

        if (created) {
            System.out.println("Collection '" + lpath + "' created successfully");
        } else {
            throw new IrodsException("Failed to create collection: " + message);
        }
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
    public void remove(User user, String lpath, boolean recurse, boolean noTrash) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );

        NestedIrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                NestedIrodsResponse.class);

        int statusCode = mapped.getIrods_response().getStatus_code();

        // throws errors if found
        statusCodeMessage(mapped.getIrods_response(), "Could not remove collection");

        if (statusCode == 0) {
            System.out.println("'" + lpath +"' removed successfully");
        }
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
    public void stat(User user, String lpath, String ticket) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsStat mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsStat.class);

        System.out.println(mapped);
    }

    public List<String> list(User user, String lpath, boolean recurse, String ticket) throws IOException, InterruptedException {
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
            //System.out.println("Entries for '" + lpath + "':\n " + mapped.getEntries());
            return mapped.getEntries();
        } else {
            System.out.println(mapped.getIrods_response());
            return null;
        }
    }

    // uses Permission enum for permission parameter
    public void set_permission(User user, String lpath, String entityName, Permission permission,
                                  boolean admin) throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_permission",
                "lpath", lpath,
                "entity-name", entityName,
                "permission", permission.getValue(),
                "admin", admin ? "1" : "0"
        );

        NestedIrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), NestedIrodsResponse.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("Permission for '" + entityName + "' set");
        } else {
            System.out.println(mapped);
        }
    }

    public void set_inheritance(User user, String lpath, boolean enable,
                                boolean admin) throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_inheritance",
                "lpath", lpath,
                "enable", enable ? "1" : "0",
                "admin", admin ? "1" : "0"
        );

        NestedIrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), NestedIrodsResponse.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("Inheritance for '" + lpath + "' " + (enable ? "enabled" : "disabled"));
        } else {
            System.out.println(mapped);
        }
    }

    public void modify_permissions(User user, String lpath, List<ModifyPermissionsOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        CollectionsModifyPermissions mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), CollectionsModifyPermissions.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("Permissions successfully modified");
        } else {
            System.out.println(mapped);
        }
    }

    public void modify_metadata(User user, String lpath, List<ModifyMetadataOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        CollectionsModifyMetadata mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), CollectionsModifyMetadata.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("Metadata successfully modified");
        } else {
            System.out.println(mapped);
        }
    }

    public void rename(User user, String oldPath, String newPath) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "rename",
                "old-lpath", oldPath,
                "new-lpath", newPath
        );

        NestedIrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), NestedIrodsResponse.class);

        int statusCode = mapped.getIrods_response().getStatus_code();

        if (statusCode == 0) {
            System.out.println("'" + oldPath + "' renamed to '" + newPath + "'");
        } else {
            statusCodeMessage(mapped.getIrods_response(), "Cannot rename");
        }
    }

    public void touch(User user, String lpath, int mtime, String reference) throws IOException, InterruptedException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        if (mtime != 0) {
            formData.put("seconds-since-epoch", mtime);
        }
        if (reference != null) {
            formData.put("reference", reference);
        }

        NestedIrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), NestedIrodsResponse.class);

        if (mapped.getIrods_response().getStatus_code() == 0) {
            System.out.println("touch request executed correctly");
        } else {
            System.out.println(mapped);
        }

    }


    /**
     * Helper method to give status code message if JSON displays it as null
     * @param errorMessage The error message that will be displayed
     * @throws IrodsException
     */
    //private void statusCodeMessage(int statusCode, String statusMessage, String errorMessage) throws IrodsException {
    private void statusCodeMessage(IrodsResponse irodsResponse, String errorMessage) throws IrodsException {
        int statusCode = irodsResponse.getStatus_code();
        String statusMessage = irodsResponse.getStatus_message();

        if (statusCode == -170000 && statusMessage == null) {
            throw new IrodsException(errorMessage + ":  NOT_A_COLLECTION");
        } else if (statusCode  == -170000) { // if statusCode does have a message
            throw new IrodsException(errorMessage +  ": " + statusMessage);
        }
    }

    /**
     * Handles response from the HTTP request and displays a success message or throws an exception
     */
//    private void handleResponse(boolean success) {
////        int statusCode = response.getStatus_code();
////        String statusMessage = response.getStatus_message();
////        System.out.println(statusCode);
//
//        if (success) {
//            System.out.println("Collection '" + lpath + "' created successfully");
//        } else {
//            throw new IrodsException("Failed to create collection: " + message);
//        }

//    }

}
