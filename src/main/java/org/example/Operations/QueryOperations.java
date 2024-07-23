package org.example.Operations;

import org.example.Properties.Query.QueryExecuteGenqueryParams;
import org.example.Wrapper;
import org.example.Util.HttpRequestUtil;
import org.example.Util.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class QueryOperations {
    private final Wrapper client;
    private String baseUrl;


    public QueryOperations(Wrapper client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/query";
    }
    public Response execute_genquery(String token, String query, QueryExecuteGenqueryParams params)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute_genquery");

        // need to URL encode the spaces in the query to prevent illegal characters in the query
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            formData.put("query", encodedQuery);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
        params.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));
        params.getCaseSensitive().ifPresent(val -> formData.put("case-sensitive", String.valueOf(val)));
        params.getDistinct().ifPresent(val -> formData.put("distinct", String.valueOf(val)));
        params.getParser().ifPresent(val -> formData.put("parser", val));
        params.getSqlOnly().ifPresent(val -> formData.put("sql-only", String.valueOf(val)));
        params.getZone().ifPresent(val -> formData.put("zone", val));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response execute_genquery(String token, String query) {
        QueryExecuteGenqueryParams params = new QueryExecuteGenqueryParams();
        return this.execute_genquery(token, query, params);
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
