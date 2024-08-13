package org.irods.operations;

import org.irods.IrodsHttpClient;
import org.irods.properties.DataObject.DataObjectCopyParams;
import org.irods.properties.Query.QueryExecuteGenQueryParams;
import org.irods.properties.Query.QueryExecuteSpecifcQueryParams;
import org.irods.util.HttpRequestUtil;
import org.irods.util.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Class provides methods to interact with the query endpoint.
 */
public class QueryOperations {
    private final IrodsHttpClient client;
    private String baseUrl;


    /**
     * Constructs a {@code QueryOperations} object.
     *
     * @param client An instance of {@link IrodsHttpClient} used to communicate with the iRODS server.
     */
    public QueryOperations(IrodsHttpClient client) {
        this.client = client;
        this.baseUrl = client.getBaseUrl() + "/query";
    }

    /**
     * Executes a GenQuery string and returns the results.
     *
     * @param token The authentication token for the iRODS user.
     * @param query The query being executed.
     * @param params An instance of the {@link QueryExecuteGenQueryParams} containing optional parameters.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response executeGenQuery(String token, String query, QueryExecuteGenQueryParams params)  {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute_genquery");

        // need to URL encode the spaces in the query to prevent illegal characters in the query
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            formData.put("query", encodedQuery);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null != params) {
            params.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
            params.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));
            params.getCaseSensitive().ifPresent(val -> formData.put("case-sensitive", String.valueOf(val)));
            params.getDistinct().ifPresent(val -> formData.put("distinct", String.valueOf(val)));
            params.getParser().ifPresent(val -> formData.put("parser", val));
            params.getSqlOnly().ifPresent(val -> formData.put("sql-only", String.valueOf(val)));
            params.getZone().ifPresent(val -> formData.put("zone", val));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Executes a specific query and returns the results.
     *
     * @param token The authentication token for the iRODS user.
     * @param name Name of specific query.
     * @param params An instance of the {@link QueryExecuteSpecifcQueryParams} containing optional parameters.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response executeSpecificQuery(String token, String name, QueryExecuteSpecifcQueryParams params) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "execute_specific_query");
        formData.put("name", name);
        if (null != params) {
            params.getArgs().ifPresent(val -> formData.put("args", val));
            params.getArgsDelimiter().ifPresent(val -> formData.put("args-delimiter", val));
            params.getOffset().ifPresent(val -> formData.put("offset", String.valueOf(val)));
            params.getCount().ifPresent(val -> formData.put("count", String.valueOf(val)));
        }

        HttpResponse<String> response = HttpRequestUtil.sendAndParseGET(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Adds a SpecificQuery to the iRODS zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param name The name of the SpecificQuery.
     * @param sql The SQL attached to the SpecificQuery.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     * @throws UnsupportedEncodingException If the named encoding is not supported.
     */
    public Response addSpecificQuery(String token, String name, String sql) throws UnsupportedEncodingException {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "add_specific_query");
        formData.put("name", name);

        // need to URL encode the spaces in the query to prevent illegal characters in the query
        String encodedSql = URLEncoder.encode(sql, StandardCharsets.UTF_8.toString());
        formData.put("sql", encodedSql);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }

    /**
     * Removes a SpecificQuery from the iRODS zone. This operation requires rodsadmin level privileges.
     *
     * @param token The authentication token for the iRODS user.
     * @param name The name of the SpecificQuery.
     * @return A {@link Response} object containing the status and body of the HTTP response.
     */
    public Response removeSpecificQuery(String token, String name) {
        Map<Object, Object> formData = new HashMap<>();
        formData.put("op", "remove_specific_query");
        formData.put("name", name);

        HttpResponse<String> response = HttpRequestUtil.sendAndParsePOST(formData, baseUrl, token, client.getClient());
        return new Response(response.statusCode(), response.body());
    }
}