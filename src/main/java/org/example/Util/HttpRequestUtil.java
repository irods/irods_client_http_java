package org.example.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.IrodsClient;
import org.example.Mapper.CollectionsCreate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String createRequestBody(Map<Object, Object> formData) {
        return formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString()
                .collect(Collectors.joining("&"));
    }

    public static HttpRequest buildRequest(String baseUrl, String token, String form) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
    }

    public static HttpResponse<String> sendRequest(HttpClient client, HttpRequest request)
            throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static <T> T parseResponse(HttpResponse<String> response, Class<T> responseType) throws JsonProcessingException {
        return mapper.readValue(response.body(), responseType);
    }

    /**
     * Sends HTTP request and parses the JSON using the methods above
     * @param formData
     * @param baseUrl
     * @param token
     * @param responseType Class type of the response object
     * @return Instance of the response type containing the parsed data
     * @param <T> Type of response object
     * @throws IOException
     * @throws InterruptedException
     */
    public static <T> T sendAndParse(Map<Object, Object> formData, String baseUrl, String token,
                                     HttpClient client, Class<T> responseType) throws IOException, InterruptedException {
        // creating the request body
        String form = HttpRequestUtil.createRequestBody(formData);

        // creating the request
        HttpRequest request = HttpRequestUtil.buildRequest(baseUrl, token, form);

        // sending request
        HttpResponse<String> response = HttpRequestUtil.sendRequest(client, request);

        // parse the JSON
        T mapped = HttpRequestUtil.parseResponse(response, responseType);

        return mapped;
    }
}
