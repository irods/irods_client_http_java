package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Wrapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Serialize.ModifyPermissionsOperations;
import org.example.Mapper.Mapped;
import org.example.Util.*;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for all the Collections Operations
 */
public class CollectionOperations {

    private final Wrapper client;
    private String baseUrl;


    public CollectionOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/collections";
    }

    /**
     * Creates a new collection.
     * @param token The token used for the authenticated request
     * @param lpath The logical path for the collection
     * @param intermediates Whether to create intermediate directories. Optional parameter
     * @return A Response object containing the status code and response body (the JSON String). Both of which can be
     * retrieved with a .getStatusCode() and .getBody(), respectively.
     */
    public Response create(String token, String lpath, boolean intermediates) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a collection
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param lpath The logical path for the collection
     * @param recurse If true, contents of the collection will be removed. Optional parameter
     * @param noTrash If true, collection is permanently removed. Optional parameter
     * @throws IOException
     * @throws InterruptedException
     */
    public Response remove(String token, String lpath, boolean recurse, boolean noTrash) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());

    }


    /**
     * Returns information about a collection
     * @param lpath The logical path for the collection
     * @param ticket An optional parameter
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
     */
    public Response stat(String token, String lpath, String ticket) {

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response list(String token, String lpath, boolean recurse, String ticket) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "list",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0"
        );
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    // uses Permission enum for permission parameter
    public Response set_permission(String token, String lpath, String entityName,
                                   Permission permission, boolean admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_permission",
                "lpath", lpath,
                "entity-name", entityName,
                "permission", permission.getValue(),
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_inheritance(String token, String lpath, boolean enable, boolean admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_inheritance",
                "lpath", lpath,
                "enable", enable ? "1" : "0",
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_permissions(String token, String lpath,
                                       List<ModifyPermissionsOperations> jsonParam, boolean admin) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());

    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                                               boolean admin) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_metadata",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());

    }

    public Response rename(String token, String oldPath, String newPath)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "rename",
                "old-lpath", oldPath,
                "new-lpath", newPath
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response touch(String token, String lpath, int secondsSinceEpoch, String reference)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        if (secondsSinceEpoch != -1) {
            formData.put("seconds-since-epoch", String.valueOf(secondsSinceEpoch));
        }
        if (reference != null) {
            formData.put("reference", reference);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
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

//    private String getToken() {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            // read the JSON file
//            JsonNode jsonNode = mapper.readTree(new File("token.json"));
//
//            // access the token value
//            JsonNode tokenNode = jsonNode.get("token");
//
//            return tokenNode.asText();
//        } catch (IOException e) {
//            System.err.println("Error reading JSON file: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }
}




