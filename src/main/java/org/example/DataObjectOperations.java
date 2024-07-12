package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.ByteArrayBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.example.Mapper.Serialize.ModifyMetadataOperations;
import org.example.Mapper.Serialize.ModifyPermissionsOperations;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataObjectOperations {
    private final Manager client;
    private String baseUrl;


    public DataObjectOperations(Manager client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/data-objects";
    }

    public Response touch(String token, String lpath, boolean noCreate, int replicaNum, String leafResource,
                          int secondsSinceEpoch, String reference) throws IOException, InterruptedException {
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

    public Response remove(String token, String lpath, boolean catalogOnly, boolean noTrash, boolean admin)
            throws IOException, InterruptedException {
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
                                       boolean all, boolean admin) throws IOException, InterruptedException {
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

    public Response vertify_checksum(String token, String lpath, String resource, int replicaNum,
                                     boolean computeChecksums, boolean admin)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "vertify_checksum");
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

    public Response stat(String token, String lpath, String ticket) throws IOException, InterruptedException {

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

    public Response rename(String token, String oldPath, String newPath) throws IOException, InterruptedException {
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
                         boolean overwrite) throws IOException, InterruptedException {
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

    public Response replicate(String token, String lpath, String srcResource, String dstResource,
                         boolean admin) throws IOException, InterruptedException {
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

    public Response trim(String token, String lpath, int replicaNum, boolean catalogOnly,
                              boolean admin) throws IOException, InterruptedException {
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
                             int dataSize, String checksum) throws IOException, InterruptedException {
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

    public Response read(String token, String lpath, int offset, int count, String ticket) throws IOException, InterruptedException {
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
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(baseUrl);
            uploadFile.setHeader("Authorization", "Bearer " + token);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("op", new StringBody("write", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            builder.addPart("lpath", new StringBody(lpath, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            if (resource != null) {
                builder.addPart("resource", new StringBody(resource, ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            }
            if (offset != -1) {
                builder.addPart("offset", new StringBody(String.valueOf(offset), ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            }

            builder.addPart("truncate", new StringBody( truncate ? "1" : "0", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            builder.addPart("append", new StringBody( append ? "1" : "0", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));

            builder.addPart("bytes", new ByteArrayBody(bytes, ContentType.APPLICATION_OCTET_STREAM, "data.bin"));
            if (parallelWriteHandle != null) {
                builder.addPart("parallel-write-handle", new StringBody(parallelWriteHandle,  ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            }
            if (streamIndex != -1) {
                builder.addPart("stream-index", new StringBody(String.valueOf(streamIndex), ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
            }

            uploadFile.setEntity(builder.build());

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                int statusCode = response.getCode();
                if (statusCode == 200) {
                    // Deserialize the response body as needed
                    String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                } else {
                    System.err.println("Error: " + response.getReasonPhrase());
                    return null;
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

//    public Response write(String token, String lpath, String resource, int offset, boolean truncate, boolean append,
//                          byte[] bytes, String parallelWriteHandle, int streamIndex)
//            throws IOException, ParseException, InterruptedException {
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost post = new HttpPost(baseUrl);
//            post.setHeader("Authorization", "Bearer " + token);
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addTextBody("op", "write", ContentType.TEXT_PLAIN);
//            builder.addTextBody("lpath", lpath, ContentType.TEXT_PLAIN);
//            if (resource != null) {
//                builder.addTextBody("resource", resource, ContentType.TEXT_PLAIN);
//            }
//            if (offset != -1) {
//                builder.addTextBody("offset", String.valueOf(offset), ContentType.TEXT_PLAIN);
//            }
//            builder.addTextBody("truncate", truncate ? "1" : "0", ContentType.TEXT_PLAIN);
//            builder.addTextBody("append", append ? "1" : "0", ContentType.TEXT_PLAIN);
//            builder.addBinaryBody("bytes", bytes, ContentType.APPLICATION_OCTET_STREAM, "data.bin");
//            if (parallelWriteHandle != null) {
//                builder.addTextBody("parallel-write-handle", parallelWriteHandle, ContentType.TEXT_PLAIN);
//            }
//            if (streamIndex != -1) {
//                builder.addTextBody("stream-index", String.valueOf(streamIndex), ContentType.TEXT_PLAIN);
//            }
//
//            post.setEntity(builder.build());
//
//            try (CloseableHttpResponse response = client.execute(post)) {
//                int statusCode = response.getCode();
//                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//                return new Response(statusCode, responseBody);
//            }
//        }
//
//    }

//    public Response write(String token, String lpath, String resource, int offset, boolean truncate, boolean append,
//                          String bytesPathString, String parallelWriteHandle, int streamIndex)
//            throws IOException, ParseException {

//        File fileToUpload = new File(bytesPathString);
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        // HTTP POST request
//        HttpPost httpPost = new HttpPost(baseUrl);
//
//        // Authorization header
//        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
//
//        // Multipart entity builder
//        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//
//        // Add form data parameters
//        entityBuilder.addTextBody("op", "write", ContentType.TEXT_PLAIN);
//        entityBuilder.addTextBody("lpath", lpath, ContentType.TEXT_PLAIN);
//        if (resource != null) {
//            entityBuilder.addTextBody("resource", resource, ContentType.TEXT_PLAIN);
//        }
//        entityBuilder.addTextBody("offset", String.valueOf(offset), ContentType.TEXT_PLAIN);
//        entityBuilder.addTextBody("truncate", truncate ? "1" : "0", ContentType.TEXT_PLAIN);
//        entityBuilder.addTextBody("append", append ? "1" : "0", ContentType.TEXT_PLAIN);
//
//        // Add binary data
//        entityBuilder.addBinaryBody("bytes", fileToUpload, ContentType.APPLICATION_OCTET_STREAM, fileToUpload.getName());
//        if (parallelWriteHandle != null) {
//            entityBuilder.addTextBody("parallel-write-handle", parallelWriteHandle, ContentType.TEXT_PLAIN);
//        }
//        entityBuilder.addTextBody("stream-index", String.valueOf(streamIndex), ContentType.TEXT_PLAIN);
//
//        // Build the entity
//        HttpEntity reqEntity = entityBuilder.build();
//
//        // Set the entity to the POST request
//        httpPost.setEntity(reqEntity);
//
//        // Execute the request
//        CloseableHttpResponse response = httpClient.execute(httpPost);
//
//        // Handle response
//        int statusCode = response.getCode();
//        String responseBody = EntityUtils.toString(response.getEntity());
//
//        System.out.println("Response status code: " + statusCode);
//        System.out.println("Response body: " + responseBody);
//    }

//     public Response write(String token, String lpath, String resource, int offset, boolean truncate, boolean append,
//                              String bytesPathString, String parallelWriteHandle, int streamIndex)
//                throws IOException, ParseException {
//
//            Path bytesPath = Paths.get(bytesPathString);
//
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpPost uploadFile = new HttpPost(baseUrl);
//            uploadFile.setHeader("Authorization", "Bearer " + token);
//
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addTextBody("op", "write", ContentType.TEXT_PLAIN);
//            builder.addTextBody("lpath", lpath, ContentType.TEXT_PLAIN);
//
//            if (resource != null) {
//                builder.addTextBody("resource", resource, ContentType.TEXT_PLAIN);
//            }
//
//            builder.addTextBody("offset", String.valueOf(offset), ContentType.TEXT_PLAIN);
//            builder.addTextBody("truncate", truncate ? "1" : "0", ContentType.TEXT_PLAIN);
//            builder.addTextBody("append", append ? "1" : "0", ContentType.TEXT_PLAIN);
//
//            builder.addBinaryBody("bytes", Files.readAllBytes(bytesPath), ContentType.APPLICATION_OCTET_STREAM,
//                    bytesPath.getFileName().toString());
//
//            if (parallelWriteHandle != null) {
//                builder.addTextBody("parallel-write-handle", parallelWriteHandle, ContentType.TEXT_PLAIN);
//            }
//
//            builder.addTextBody("stream-index", String.valueOf(streamIndex), ContentType.TEXT_PLAIN);
//
//            uploadFile.setEntity(builder.build());
//
//            CloseableHttpResponse response = httpClient.execute(uploadFile);
//            System.out.println(response.getCode());
//            System.out.println(EntityUtils.toString(response.getEntity()));
//
//            response.close();
//            httpClient.close();
//
//            return new Response(response.getCode(), EntityUtils.toString(response.getEntity()));
//        }


//    public Response write(String token, String lpath, String resource, int offset, boolean truncate, boolean append,
//                          String bytesPathString, String parallelWriteHandle, int streamIndex)
//            throws IOException, InterruptedException, ParseException {
//        Map<Object, Object> formData = new HashMap<>();
//        formData.put("op", "write");
//        if (resource != null) {
//            formData.put("resource", resource);
//        }
//        formData.put("lpath", lpath);
//        if (offset != -1) {
//            formData.put("offset", offset);
//        } else {
//            formData.put("offset", 0);
//        }
//        formData.put("truncate", truncate ? "1" : "0");
//        formData.put("append", append ? "1" : "0");
//        formData.put()
//        if (resource != parallelWriteHandle) {
//            formData.put("parallel-write-handle", parallelWriteHandle);
//        }
//        if (streamIndex != -1) {
//            formData.put("stream-index", streamIndex);
//        }
//
//
//
//        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
//        return new Response(response.statusCode(), response.body());
//    }

    public Response parallel_write_init(String token, String lpath, int streamCount, boolean truncate, boolean append,
                                        String ticket) throws IOException, InterruptedException {
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

    public Response parallel_write_shutdown(String token, String parallelWriteHandle) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "parallel_write_shutdown");
        formData.put("parallel-write-handle", parallelWriteHandle);


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response modify_metadata(String token, String lpath, List<ModifyMetadataOperations> jsonParam,
                                    boolean admin)
            throws IOException, InterruptedException, IrodsException {
        // Serialize the operations parameter to JSON
        ObjectMapper mapper = new ObjectMapper();
        String operationsJson = mapper.writeValueAsString(jsonParam);

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
                                   boolean admin) throws IOException, InterruptedException, IrodsException {
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

    public Response modify_permissions(String token, String lpath,
                                       List<ModifyPermissionsOperations> jsonParam, boolean admin)
            throws IOException, InterruptedException, IrodsException {
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

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());

    }

    // curl http://localhost:<port>/irods-http-api/<version>/data-objects \
    //    -H 'Authorization: Bearer <token>' \
    //    --data-urlencode 'op=modify_replica' \
    //    --data-urlencode 'lpath=<string>' \ # Absolute logical path to a data object.
    //    --data-urlencode 'resource-hierarchy=<string>' \
    //    --data-urlencode 'replica-number=<integer>' \
    //    --data-urlencode 'new-data-checksum=<string>' \
    //    --data-urlencode 'new-data-comments=<string>' \
    //    --data-urlencode 'new-data-create-time=<integer>' \
    //    --data-urlencode 'new-data-expiry=<integer>' \
    //    --data-urlencode 'new-data-mode=<string>' \
    //    --data-urlencode 'new-data-modify-time=<string>' \
    //    --data-urlencode 'new-data-path=<string>' \
    //    --data-urlencode 'new-data-replica-number=<integer>' \
    //    --data-urlencode 'new-data-replica-status=<integer>' \
    //    --data-urlencode 'new-data-resource-id=<integer>' \
    //    --data-urlencode 'new-data-size=<integer>' \
    //    --data-urlencode 'new-data-status=<string>' \
    //    --data-urlencode 'new-data-type-name=<string>' \
    //    --data-urlencode 'new-data-version=<string>'
    public Response modify_replica(String token, String lpath,String resourceHierarchy, int replicaNum,
                                   String newDataChecksum, String newDataComments, int newDataCreateTime,
                                   int newDataExpiry, String newDataMode, String newDataModifyTime, String newDataPath,
                                   int newDataReplicaNum, int newDataRepliaStatus, int newDataResourceId, int newDataSize,
                                   String newDataStatus, String newDataTypeName, String newDataVersion)
            throws IOException, InterruptedException, IrodsException {


        // contains parameters for the HTTP request
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify_replica");
        formData.put("lpath", lpath);
        if (resourceHierarchy != null) {
            formData.put("resource-hierarchy", resourceHierarchy);
        }
        if (replicaNum != -1) {
            formData.put("replica-number", String.valueOf(replicaNum));
        }
        if (newDataChecksum != null) {
            formData.put("newDataComments", newDataChecksum);
        }
        if (newDataComments != null) {
            formData.put("new-data-comments", newDataComments);
        }
        if (newDataCreateTime != -1) {
            formData.put("new-data-create-time", String.valueOf(newDataCreateTime));
        }
        if (newDataExpiry != -1) {
            formData.put("new-data-expiry", String.valueOf(newDataExpiry));
        }
        if (newDataMode != null) {
            formData.put("new-data-mode", newDataMode);
        }
        if (newDataModifyTime != null) {
            formData.put("new-data-modify-time", newDataModifyTime);
        }
        if (newDataPath != null) {
            formData.put("new-data-path", newDataPath);
        }
        if (newDataReplicaNum != -1) {
            formData.put("new-data-replica-number", String.valueOf(newDataReplicaNum));
        }
        if (newDataRepliaStatus != -1) {
            formData.put("new-data-replica-status", String.valueOf(newDataRepliaStatus));
        }
        if (newDataResourceId != -1) {
            formData.put("new-data-resource-id", String.valueOf(newDataResourceId));
        }
        if (newDataSize != -1) {
            formData.put("new-data-size", String.valueOf(newDataSize));
        }
        if (newDataStatus != null) {
            formData.put("new-data-status", newDataSize);
        }
        if (newDataTypeName != null) {
            formData.put("new-data-type-name", newDataTypeName);
        }
        if (newDataVersion != null) {
            formData.put("new-data-version", newDataVersion);
        }


        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());

        return new Response(response.statusCode(), response.body());
    }

}
