package org.example.Operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Wrapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.IrodsException;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserGroupOperations {
    private final Wrapper client;
    private String baseUrl;


    public UserGroupOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/users-groups";
    }

    public Response create_user(String token, String name, String zone, String userType)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_user");
        formData.put("name", name);
        formData.put("zone", zone);
        if (userType != null) {
            formData.put("user-type", userType);
        } else {
            formData.put("user-type", "rodsuser"); // default
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_user(String token, String name, String zone)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_user");
        formData.put("name", name);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response set_password(String token, String name, String zone, String newPassword)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_password");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-password", newPassword);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response set_user_type(String token, String name, String zone, String newUserType)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_user_type");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-user-type", newUserType);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response create_group(String token, String name) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_group(String token, String name) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response add_to_group(String token, String user, String zone, String group)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_to_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_from_group(String token, String user, String zone, String group)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_from_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response users(String token) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "users");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response groups(String token) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "groups");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response is_member_of_group(String token, String group, String user, String zone)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "is_member_of_group");
        formData.put("group", group);
        formData.put("user", user);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String name, String zone) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);
        if (zone != null) {
            formData.put("zone", zone);
        }

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
