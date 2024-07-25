package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Properties.Collection.*;
import org.example.Wrapper;
import org.example.Serialize.ModifyMetadataOperations;
import org.example.Serialize.ModifyPermissionsOperations;
import org.example.Util.*;

import java.io.IOException;

import java.net.http.HttpResponse;
import java.util.*;

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

    public Response create(String token, String lpath, OptionalInt createIntermediates) {
         // contains parameters for the HTTP request
         Map<Object, Object> formData = new HashMap<>();
         formData.put("op", "create");
         formData.put("lpath", lpath);
         if (createIntermediates.isPresent()) {
             formData.put("create-intermediates", String.valueOf(createIntermediates));
         }

         HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
         return new Response(response.statusCode(), response.body());
     }

    public Response remove(String token, String lpath, CollectionsRemoveParams params) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        if (params != null) {
            params.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
            params.getNoTrash().ifPresent(val -> formData.put("no-trash", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
    public Response stat(String token, String lpath, Optional<String> ticket) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket.isPresent()) {
            formData.put("ticket", ticket);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response list(String token, String lpath, CollectionsListParams params) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "list");
        formData.put("lpath", lpath);
        if (params != null) {
            params.getRecurse().ifPresent(val -> formData.put("recurse", String.valueOf(val)));
            params.getTicket().ifPresent(val -> formData.put("ticket", val));

        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    // uses Permission enum for permission parameter
    public Response set_permission(String token, String lpath, String entityName, Permission permission,
                                   OptionalInt admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_permission");
        formData.put("lpath", lpath);
        formData.put("entity-name", entityName);
        formData.put("permission", permission.getValue());
        if (admin.isPresent()) {
            formData.put("admin", String.valueOf(admin));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_inheritance(String token, String lpath, int enable, OptionalInt admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_inheritance");
        formData.put("lpath", lpath);
        formData.put("enable", String.valueOf(enable));
        if (admin.isPresent()) {
            formData.put("admin", String.valueOf(admin));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
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
        formData.put("op", "modify_permissions");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        if (admin.isPresent()) {
            formData.put("admin", String.valueOf(admin));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
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
        if (admin.isPresent()) {
            formData.put("admin", String.valueOf(admin));
        }

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
        if (params != null) {
            params.getSecondsSinceEpoch().ifPresent(val -> formData.put("seconds-since-epoch", String.valueOf(val)));
            params.getReference().ifPresent(val -> formData.put("reference", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }
}