package org.example.Collections;

import org.example.IrodsClient;
import org.example.IrodsException;
import org.example.Mapper.Collections.*;
import org.example.Mapper.Collections.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Collections.Serialize.ModifyPermissionsOperations;
import org.example.Mapper.IrodsResponse;
import org.example.Mapper.Mapped;
import org.example.User;
import org.example.Util.HttpRequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class for all the Collections Operations
 */
public class CollectionOperations {

    private final IrodsClient client;
    private String baseUrl;

    public CollectionOperations(IrodsClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/collections";
    }

    /**
     * Creates a new collection.
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param intermediates Whether to create intermediate directories. Optional parameter
     * @throws IOException
     * @throws InterruptedException
     */
    public CollectionsCreate create(User user, String lpath, boolean intermediates) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "create",
                "lpath", lpath,
                "create-intermediates", intermediates ? "1" : "0"
        );

        CollectionsCreate mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                CollectionsCreate.class);

        String message = mapped.getIrods_response().getStatus_message();

        // checks status code number given by irods_response JSON
        statusCodeMessage(mapped.getIrods_response(), "Failed to created collection: '" + lpath + "'");

        if (mapped.isCreated()) {
            System.out.println("Collection '" + lpath + "' created successfully");
        } else {
            // status code = 0, null message, and isCreated() = false means that it is a duplicate?
            if (message == null) {
                throw new IrodsException("Failed to created collection: '" + lpath + "' already exists");
            } else {
                throw new IrodsException("Failed to create collection '" + lpath + "': " + message);
            }
        }

        return mapped;
    }

    /**
     * Removes a collection
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param recurse If true, contents of the collection will be removed. Optional parameter
     * @param noTrash If true, collection is permanently removed. Optional parameter
     * @throws IOException
     * @throws InterruptedException
     */
    public IrodsResponse remove(User user, String lpath, boolean recurse, boolean noTrash) throws IOException,
            InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "remove",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0",
                "no-trash", noTrash ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient(),
                IrodsResponse.class);

        // throws errors if found
        statusCodeMessage(mapped.getIrods_response(), "Failed to remove collection: '" + lpath + "'");

        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Collection '" + lpath +"' removed successfully");
        } else {
            throw new IrodsException("Failed to remove collection '" + lpath + "': " + message);
        }

        return mapped;
    }


    /**
     * Returns information about a collection
     * Protected, so it can only be accessed from this package. Enforces use of builder
     * @param user The user making the request
     * @param lpath The logical path for the collection
     * @param ticket An optional parameter
     * @throws IOException
     * @throws InterruptedException
     * @throws IrodsException
     */
    public CollectionsStat stat(User user, String lpath, String ticket) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsStat mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsStat.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to retrieve information for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Information for '" + lpath +"' retrieved successfully");
        } else {
            throw new IrodsException("Failed to retrieve information for '" + lpath + "': " + message);
        }
        return mapped;
    }

    public List<String> list(User user, String lpath, boolean recurse, String ticket) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "list",
                "lpath", lpath,
                "recurse", recurse ? "1" : "0"
        );

        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        CollectionsList mapped = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient(),
                CollectionsList.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to retrieve list for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("List for '" + lpath + "' retrieved successfully");
            return mapped.getEntries();
        } else {
            throw new IrodsException("Failed to retrieve list for '" + lpath + "': " + message);
        }
    }

    // uses Permission enum for permission parameter
    public IrodsResponse set_permission(User user, String lpath, String entityName, Permission permission,
                                  boolean admin) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_permission",
                "lpath", lpath,
                "entity-name", entityName,
                "permission", permission.getValue(),
                "admin", admin ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to change permissions for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Permission for '" + entityName + "' set");
            return mapped;
        } else {
            throw new IrodsException("Failed to change permissions for '" + lpath + "': " + message);
        }

    }

    public IrodsResponse set_inheritance(User user, String lpath, boolean enable,
                                boolean admin) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_inheritance",
                "lpath", lpath,
                "enable", enable ? "1" : "0",
                "admin", admin ? "1" : "0"
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to change inheritance for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Inheritance for '" + lpath + "' " + (enable ? "enabled" : "disabled"));
            return mapped;
        } else {
            throw new IrodsException("Failed to change inheritance for '" + lpath + "': " + message);
        }
    }

    public CollectionsModifyPermissions modify_permissions(User user, String lpath, List<ModifyPermissionsOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        CollectionsModifyPermissions mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), CollectionsModifyPermissions.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to modify permission for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Permissions successfully modified");
            return mapped;
        } else {
            throw new IrodsException("Failed to modify permission for '" + lpath + "'" + message);
        }
    }

    public CollectionsModifyMetadata CollectionsModifyMetadata(User user, String lpath, List<ModifyMetadataOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        CollectionsModifyMetadata mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), CollectionsModifyMetadata.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to modify metadata for '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Metadata successfully modified");
            return mapped;
        } else {
            throw new IrodsException("Failed to modify metadata for '" + lpath + "': " + message);
        }
    }

    public IrodsResponse rename(User user, String oldPath, String newPath) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "rename",
                "old-lpath", oldPath,
                "new-lpath", newPath
        );

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to rename '" + oldPath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("'" + oldPath + "' renamed to '" + newPath + "'");
            return mapped;
        } else {
            throw new IrodsException("Failed to rename '" + oldPath + "': " + message);
        }
    }

    public IrodsResponse touch(User user, String lpath, int mtime, String reference) throws IOException, InterruptedException, IrodsException {
        String token = user.getAuthToken();

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        if (mtime != 0) {
            formData.put("seconds-since-epoch", mtime);
        }
        if (reference != null) {
            formData.put("reference", reference);
        }

        IrodsResponse mapped = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient(), IrodsResponse.class);

        statusCodeMessage(mapped.getIrods_response(), "Failed to update mtime of '" + lpath + "'");
        int statusCode = mapped.getIrods_response().getStatus_code();
        String message = mapped.getIrods_response().getStatus_message();
        if (statusCode == 0) {
            System.out.println("Updated mtime successfully");
            return mapped;
        } else {
            throw new IrodsException("Failed to update mtime of '" + lpath + "': " + message);
        }
    }


    /**
     * Helper method to give status code message if JSON displays it as null
     * @param errorMessage The error message that will be displayed
     * @throws IrodsException
     */
    private void statusCodeMessage(Mapped.IrodsResponse irodsResponse, String errorMessage) throws IrodsException {
        int statusCode = irodsResponse.getStatus_code();
        String statusMessage = irodsResponse.getStatus_message();

        if (statusCode == -170000 && statusMessage == null) {
            throw new IrodsException(errorMessage + ":  NOT_A_COLLECTION");
        } else if (statusCode  == -170000) { // if statusCode does have a message
            throw new IrodsException(errorMessage +  ": " + statusMessage);
        }
    }
}




