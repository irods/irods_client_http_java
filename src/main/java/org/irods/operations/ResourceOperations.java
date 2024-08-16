package org.irods.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.properties.resource.ResourceCreateParams;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.util.HttpRequestUtil;
import org.irods.util.ResourceProperty;
import org.irods.util.Response;

import java.net.http.HttpResponse;
import java.util.*;

/**
 * Class provides methods to interact with the resources endpoint.
 */
public class ResourceOperations {
    private final IrodsHttpClient client;
    private String baseUrl;

    /**
     * Constructs a {@code ResourceOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public ResourceOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/resources";
    }

    /**
     * Creates a new resource. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param name Name of the resource.
     * @param type Type of the resource.
     * @param params An instance of the {@link ResourceCreateParams} containing optional parameters.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
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

    /**
     * Removes a resource. This operation requires rodsadmin level privileges.
     * @param token The authentication token for the iRODS user.
     * @param name Name of the resource.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response remove(String token, String name)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Modifies a single property of a resource. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param name Name of the resource.
     * @param property Name of the property to modify. Uses the {@link ResourceProperty} enum.
     * @param value The new value of the property.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response modify(String token, String name, ResourceProperty property, String value) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify");
        formData.put("name", name);
        formData.put("property", property.getValue());
        formData.put("value", value);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Creates a parent-child relationship between two resources. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param parentName The name of the parent resource.
     * @param childName The name of the child resource.
     * @param context An optional parameter to add additional information to a zone. This is wrapped in an
     * {@link Optional}. If present, it will be included in the request; if absent, the {@code context} parameter will be omitted.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response addChild(String token, String parentName, String childName, Optional<String> context) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);
        context.ifPresent(val -> formData.put("context", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes the parent-child relationship between two resources. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param parentName The name of the parent resource.
     * @param childName The name of the child resource.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeChild(String token, String parentName, String childName) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_child");
        formData.put("parent-name", parentName);
        formData.put("child-name", childName);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Rebalances a resource hierarchy. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param name The name of the resource.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response rebalance(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rebalance");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns information about a resource.
     *
     * @param token The authentication token for the iRODS user.
     * @param name The name of the resource.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response stat(String token, String name)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adjust multiple AVUs on a resource. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name The name of the resource.
     * @param jsonParam A list of {@link ModifyMetadataOperations} specifying the metadata modifications.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
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