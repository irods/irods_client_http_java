package org.irods.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.properties.Resource.ResourceCreateParams;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.util.HttpRequestUtil;
import org.irods.util.ResourceProperty;
import org.irods.util.Response;

import java.net.http.HttpResponse;
import java.util.*;

public class ResourceOperations {
    private final IrodsHttpClient client;
    private String baseUrl;

    public ResourceOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/resources";
    }

    public Response create(String token, String name, String type, ResourceCreateParams params) {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("name", name);
        formData.put("type", type);
        if (null != params) {
            params.getHost().ifPresent(val -> formData.put("host", val));
            params.getVaultPath().ifPresent(val -> formData.put("vault-path", val));
            params.getContext().ifPresent(val -> formData.put("context", val));

        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String name)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify(String token, String name, ResourceProperty property, String value) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify");
        formData.put("name", name);
        formData.put("property", property.getValue());
        formData.put("value", value);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response addChild(String token, String parentName, String childName, Optional<String> context) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);
        context.ifPresent(val -> formData.put("context", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response removeChild(String token, String parentName, String childName) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response rebalance(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rebalance");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String name)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modifyMetadata(String token, String name, List<ModifyMetadataOperations> jsonParam) {
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
                "name", name,
                "operations", operationsJson
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}