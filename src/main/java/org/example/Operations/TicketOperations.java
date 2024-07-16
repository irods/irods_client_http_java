package org.example.Operations;

import org.example.Manager;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class TicketOperations {
    private final Manager client;
    private String baseUrl;


    public TicketOperations(Manager client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/tickets";
    }

    public Response create(String token, String lpath, String type, int useCount, int writeDataObjectCount,
                                 int writeByteCount, int secondsUntilExpiration, String users, String groups,
                                 String hosts) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("lpath", lpath);
        if (type != null) {
            formData.put("type", type);
        } else {
            formData.put("type", "read");
        }
        if (useCount != -1) {
            formData.put("use-count", String.valueOf(useCount));
        }
        if (writeDataObjectCount != -1) {
            formData.put("write-data-object-count", String.valueOf(writeDataObjectCount));
        }
        if (writeByteCount != -1) {
            formData.put("write-byte-count", String.valueOf(writeByteCount));
        }
        if (secondsUntilExpiration != -1) {
            formData.put("seconds-until-expiration", String.valueOf(secondsUntilExpiration));
        }
        if (users != null) {
            formData.put("users", users);
        }
        if (groups != null) {
            formData.put("groups", groups);
        }
        if (hosts != null) {
            formData.put("hosts", hosts);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String name) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

}
