package org.irods.operations;

import org.irods.properties.Collection.CollectionsTouchParams;
import org.irods.properties.Zone.ZoneAddParams;
import org.irods.IrodsHttpClient;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;
import org.irods.util.ZoneProperty;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides methods to interact with the information endpoint.
 */
public class ZoneOperations {
    private final IrodsHttpClient client;
    private String baseUrl;


    /**
     * Constructs a {@code ZoneOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public ZoneOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/zones";
    }

    /**
     * Adds a remote zone to the local zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name The name of the remote zone to add.
     * @param params An instance of the {@link ZoneAddParams} containing optional parameters.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response add(String token, String name, ZoneAddParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add");
        formData.put("name", name);
        if (null != params) {
            params.getConnectionInfo().ifPresent(val -> formData.put("connection-info", val));
            params.getComment().ifPresent(val -> formData.put("comment", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a remote zone from the local zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name The name of the remote zone to remove.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response remove(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Modifies properties of a remote zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name The name of the remote zone to modify.
     * @param zoneProperty The property to modify.
     * @param value The new value of the property. Takes in the {@link ZoneProperty} enum.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response modify(String token, String name, ZoneProperty zoneProperty, String value) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify");
        formData.put("name", name);
        formData.put("property", zoneProperty.getValue());
        formData.put("value", value);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns information about the iRODS zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response report(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "report");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}