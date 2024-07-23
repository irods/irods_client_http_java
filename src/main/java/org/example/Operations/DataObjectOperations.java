package org.example.Operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Properties.DataObject.*;
import org.example.Properties.DataObject.DataObjectModifyReplicaParams;
import org.example.Wrapper;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Serialize.ModifyPermissionsOperations;
import org.example.Util.HttpRequestUtil;
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

    public Response touch(String token, String lpath, DataObjectTouchParams prop)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("lpath", lpath);
        prop.getNoCreate().ifPresent(val -> formData.put("no-create", String.valueOf(val)));
        prop.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
        prop.getLeafResource().ifPresent(val -> formData.put("leaf-resource", val));
        prop.getSecondsSinceEpoch().ifPresent(val -> formData.put("seconds-since-epoch", String.valueOf(val)));
        prop.getReference().ifPresent(val -> formData.put("reference", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response touch(String token, String lpath) {
        DataObjectTouchParams prop = new DataObjectTouchParams();
        return this.touch(token, lpath, prop);
    }

    public Response remove(String token, String lpath, int catalogOnly, DataObjectRemoveParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("lpath", lpath);
        formData.put("catalog-only", String.valueOf(catalogOnly));
        prop.getNoTrash().ifPresent(val -> formData.put("no-trash", String.valueOf(val)));
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String lpath, int catalogOnly) {
        DataObjectRemoveParams prop = new DataObjectRemoveParams();
        return this.remove(token, lpath, catalogOnly, prop);
    }

    public Response calculate_checksum(String token, String lpath, DataObjectCalculateChecksumParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "calculate_checksum");
        formData.put("lpath", lpath);
        prop.getResource().ifPresent(val -> formData.put("resource", val));
        prop.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
        prop.getForce().ifPresent(val -> formData.put("force", String.valueOf(val)));
        prop.getAll().ifPresent(val -> formData.put("all", String.valueOf(val)));
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response calculate_checksum(String token, String lpath) {
        DataObjectCalculateChecksumParams prop = new DataObjectCalculateChecksumParams();
        return this.calculate_checksum(token, lpath, prop);
    }

    public Response verify_checksum(String token, String lpath, DataObjectVerifyChecksumParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "verify_checksum");
        formData.put("lpath", lpath);
        prop.getResource().ifPresent(val -> formData.put("resource", val));
        prop.getReplicaNum().ifPresent(val -> formData.put("replica-number", String.valueOf(val)));
        prop.getComputeChecksums().ifPresent(val -> formData.put("compute-checksums", String.valueOf(val)));
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response verify_checksum(String token, String lpath) {
        DataObjectVerifyChecksumParams prop = new DataObjectVerifyChecksumParams();
        return this.verify_checksum(token, lpath, prop);
    }

    public Response stat(String token, String lpath, DataObjectStatParams prop) {

        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "stat");
        formData.put("lpath", lpath);
        prop.getTicket().ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response stat(String token, String lpath) {
        DataObjectStatParams prop = new DataObjectStatParams();
        return this.stat(token, lpath, prop);
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

    public Response copy(String token, String srcLpath, String dstLpath, DataObjectCopyParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "copy");
        formData.put("src-lpath", srcLpath);
        formData.put("dst-lpath", dstLpath);
        prop.getSrcResource().ifPresent(val -> formData.put("src-resource", val));
        prop.getDstResource().ifPresent(val -> formData.put("dst-resource", val));
        prop.getOverwrite().ifPresent(val -> formData.put("overwrite", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response copy(String token, String srcLpath, String dstLpath) {
        DataObjectCopyParams prop = new DataObjectCopyParams();
        return this.copy(token, srcLpath, dstLpath, prop);
    }

    public Response replicate(String token, String lpath, String srcResource, String dstResource,
                              DataObjectReplicateParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "replicate");
        formData.put("lpath", lpath);
        formData.put("src-resource", srcResource);
        formData.put("dst-resource", dstResource);
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response replicate(String token, String lpath, String srcResource, String dstResource) {
        DataObjectReplicateParams prop = new DataObjectReplicateParams();
        return this.replicate(token, lpath, srcResource, dstResource, prop);
    }

    public Response trim(String token, String lpath, int replicaNum, DataObjectTrimParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "trim");
        formData.put("lpath", lpath);
        formData.put("replica-number", String.valueOf(replicaNum));
        prop.getCatalogOnly().ifPresent(val -> formData.put("catalog-only", String.valueOf(val)));
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response trim(String token, String lpath, int replicaNum) {
        DataObjectTrimParams prop = new DataObjectTrimParams();
        return this.trim(token, lpath, replicaNum, prop);
    }

    public Response register(String token, String lpath, String ppath, String resource, DataObjectRegisterParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "register");
        formData.put("lpath", lpath);
        formData.put("ppath", ppath);
        formData.put("resource", resource);
        prop.getAsAdditionalReplica().ifPresent(val -> formData.put("as-additional-replica", String.valueOf(val)));
        prop.getDataSize().ifPresent(val -> formData.put("data-size", String.valueOf(val)));
        prop.getChecksum().ifPresent(val -> formData.put("checksum", val));


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response register(String token, String lpath, String ppath, String resource) {
        DataObjectRegisterParams prop = new DataObjectRegisterParams();
        return this.register(token, lpath, ppath, resource, prop);
    }

    public Response read(String token, String lpath, DataObjectReadParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "read");
        formData.put("lpath", lpath);
        prop.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
        prop.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));
        prop.getTicket().ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response read(String token, String lpath) {
        DataObjectReadParams prop = new DataObjectReadParams();
        return this.read(token, lpath, prop);
    }

    public Response write(String token, String lpath, byte[] bytes, DataObjectWriteParams prop) {

        String boundary = "----http_api_write_data_objects_operations----";

        // add parameters
        StringBuilder sb = new StringBuilder();
        addFormData(sb, boundary, "op", "write");
        addFormData(sb, boundary, "lpath", lpath);
        prop.getResource().ifPresent(val -> addFormData(sb, boundary, "resource", val));
        prop.getOffset().ifPresent(val -> addFormData(sb, boundary, "offset", String.valueOf(val)));
        prop.getTruncate().ifPresent(val -> addFormData(sb, boundary, "truncate", String.valueOf(val)));
        prop.getAppend().ifPresent(val -> addFormData(sb, boundary, "append", String.valueOf(val)));
        prop.getParallelWriteHandle().ifPresent(val -> addFormData(sb, boundary, "parallel-write-handle", val));
        prop.getStreamIndex().ifPresent(val -> addFormData(sb, boundary, "stream-index", String.valueOf(val)));

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

    public Response write(String token, String lpath, byte[] bytes) {
        DataObjectWriteParams prop = new DataObjectWriteParams();
        return this.write(token, lpath, bytes, prop);
    }


    /**
     * Helper method for write() to add form data
     */
    private static void addFormData(StringBuilder sb, String boundary, String name, String value) {
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        sb.append(value + "\r\n");
    }

    public Response parallel_write_init(String token, String lpath, int streamCount,
                                        DataObjectParallelWriteInitParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_init");
        formData.put("lpath", lpath);
        formData.put("stream-count", String.valueOf(streamCount));
        prop.getTruncate().ifPresent(val -> formData.put("truncate", String.valueOf(val)));
        prop.getAppend().ifPresent(val -> formData.put("append", String.valueOf(val)));
        prop.getTicket().ifPresent(val -> formData.put("ticket", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response parallel_write_init(String token, String lpath, int streamCount) {
        DataObjectParallelWriteInitParams prop = new DataObjectParallelWriteInitParams();
        return this.parallel_write_init(token, lpath, streamCount, prop);
    }

    public Response parallel_write_shutdown(String token, String parallelWriteHandle) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_shutdown");
        formData.put("parallel-write-handle", parallelWriteHandle);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                    DataObjectModifyMetadataParams prop) {
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
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam) {
        DataObjectModifyMetadataParams prop = new DataObjectModifyMetadataParams();
        return this.modify_metadata(token, lpath, jsonParam, prop);
    }

    public Response set_permission(String token, String lpath, String entityName, String permission,
                                   DataObjectSetPermissionParams prop) {
        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "set_permission");
        formData.put("lpath", lpath);
        formData.put("entity-name", entityName);
        formData.put("permission", permission);
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token,
                client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response set_permission(String token, String lpath, String entityName, String permission) {
        DataObjectSetPermissionParams prop = new DataObjectSetPermissionParams();
        return this.set_permission(token, lpath, entityName, permission, prop);
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam,
                                       DataObjectModifyPermissionsParams prop) {
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
        prop.getAdmin().ifPresent(val -> formData.put("admin", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

    public Response modify_permissions(String token, String lpath, List<ModifyPermissionsOperations> jsonParam) {
        DataObjectModifyPermissionsParams prop = new DataObjectModifyPermissionsParams();
        return this.modify_permissions(token, lpath, jsonParam, prop);
    }

    public Response modify_replica(String token, String lpath, DataObjectModifyReplicaParams prop) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_replica");
        formData.put("lpath", lpath);
        prop.getResourceHierarchy().ifPresent(val -> formData.put("resource-hierarchy", val));
        prop.getReplicaNum().ifPresent(val -> formData.put("replica-number", val));
        prop.getNewDataChecksum().ifPresent(val -> formData.put("new-data-checksum", val));
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
