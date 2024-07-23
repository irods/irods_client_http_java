package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Properties.Resource.ResourceAddChildParams;
import org.example.Properties.Resource.ResourceCreateParams;
import org.example.Wrapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceOperations {
    private final Wrapper client;
    private String baseUrl;

    public ResourceOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/resources";
    }

    public Response create(String token, String name, String type, ResourceCreateParams prop) {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("name", name);
        formData.put("type", type);
        prop.getHost().ifPresent(val -> formData.put("host", val));
        prop.getVaultPath().ifPresent(val -> formData.put("vault-path", val));
        prop.getContext().ifPresent(val -> formData.put("context", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response create(String token, String name, String type) {
        ResourceCreateParams prop = new ResourceCreateParams();
        return this.create(token, name, type, prop);
    }

    public Response remove(String token, String name)  {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify(String token, String name, String property, String value) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify");
        formData.put("name", name);
        formData.put("property", property);
        formData.put("value", value);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response add_child(String token, String parentName, String childName, ResourceAddChildParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);
        prop.getContext().ifPresent(val -> formData.put("context", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response add_child(String token, String parentName, String childName) {
        ResourceAddChildParams prop = new ResourceAddChildParams();
        return this.add_child(token, parentName, childName, prop);
    }

    public Response remove_child(String token, String parentName, String childName) {

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

    public Response modify_metadata(String token, String name, List<ModifyMetadataOperations> jsonParam) {
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
