package org.irods.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;
import org.irods.util.UserType;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class provides methods to interact with the users-groups endpoint.
 */
public class UserGroupOperations {
    private final IrodsHttpClient client;
    private String baseUrl;

    /**
     * Constructs a {@code InformationOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public UserGroupOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/users-groups";
    }

    /**
     * Creates a new user. This operation requires rodsadmin or groupadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of user.
     * @param zone Name of zone for user.
     * @param userType An optional parameter to choose the user type. Takes in the {@link UserType} enum. This is
     *                 wrapped in an {@link Optional}. If present, it will be included in the request; if absent,
     *                 the {@code context} parameter will be omitted.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response createUser(String token, String name, String zone, Optional<UserType> userType) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_user");
        formData.put("name", name);
        formData.put("zone", zone);
        userType.ifPresent(val -> formData.put("user-type", val.getValue()));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a user. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of user.
     * @param zone Name of zone for user.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeUser(String token, String name, String zone)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_user");
        formData.put("name", name);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Changes a user's password. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of user.
     * @param zone Name of zone for user.
     * @param newPassword The new password to be set to.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response setPassword(String token, String name, String zone, String newPassword) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_password");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-password", newPassword);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Changes a user's type. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of user.
     * @param zone Name of zone for user.
     * @param newUserType The new user type to be set to. Takes in the {@link UserType} enum.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response setUserType(String token, String name, String zone, UserType newUserType) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_user_type");
        formData.put("name", name);
        formData.put("zone", zone);
        formData.put("new-user-type", newUserType.getValue());

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Creates a new group. This operation requires rodsadmin or groupadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of group.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response createGroup(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a group. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of group.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeGroup(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_group");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adds a user to a group. This operation requires rodsadmin or groupadmin level privileges.
     * <p>
     *     Users of type groupadmin are allowed to execute this operation if at least one of the following conditions
     *     is true:
     * </p>
     * <ul>
     *     <li>The target group is initially empty</li>
     *     <li>The groupadmin user is a member of the group</li>
     * </ul>
     * <p>
     *     Users of type groupadmin are always allowed to add themselves to an empty group. If the target group is not
     *     empty and the groupadmin user isn't a member of the group, execution of this operation will result in an error.
     * </p>
     *
     * @param token The authentication token to use for the request.
     * @param user Name of the user.
     * @param zone Name of zone for the user.
     * @param group Name of the group.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response addToGroup(String token, String user, String zone, String group) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_to_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a user from a group. This operation requires rodsadmin or groupadmin level privileges. If the user is of
     * type groupadmin, they must be a member of the target group to execute this operation.
     *
     * @param token The authentication token to use for the request.
     * @param user Name of the user.
     * @param zone Name of zone for the user.
     * @param group Name of the group.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeFromGroup(String token, String user, String zone, String group) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_from_group");
        formData.put("user", user);
        formData.put("zone", zone);
        formData.put("group", group);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Lists all users in the zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response users(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "users");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Lists all groups in the zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response groups(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "groups");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns whether a user is a member of a group.
     *
     * @param token The authentication token to use for the request.
     * @param group Name of the group.
     * @param user Name of the user.
     * @param zone Name of zone for the user.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response isMemberOfGroup(String token, String group, String user, String zone)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "is_member_of_group");
        formData.put("group", group);
        formData.put("user", user);
        formData.put("zone", zone);

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Returns information about a user or group.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of a user or group.
     * @param zone Name of zone if name represents a user. Not required for groups, thus is wrapped in an
     * {@link Optional}
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response stat(String token, String name, Optional<String> zone) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("name", name);
        zone.ifPresent(val -> formData.put("zone", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adjust multiple AVUs on a user or group. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token to use for the request.
     * @param name Name of the user or group.
     * @param jsonParam A list of {@link ModifyMetadataOperations} specifying the metadata modifications.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
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