package org.example.Operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Wrapper;
import org.example.Serialize.ModifyMetadataOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.IrodsException;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserGroupOperations {
    private final Wrapper client;
    private String baseUrl;

    public UserGroupOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/users-groups";
    }

    public Response createUser(String token, String name, String zone, Optional<String> userType) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_user");
        formData.put("name", name);
        formData.put("zone", zone);
        userType.ifPresent(val -> formData.put("user-type", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response removeUser(String token, String name, String zone)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_user");
        formData.put("name", name);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response setPassword(String token, String name, String zone, String newPassword) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_password");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-password", newPassword);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response setUserType(String token, String name, String zone, String newUserType) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_user_type");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-user-type", newUserType);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response createGroup(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response removeGroup(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response addToGroup(String token, String user, String zone, String group) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_to_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response removeFromGroup(String token, String user, String zone, String group) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_from_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response users(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "users");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response groups(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "groups");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response isMemberOfGroup(String token, String group, String user, String zone)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "is_member_of_group");
        formData.put("group", group);
        formData.put("user", user);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String name, Optional<String> zone) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);
        zone.ifPresent(val -> formData.put("zone", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modifyMetadata(String token, String name, List<ModifyMetadataOperations> jsonParam)
            throws IOException {
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