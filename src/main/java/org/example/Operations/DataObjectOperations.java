package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Properties.ModifyReplicaProperties;
import org.example.Wrapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Serialize.ModifyPermissionsOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Permission;
import org.example.Util.Response;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class DataObjectOperations {
    private final Wrapper client;
    private static String baseUrl;


    public DataObjectOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/data-objects";
    }

    public Response touch(String token, String lpath, boolean noCreate, int replicaNum, String leafResource,
                          int secondsSinceEpoch, String reference)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        formData.put("no-create", noCreate ? "1" : "0");
        if (replicaNum != -1) {
            formData.put("replica-number", String.valueOf(replicaNum));
        }
        if (leafResource != null) {
            formData.put("leaf-resource", leafResource);
        }
        if (secondsSinceEpoch != -1) {
            formData.put("seconds-since-epoch", String.valueOf(secondsSinceEpoch));
        }
        if (reference != null) {
            formData.put("reference", reference);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String lpath, boolean catalogOnly, boolean noTrash, boolean admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        formData.put("catalog-only", catalogOnly ? "1" : "0");
        formData.put("no-trash", noTrash ? "1" : "0");
        formData.put("admin", admin ? "1" : "0");

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response calculate_checksum(String token, String lpath, String resource, int replicaNum, boolean force,
                                       boolean all, boolean admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "calculate_checksum");
        formData.put("lpath", lpath);
        if (resource != null) {
            formData.put("resource", resource);
        }
        if (replicaNum != -1) {
            formData.put("replica-number", String.valueOf(replicaNum));
        }
        formData.put("force", force ? "1" : "0");
        formData.put("all", all ? "1" : "0");
        formData.put("admin", admin ? "1" : "0");


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response verify_checksum(String token, String lpath, String resource, int replicaNum,
                                    boolean computeChecksums, boolean admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "verify_checksum");
        formData.put("lpath", lpath);
        if (resource != null) {
            formData.put("resource", resource);
        }
        if (replicaNum != -1) {
            formData.put("replica-number", String.valueOf(replicaNum));
        }
        formData.put("compute-checksums", computeChecksums ? "1" : "0");
        formData.put("admin", admin ? "1" : "0");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String lpath, String ticket) {

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        if (ticket != null) {
            formData.put("ticket", ticket);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response rename(String token, String oldPath, String newPath) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "rename",
                "old-lpath", oldPath,
                "new-lpath", newPath
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response copy(String token, String srcLpath, String dstLpath, String srcResource, String dstResource,
                         boolean overwrite) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "copy");
        formData.put("src-lpath", srcLpath);
        formData.put("src-lpath", dstLpath);
        if (srcResource != null) {
            formData.put("src-resource", srcResource);
        }
        if (dstResource != null) {
            formData.put("dst-resource", dstResource);
        }
        formData.put("overwrite", overwrite ? "1" : "0");

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response replicate(String token, String lpath, String srcResource, String dstResource, boolean admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "replicate");
        formData.put("lpath", lpath);
        if (srcResource != null) {
            formData.put("src-resource", srcResource);
        }
        if (dstResource != null) {
            formData.put("dst-resource", dstResource);
        }
        formData.put("overwrite", admin ? "1" : "0");

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response trim(String token, String lpath, int replicaNum, boolean catalogOnly, boolean admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "trim");
        formData.put("lpath", lpath);
        if (replicaNum != -1) {
            formData.put("replica-number", String.valueOf(replicaNum));
        }
        formData.put("catalog-only", catalogOnly ? "1" : "0");
        formData.put("overwrite", admin ? "1" : "0");

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response register(String token, String lpath, String ppath, String resource, boolean asAdditionalReplica,
                             int dataSize, String checksum) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "register");
        formData.put("lpath", lpath);
        formData.put("ppath", ppath);
        formData.put("resource", resource);
        formData.put("as-additional-replica", asAdditionalReplica ? "1" : "0");
        if (dataSize != -1) {
            formData.put("data-size", String.valueOf(dataSize));
        }
        if (checksum != null) {
            formData.put("checksum", checksum);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response read(String token, String lpath, int offset, int count, String ticket) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "read");
        formData.put("lpath", lpath);
        if (offset != -1) {
            formData.put("offset", String.valueOf(offset));
        } else {
            formData.put("offset", "0");
        }
        if (count != -1) {
            formData.put("count", String.valueOf(count));
        }
        if (ticket != null) {
            formData.put("ticket", ticket);
        }


        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response write(String token, String lpath, String resource, int offset, boolean truncate, boolean append,
                          byte[] bytes, String parallelWriteHandle, int streamIndex) {

        String boundary = "----http_api_write_data_objects_operations----";

        // add parameters
        StringBuilder sb = new StringBuilder();
        addFormData(sb, boundary, "op", "write");
        addFormData(sb, boundary, "lpath", lpath);
        if (resource != null) { // optional
            addFormData(sb, boundary, "resource", resource);
        }
        if (offset != -1) { // optional
            addFormData(sb, boundary, "offset", String.valueOf(offset));
        } else { // defaults to 0
            addFormData(sb, boundary, "offset", "0");
        }
        addFormData(sb, boundary, "truncate", truncate ? "1" : "0");
        addFormData(sb, boundary, "append", append ? "1" : "0");
        if (parallelWriteHandle != null) { // optional
            addFormData(sb, boundary, "parallel-write-handle", parallelWriteHandle);
        }
        if (streamIndex != -1) { // optional
            addFormData(sb, boundary, "stream-index", String.valueOf(streamIndex));
        }

        // byte data
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"bytes\"\r\n");
        sb.append("Content-Type: application/octet-stream\r\n\r\n");

        byte[] header = sb.toString().getBytes();
        byte[] footer = ("\r\n--" + boundary + "--\r\n").getBytes();

        // represents entire body of the multipart/fom-data request
        byte[] multipartBody = new byte[header.length + bytes.length + footer.length];
        System.arraycopy(header, 0, multipartBody, 0, header.length);
        System.arraycopy(bytes, 0, multipartBody, header.length, bytes.length);
        System.arraycopy(footer, 0, multipartBody, header.length + bytes.length, footer.length);

        // create request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();

        // send response
        HttpResponse<String> response = null;
        try {
            response = client.getClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Response(response.statusCode(), response.body());
    }


    /**
     * Helper method for write() to add form data
     */
    private static void addFormData(StringBuilder sb, String boundary, String name, String value) {
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        sb.append(value + "\r\n");
    }

    public Response parallel_write_init(String token, String lpath, int streamCount, boolean truncate, boolean append,
                                        String ticket) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_init");
        formData.put("lpath", lpath);
        formData.put("stream-count", String.valueOf(streamCount));
        formData.put("truncate", truncate ? "1" : "0");
        formData.put("append", append ? "1" : "0");
        if (ticket != null) {
            formData.put("ticket", ticket);
        }


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response parallel_write_shutdown(String token, String parallelWriteHandle) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_shutdown");
        formData.put("parallel-write-handle", parallelWriteHandle);


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                    boolean admin) {
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
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_permission(String token, String lpath, String entityName, Permission permission,
                                   boolean admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = Map.of(
                "op", "set_permission",
                "lpath", lpath,
                "entity-name", entityName,
                "permission", permission.getValue(),
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
                                       boolean admin) {
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
                "op", "modify_permissions",
                "lpath", lpath,
                "operations", operationsJson,
                "admin", admin ? "1" : "0"
        );

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());

    }

    public Response modify_replica(String token, String lpath, ModifyReplicaProperties prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_replica");
        formData.put("lpath", lpath);
        prop.getResourceHierarchy().ifPresent(val -> formData.put("resource-hierarchy", val));
        prop.getReplicaNum().ifPresent(val -> formData.put("replica-number", val));
        prop.getNewDataChecsum().ifPresent(val -> formData.put("new-data-checksum", val));
        prop.getNewDataComments().ifPresent(val -> formData.put("new-data-comments", val));

        prop.getNewDataCreateTime().ifPresent(val -> formData.put("new-data-create-time", val));
        prop.getNewDataExpiry().ifPresent(val -> formData.put("new-data-expiry", val));
        prop.getNewDataMode().ifPresent(val -> formData.put("new-data-mode", val));
        prop.getNewDataModifyTime().ifPresent(val -> formData.put("new-data-modify-time", val));

        prop.getNewDataPath().ifPresent(val -> formData.put("new-data-path", val));
        prop.getNewDataReplicaNum().ifPresent(val -> formData.put("new-data-replica-number", val));
        prop.getNewDataRepliaStatus().ifPresent(val -> formData.put("new-data-replica-status", val));
        prop.getNewDataResourceId().ifPresent(val -> formData.put("new-data-resource-id", val));

        prop.getNewDataSize().ifPresent(val -> formData.put("new-data-size", val));
        prop.getNewDataStatus().ifPresent(val -> formData.put("new-data-status", val));
        prop.getNewDataTypeName().ifPresent(val -> formData.put("new-data-type-name", val));
        prop.getNewDataVersion().ifPresent(val -> formData.put("new-data-version", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());

    }

}
