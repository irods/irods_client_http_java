package org.example.Operations;

import org.example.Manager;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class QueryOperations {
    private final Manager client;
    private String baseUrl;


    public QueryOperations(Manager client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/query";
    }
    public Response execute_genquery(String token, String query, int offset, int count, boolean caseSensitive,
                                     boolean distinct, String parser, boolean sqlOnly, String zone)
            throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "touch");
        formData.put("query", query);
        if (offset != -1) {
            formData.put("offset", String.valueOf(offset));
        } else {
            formData.put("offset", "0");
        }
        if (count != -1) {
            formData.put("count", String.valueOf(count));
        }
        formData.put("canse-sensitive", caseSensitive ? "1" : "0");
        formData.put("distinct", distinct ? "1" : "0");
        if (parser != null) {
            formData.put("parser", parser);
        } else {
            formData.put("parser", "genquery1");
        }
        formData.put("sqlOnly", sqlOnly ? "1" : "0");
        if (zone != null) {
            formData.put("zone", zone);
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response execute_specific_query(String token, String name, String args, String argsDelimiter,
                                           int offset, int count) throws IOException, InterruptedException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute_specific_query");
        formData.put("name", name);
        if (args != null) {
            formData.put("args", args);
        }
        if (argsDelimiter != null) {
            formData.put("args-delimiter", argsDelimiter);
        } else {
            formData.put("args-delimiter", ","); // default
        }
        if (offset != -1) {
            formData.put("offset", String.valueOf(offset));
        } else {
            formData.put("offset", "0"); // default
        }
        if (count != -1) {
            formData.put("count", String.valueOf(count));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

}
