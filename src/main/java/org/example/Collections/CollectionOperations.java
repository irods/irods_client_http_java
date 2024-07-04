package org.example.Collections;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.Mapper.Collections.*;
import org.example.Mapper.Collections.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Collections.Serialize.ModifyPermissionsOperations;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
import org.example.User;
import org.example.Util.HttpRequestUtil;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Util.IrodsErrorCodes;

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
    public CollectionsCreate create(String lpath, boolean intermediates) throws IOException, InterruptedException, IrodsException {
        String token = getToken();
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        CollectionsCreate mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);

        String message = mapped.getIrods_response().getStatus_message();

        // checks status code number given by irods_response JSON
        String failMessage = "Failed to created collection: '" + lpath + "'";
        IrodsErrorCodes.statusCodeMessage(mapped.getIrods_response(), failMessage);

        if (mapped.isCreated()) {
            System.out.println("Collection '" + lpath + "' created successfully");
        } else {
            // status code = 0, null message, and isCreated() = false means that it is a duplicate?
            if (message == null) {
                throw new IrodsException(failMessage + " already exists");
            } else {
                throw new IrodsException(failMessage + ": " + message);
            }
        }

        return mapped;
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
    public IrodsResponse remove(String lpath, boolean recurse, boolean noTrash) throws IOException,
            InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                IrodsResponse.class);

        String failMessage = "Failed to remove collection '" + lpath + "'";
        String successMessage = "Collection '" + lpath + "' removed successfully";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }


    /**
     * Returns information about a collection
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param ticket An optional parameter
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
     */
    public CollectionsStat stat(String lpath, String ticket)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsStat mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsStat.class);

        String failMessage = "Failed to retrieve information for '" + lpath + "'";
        String successMessage = "Information for '" + lpath +"' retrieved successfully";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public List<String> list(String lpath, boolean recurse, String ticket)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "list",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0"
        );
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

//        CollectionsList mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
//                CollectionsList.class);
        String form = HttpRequestUtil.createRequestBody(formData);

        // creating the request
        HttpRequest request = HttpRequestUtil.buildRequestGET(baseUrl, token, form);

        // sending request
        HttpResponse<String> response = HttpRequestUtil.sendRequest(client.getClient(), request);

        // parse the JSON
        CollectionsList mapped = HttpRequestUtil.parseResponse(response, CollectionsList.class);

        String failMessage = "Failed to retrieve list for '" + lpath + "'";
        String successMessage = "List for '" + lpath + "' retrieved successfully";
//        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped.getEntries();
//        return null;
    }

    // uses Permission enum for permission parameter
    public IrodsResponse set_permission(String lpath, String entityName, Permission permission,
                                  boolean admin) throws IOException, InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_permission",
                "lpath", lpath,
                "entity-name", entityName,
                "permission", permission.getValue(),
                "admin", admin ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        String failMessage = "Failed to change permissions for '" + lpath + "'";
        String successMessage = "Permission for '" + entityName + "' set";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public IrodsResponse set_inheritance(String lpath, boolean enable,
                                boolean admin) throws IOException, InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_inheritance",
                "lpath", lpath,
                "enable", enable ? "1" : "0",
                "admin", admin ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        String failMessage = "Failed to change inheritance for '" + lpath + "'";
        String successMessage = "Inheritance for '" + lpath + "' " + (enable ? "enabled" : "disabled");
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public CollectionsModifyPermissions modify_permissions(String lpath,
                                                           List<ModifyPermissionsOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

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

        String failMessage = "Failed to modify permission for '" + lpath + "'";
        String successMessage = "Permissions successfully modified";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public CollectionsModifyMetadata modify_metadata(String lpath, List<ModifyMetadataOperations> jsonParam,
                                                               boolean admin)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

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

        String failMessage = "Failed to modify metadata for '" + lpath + "'";
        String successMessage = "Metadata successfully modified";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public IrodsResponse rename(String oldPath, String newPath)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "rename",
                "old-lpath", oldPath,
                "new-lpath", newPath
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);


        String failMessage = "Failed to rename '" + oldPath + "'";
        String successMessage = "'" + oldPath + "' renamed to '" + newPath + "'";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }

    public IrodsResponse touch(String lpath, int mtime, String reference)
            throws IOException, InterruptedException, IrodsException {
        String token = getToken();

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

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        String failMessage = "Failed to update mtime of '" + lpath + "'";
        String successMessage = "Updated mtime successfully";
        handleErrors(mapped.getIrods_response(), failMessage, successMessage);

        return mapped;
    }


    /**
     * Helper method to give status code message if JSON displays it as null
     * @param irodsResponse The JSON that contains the status code and the message
     * @param failMessage The failure message that will be displayed
     * @throws IrodsException because status code is not 0
     */
//    private void statusCodeMessage(Mapped.IrodsResponse irodsResponse, String failMessage) throws IrodsException {
//        int statusCode = irodsResponse.getStatus_code();
//        String statusMessage = irodsResponse.getStatus_message();
//
//        if (statusCode == -170000 && statusMessage == null) {
//            throw new IrodsException(failMessage + ": NOT_A_COLLECTION");
//        } else if (statusCode == -814000 && statusMessage == null) {
//            throw new IrodsException(failMessage + ": CAT_UNKNOWN_COLLECTION");
//        } else if (statusCode == -130000 && statusMessage == null) {
//            throw new IrodsException(failMessage + ": SYS_INVALID_INPUT_PARAM");
//        } else if (statusCode == -154000 && statusMessage == null) {
//            throw new IrodsException(failMessage + ": SYS_INTERNAL_ERR");
//        } else if (statusCode != 0) {
//            throw new IrodsException(failMessage +  ": " + statusMessage);
//        }
//    }

    /**
     * Displays success message or throws an error when request failed
     * @param irodsResponse The JSON that contains the status code and the message
     * @param failMessage The failure message that will be displayed
     * @param successMessage The success message that will be displayed
     * @throws IrodsException when status code is not 0
     */
    private void handleErrors(Mapped.IrodsResponse irodsResponse, String failMessage, String successMessage)
            throws IrodsException {

        IrodsErrorCodes.statusCodeMessage(irodsResponse, failMessage);

        int statusCode = irodsResponse.getStatus_code();
        String message = irodsResponse.getStatus_message();

        if (statusCode == 0) { // success
            System.out.println(successMessage);
        } else { // failure
            throw new IrodsException(failMessage + ": " + message);
        }
    }

    private String getToken() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // read the JSON file
            JsonNode jsonNode = mapper.readTree(new File("token.json"));

            // access the token value
            JsonNode tokenNode = jsonNode.get("token");

            return tokenNode.asText();
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}




