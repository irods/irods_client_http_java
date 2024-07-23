package org.example.Operations;

import org.example.Properties.DataObject.DataObjectTouchParams;
import org.example.Properties.Query.QueryExecuteGenqueryParams;
import org.example.Properties.Query.QueryExecuteSpecifcQueryParams;
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

    public Response execute_specific_query(String token, String name, QueryExecuteSpecifcQueryParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute_specific_query");
        formData.put("name", name);
        params.getArgs().ifPresent(val -> formData.put("args", val));
        params.getArgsDelimiter().ifPresent(val -> formData.put("args-delimiter", val));
        params.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
        params.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response execute_specific_query(String token, String name) {
        QueryExecuteSpecifcQueryParams param = new QueryExecuteSpecifcQueryParams();
        return this.execute_specific_query(token, name, param);
    }

    public Response add_specific_query(String token, String name, String sql) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_specific_query");
        formData.put("name", name);
        formData.put("sql", sql);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    public Response remove_specific_query(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_specific_query");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }


}
