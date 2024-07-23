package org.example.Operations;

import org.example.Wrapper;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.io.IOException;
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

    public Response report(String token) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "report");

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}
