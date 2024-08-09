package org.irods.operations;

import org.irods.IrodsHttpClient;
import org.irods.properties.Ticket.TicketCreateParams;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class TicketOperations {
    private final IrodsHttpClient client;
    private String baseUrl;

    public TicketOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/tickets";
    }

    public Response create(String token, String lpath, TicketCreateParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "create");
        formData.put("lpath", lpath);
        if (null != params) {
            params.getType().ifPresent(val -> formData.put("type", val));
            params.getUseCount().ifPresent(val -> formData.put("use-count", String.valueOf(val)));
            params.getWriteDataObjectCount().ifPresent(val -> formData.put("write-data-object-count", String.valueOf(val)));
            params.getWriteByteCount().ifPresent(val -> formData.put("write-byte-count", String.valueOf(val)));
            params.getSecondsUntilExpiration().ifPresent(val -> formData.put("seconds-until-expiration", String.valueOf(val)));
            params.getUsers().ifPresent(val -> formData.put("users", val));
            params.getGroups().ifPresent(val -> formData.put("groups", val));
            params.getHosts().ifPresent(val -> formData.put("hosts", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}