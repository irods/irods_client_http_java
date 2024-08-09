package org.irods.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.irods.IrodsHttpClient;
import org.irods.properties.Collection.*;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.serialize.ModifyPermissionsOperations;
import org.irods.util.*;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for all the Collections Operations
 */
public class CollectionOperations {

    private final IrodsHttpClient client;
    private String baseUrl;

    public CollectionOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/collections";
    }

    public Response create(String token, String lpath, OptionalInt createIntermediates) {
         // contains parameters for the HTTP request
         Map<Object, Object> formData = new HashMap<>();
         formData.put("op", "create");
         formData.put("lpath", lpath);
         createIntermediates.ifPresent(val -> formData.put("create-intermediates", String.valueOf(val)));


         HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
         return new Response(response.statusCode(), response.body());
     }

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
     * Returns information about a collection
     * @param lpath The logical path for the collection
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
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

    public Response modifyMetadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                   OptionalInt admin) {
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
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
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