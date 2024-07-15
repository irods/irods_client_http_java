package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.IrodsException;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceOperations {
    private final Manager client;
    private String baseUrl;


    public ResourceOperations(Manager client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/resources";
    }

    public Response create(String token, String name, String type, String host, String vaultPath, String context)
            throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("name", name);
        formData.put("type", type);
        if (host != null) {
            formData.put("host", host);
        }
        if (vaultPath != null) {
            formData.put("vault-path", vaultPath);
        }
        if (context != null) {
            formData.put("context", context);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String name) throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response add_child(String token, String parentName, String childName, String context)
            throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);
        if (context != null) {
            formData.put("context", context);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_child(String token, String parentName, String childName)
            throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response rebalance(String token, String name) throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rebalance");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String name) throws IOException, InterruptedException {

        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String name, List<ModifyMetadataOperations> jsonParam)
            throws IOException, InterruptedException, IrodsException {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

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
