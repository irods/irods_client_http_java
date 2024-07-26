package org.example.Operations;

import org.example.Properties.Zone.ZoneAddParams;
import org.example.Wrapper;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ZoneOperations {
    private final Wrapper client;
    private String baseUrl;


    public ZoneOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/zones";
    }

    public Response add(String token, String name, ZoneAddParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add");
        formData.put("name", name);
        if (params != null) {
            params.getConnectionInfo().ifPresent(val -> formData.put("connection-info", val));
            params.getComment().ifPresent(val -> formData.put("comment", val));
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

    public Response modify(String token, String name, String property, String value) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "modify");
        formData.put("name", name);
        formData.put("property", property);
        formData.put("value", value);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response report(String token) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "report");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}
