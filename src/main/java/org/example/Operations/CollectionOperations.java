package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Properties.Collection.*;
import org.example.Wrapper;
import org.example.Serialize.ModifyMetadataOperations;
import org.example.Serialize.ModifyPermissionsOperations;
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
     * @return A Response object containing the status code and response body (the JSON String). Both of which can be
     * retrieved with a .getStatusCode() and .getBody(), respectively.
     */
     public Response create(String token, String lpath, CollectionsCreateParams prop) {
         // contains parameters for the HTTP request
         Map<Object, Object> formData = new HashMap<>();
         formData.put("op", "create");
         formData.put("lpath", lpath);
         prop.getIntermediates().ifPresent(val -> formData.put("create-intermediates", String.valueOf(val)));

         HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
         return new Response(response.statusCode(), response.body());
     }
     public Response create(String token, String lpath) {
         CollectionsCreateParams prop = new CollectionsCreateParams();
         return this.create(token, lpath, prop);
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
    public Response remove(String token, String lpath, CollectionsRemoveParams prop) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        prop.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
        prop.getNoTrash().ifPresent(val -> formData.put("no-trash", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String lpath) {
        CollectionsRemoveParams prop = new CollectionsRemoveParams();
        return this.remove(token, lpath, prop);
    }

    /**
     * Returns information about a collection
     * @param lpath The logical path for the collection
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
     */
    public Response stat(String token, String lpath, CollectionsStatParams prop) {

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        prop.getTicket().ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String lpath) {
        CollectionsStatParams prop = new CollectionsStatParams();
        return this.stat(token, lpath, prop);
    }

    public Response list(String token, String lpath, CollectionsListParams prop) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "list");
        formData.put("lpath", lpath);
        prop.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
        prop.getTicket().ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response list(String token, String lpath) {
        CollectionsListParams prop = new CollectionsListParams();
        return this.list(token, lpath, prop);
    }

    // uses Permission enum for permission parameter
    public Response set_permission(String token, String lpath, String entityName, Permission permission,
                                   CollectionsSetPermissionParams prop) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_permission");
        formData.put("lpath", lpath);
        formData.put("entity-name", entityName);
        formData.put("permission", permission.getValue());
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_permission(String token, String lpath, String entityName, Permission permission) {
        CollectionsSetPermissionParams prop = new CollectionsSetPermissionParams();
        return this.set_permission(token, lpath, entityName, permission, prop);
    }

    public Response set_inheritance(String token, String lpath, int enable, CollectionsSetInheritanceParams prop) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_inheritance");
        formData.put("lpath", lpath);
        formData.put("enable", String.valueOf(enable));
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_inheritance(String token, String lpath, int enable) {
        CollectionsSetInheritanceParams prop = new CollectionsSetInheritanceParams();
        return this.set_inheritance(token, lpath, enable, prop);
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
                                       CollectionsModifyPermissionParams prop) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_permissions");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam) {
        CollectionsModifyPermissionParams prop = new CollectionsModifyPermissionParams();
        return this.modify_permissions(token, lpath, jsonParam, prop);
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                                               CollectionsModifyMetadataParams prop) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
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
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam) {
        CollectionsModifyMetadataParams prop = new CollectionsModifyMetadataParams();
        return this.modify_metadata(token, lpath, jsonParam, prop);
    }

    public Response rename(String token, String oldPath, String newPath)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rename");
        formData.put("old-lpath", oldPath);
        formData.put("new-lpath", newPath);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response touch(String token, String lpath, CollectionsTouchParams prop)  {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        prop.getSecondsSinceEpoch().ifPresent(val -> formData.put("seconds-since-epoch", String.valueOf(val)));
        prop.getReference().ifPresent(val -> formData.put("reference", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }
    public Response touch(String token, String lpath) {
        CollectionsTouchParams prop = new CollectionsTouchParams();
        return this.touch(token, lpath, prop);
    }
}