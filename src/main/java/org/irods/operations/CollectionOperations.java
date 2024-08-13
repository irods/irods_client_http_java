package org.irods.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.irods.IrodsHttpClient;
import org.irods.properties.Collection.*;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.serialize.ModifyPermissionsOperations;
import org.irods.util.*;
import java.net.http.HttpResponse;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class provides methods to interact with the collections endpoint.
 */
public class CollectionOperations {

    private final IrodsHttpClient client;
    private final String baseUrl;

    /**
     * Constructs a {@code CollectionOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public CollectionOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/collections";
    }

    /**
     * Creates a new collection at the specified logical path.
     *
     * @param token The authentication token for the iRODS user.
     * @param lpath The logical path for the collection.
     * @param createIntermediates An optional parameter that specifies whether intermediate collections should be
     *                            created if they do not exist. 0 or 1. Defaults to 0. This is wrapped in an {@link OptionalInt}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response create(String token, String lpath, OptionalInt createIntermediates) {
         // contains parameters for the HTTP request
         Map<Object, Object> formData = new HashMap<>();
         formData.put("op", "create");
         formData.put("lpath", lpath);
         createIntermediates.ifPresent(val -> formData.put("create-intermediates", String.valueOf(val)));


         HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
         return new Response(response.statusCode(), response.body());
     }

    /**
     * Removes a collection at the specified logical path.
     *
     * @param token The authentication token for the iRODS user.
     * @param lpath The logical path for the collection.
     * @param params An instance of the {@link CollectionsRemoveParams} containing optional parameters such as recurse
     *               and no-trash options.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response remove(String token, String lpath, CollectionsRemoveParams params) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
            params.getNoTrash().ifPresent(val -> formData.put("no-trash", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns information about a collection.
     *
     * @param token The authentication token for the iRODS user.
     * @param lpath The logical path for the collection.
     * @param ticket An optional parameter specifying a ticket for access. If {@code ticket} is a valid string, it will
     *               be enabled before carrying out the operation. This is wrapped in an {@link Optional}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response stat(String token, String lpath, Optional<String> ticket) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        ticket.ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns the contents of a collection.
     *
     * @param token The authentication token for the iRODS user.
     * @param lpath The logical path for the collection.
     * @param params An instance of {@link CollectionsListParams} containing additional parameters such as recurse
     *               and ticket options.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response list(String token, String lpath, CollectionsListParams params) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "list");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
            params.getTicket().ifPresent(val -> formData.put("ticket", val));

        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    // uses Permission enum for permission parameter

    /**
     * Sets the permission of a user or group on a collection.
     *
     * @param token The authentication token for the iRODS user.
     * @param lpath The logical path for the collection.
     * @param entityName The name of a user or group.
     * @param permission The {@link Permission} level to be set.
     * @param admin An optional parameter indicating whether the operation is executed as rodsadmin. 0 or 1.
     *              Defaults to 0. This is wrapped in an {@link OptionalInt}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response setPermission(String token, String lpath, String entityName, Permission permission,
                                  OptionalInt admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_permission");
        formData.put("lpath", lpath);
        formData.put("entity-name", entityName);
        formData.put("permission", permission.getValue());
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    /**
     * Enable or disable inheritance on a collection.
     *
     * @param token The authentication token to use for the request.
     * @param lpath The logical path of the collection.
     * @param enable Indicates whether inheritance should be disabled or enabled. 0 or 1.
     * @param admin An optional parameter indicating whether the operation is executed as rodsadmin. 0 or 1.
     *              Defaults to 0. This is wrapped in an {@link OptionalInt}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response setInheritance(String token, String lpath, int enable, OptionalInt admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_inheritance");
        formData.put("lpath", lpath);
        formData.put("enable", String.valueOf(enable));
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adjust permissions for multiple users and groups on a collection atomically.
     *
     * @param token The authentication token to use for the request.
     * @param lpath The logical path of the collection.
     * @param jsonParam A list of {@link ModifyPermissionsOperations} specifying the permissions to modify.
     * @param admin An optional parameter indicating whether the operation is executed as rodsadmin. 0 or 1.
     *              Defaults to 0. This is wrapped in an {@link OptionalInt}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     * @throws JsonProcessingException If an error occurs during JSON serialization of the operations.
     */
    public Response modifyPermissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
                                      OptionalInt admin) throws JsonProcessingException {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_permissions");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adjust multiple AVUs on a collection.
     *
     * @param token The authentication token to use for the request.
     * @param lpath The logical path of the collection.
     * @param jsonParam A list of {@link ModifyMetadataOperations} specifying the metadata modifications.
     * @param admin An optional parameter indicating whether the operation is executed as rodsadmin. 0 or 1.
     *              Defaults to 0. This is wrapped in an {@link OptionalInt}.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response modifyMetadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                   OptionalInt admin) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_metadata");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Renames or moves a collection.
     *
     * @param token The authentication token to use for the request.
     * @param oldPath The current logical path of the collection.
     * @param newPath The new logical path for the collection.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response rename(String token, String oldPath, String newPath)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rename");
        formData.put("old-lpath", oldPath);
        formData.put("new-lpath", newPath);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Updates the mtime of an existing collection.
     *
     * @param token The authentication token to use for the request.
     * @param lpath The logical path of the collection.
     * @param params An instance of the {@link CollectionsTouchParams} containing optional parameters.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response touch(String token, String lpath, CollectionsTouchParams params)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getSecondsSinceEpoch().ifPresent(val -> formData.put("seconds-since-epoch", String.valueOf(val)));
            params.getReference().ifPresent(val -> formData.put("reference", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }
}