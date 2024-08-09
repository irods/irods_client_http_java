package org.irods.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.properties.DataObject.*;
import org.irods.properties.DataObject.DataObjectModifyReplicaParams;
import org.irods.serialize.ModifyMetadataOperations;
import org.irods.serialize.ModifyPermissionsOperations;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;

import java.io.*;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class DataObjectOperations {
    private final IrodsHttpClient client;
    private static String baseUrl;


    public DataObjectOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/data-objects";
    }

    public Response touch(String token, String lpath, DataObjectTouchParams params)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getNoCreate().ifPresent(val -> formData.put("no-create", String.valueOf(val)));
            params.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
            params.getLeafResource().ifPresent(val -> formData.put("leaf-resource", val));
            params.getSecondsSinceEpoch().ifPresent(val -> formData.put("seconds-since-epoch", String.valueOf(val)));
            params.getReference().ifPresent(val -> formData.put("reference", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String lpath, int catalogOnly, DataObjectRemoveParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        formData.put("catalog-only", String.valueOf(catalogOnly));
        if (null != params) {
            params.getNoTrash().ifPresent(val -> formData.put("no-trash", String.valueOf(val)));
            params.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response calculateChecksum(String token, String lpath, DataObjectCalculateChecksumParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "calculate_checksum");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getResource().ifPresent(val -> formData.put("resource", val));
            params.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
            params.getForce().ifPresent(val -> formData.put("force", String.valueOf(val)));
            params.getAll().ifPresent(val -> formData.put("all", String.valueOf(val)));
            params.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response verifyChecksum(String token, String lpath, DataObjectVerifyChecksumParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "verify_checksum");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getResource().ifPresent(val -> formData.put("resource", val));
            params.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
            params.getComputeChecksums().ifPresent(val -> formData.put("compute-checksums", String.valueOf(val)));
            params.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String lpath, Optional<String> ticket) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        ticket.ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response rename(String token, String oldPath, String newPath) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "rename");
        formData.put("old-lpath", oldPath);
        formData.put("new-lpath", newPath);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response copy(String token, String srcLpath, String dstLpath, DataObjectCopyParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "copy");
        formData.put("src-lpath", srcLpath);
        formData.put("dst-lpath", dstLpath);
        if (null != params) {
            params.getSrcResource().ifPresent(val -> formData.put("src-resource", val));
            params.getDstResource().ifPresent(val -> formData.put("dst-resource", val));
            params.getOverwrite().ifPresent(val -> formData.put("overwrite", String.valueOf(val)));
        }
        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response replicate(String token, String lpath, String srcResource, String dstResource,
                              OptionalInt admin) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "replicate");
        formData.put("lpath", lpath);
        formData.put("src-resource", srcResource);
        formData.put("dst-resource", dstResource);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response trim(String token, String lpath, int replicaNum, DataObjectTrimParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "trim");
        formData.put("lpath", lpath);
        formData.put("replica-number", String.valueOf(replicaNum));
        if (null != params) {
            params.getCatalogOnly().ifPresent(val -> formData.put("catalog-only", String.valueOf(val)));
            params.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response register(String token, String lpath, String ppath, String resource, DataObjectRegisterParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "register");
        formData.put("lpath", lpath);
        formData.put("ppath", ppath);
        formData.put("resource", resource);
        if (null != params) {
            params.getAsAdditionalReplica().ifPresent(val -> formData.put("as-additional-replica", String.valueOf(val)));
            params.getDataSize().ifPresent(val -> formData.put("data-size", String.valueOf(val)));
            params.getChecksum().ifPresent(val -> formData.put("checksum", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response read(String token, String lpath, DataObjectReadParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "read");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
            params.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));
            params.getTicket().ifPresent(val -> formData.put("ticket", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response write(String token, String lpath, byte[] bytes, DataObjectWriteParams params) {

        String boundary = "----http_api_write_data_objects_operations----";

        // add parameters
        StringBuilder sb = new StringBuilder();
        addFormData(sb, boundary, "op", "write");
        addFormData(sb, boundary, "lpath", lpath);
        if (null != params) {
            params.getResource().ifPresent(val -> addFormData(sb, boundary, "resource", val));
            params.getOffset().ifPresent(val -> addFormData(sb, boundary, "offset", String.valueOf(val)));
            params.getTruncate().ifPresent(val -> addFormData(sb, boundary, "truncate", String.valueOf(val)));
            params.getAppend().ifPresent(val -> addFormData(sb, boundary, "append", String.valueOf(val)));
            params.getParallelWriteHandle().ifPresent(val -> addFormData(sb, boundary, "parallel-write-handle", val));
            params.getStreamIndex().ifPresent(val -> addFormData(sb, boundary, "stream-index", String.valueOf(val)));
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

    public Response parallelWriteInit(String token, String lpath, int streamCount,
                                      DataObjectParallelWriteInitParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_init");
        formData.put("lpath", lpath);
        formData.put("stream-count", String.valueOf(streamCount));
        if (null != params) {
            params.getTruncate().ifPresent(val -> formData.put("truncate", String.valueOf(val)));
            params.getAppend().ifPresent(val -> formData.put("append", String.valueOf(val)));
            params.getTicket().ifPresent(val -> formData.put("ticket", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response parallelWriteShutdown(String token, String parallelWriteHandle) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_shutdown");
        formData.put("parallel-write-handle", parallelWriteHandle);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modifyMetadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                   OptionalInt admin) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_metadata");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response setPermission(String token, String lpath, String entityName, String permission,
                                  OptionalInt admin) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_permission");
        formData.put("lpath", lpath);
        formData.put("entity-name", entityName);
        formData.put("permission", permission);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modifyPermissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
                                      OptionalInt admin) {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = null;
        try {
            operationsJson = mapper.writeValueAsString(jsonParam);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_permissions");
        formData.put("lpath", lpath);
        formData.put("operations", operationsJson);
        admin.ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modifyReplica(String token, String lpath, DataObjectModifyReplicaParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_replica");
        formData.put("lpath", lpath);
        params.getResourceHierarchy().ifPresent(val -> formData.put("resource-hierarchy", val));
        params.getReplicaNum().ifPresent(val -> formData.put("replica-number", val));
        params.getNewDataChecksum().ifPresent(val -> formData.put("new-data-checksum", val));
        params.getNewDataComments().ifPresent(val -> formData.put("new-data-comments", val));

        params.getNewDataCreateTime().ifPresent(val -> formData.put("new-data-create-time", val));
        params.getNewDataExpiry().ifPresent(val -> formData.put("new-data-expiry", val));
        params.getNewDataMode().ifPresent(val -> formData.put("new-data-mode", val));
        params.getNewDataModifyTime().ifPresent(val -> formData.put("new-data-modify-time", val));

        params.getNewDataPath().ifPresent(val -> formData.put("new-data-path", val));
        params.getNewDataReplicaNum().ifPresent(val -> formData.put("new-data-replica-number", val));
        params.getNewDataRepliaStatus().ifPresent(val -> formData.put("new-data-replica-status", val));
        params.getNewDataResourceId().ifPresent(val -> formData.put("new-data-resource-id", val));

        params.getNewDataSize().ifPresent(val -> formData.put("new-data-size", val));
        params.getNewDataStatus().ifPresent(val -> formData.put("new-data-status", val));
        params.getNewDataTypeName().ifPresent(val -> formData.put("new-data-type-name", val));
        params.getNewDataVersion().ifPresent(val -> formData.put("new-data-version", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }
}