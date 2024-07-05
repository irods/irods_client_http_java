package org.example.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapper.Collections.CollectionsModifyPermissions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static HttpResponse<String> response;

    /**
     * Sends a POST HTTP request and parses the JSON using the methods above
     * @param formData Contains the parameters of the HTTP request
     * @param baseUrl
     * @param token
     * @param responseType Class type of the response object
     * @return Instance of the response type containing the parsed data
     * @param <T> Type of response object
     * @throws IOException
     * @throws InterruptedException
     */
    public static <T> T sendAndParsePOST(Map<Object, Object> formData, String baseUrl, String token,
                                         HttpClient client, Class<T> responseType) throws IOException, InterruptedException {
        /// creating the request body
        String form = formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString(): key=value
                .collect(Collectors.joining("&"));

        // creating the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        // sending request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // parse the JSON
        T mapped = mapper.readValue(response.body(), responseType);
        return mapped;
    }

    /**
     * Sends a GET HTTP request and parses the JSON using the methods above
     * @param formData
     * @param baseUrl
     * @param token
     * @param responseType Class type of the response object
     * @return Instance of the response type containing the parsed data
     * @param <T> Type of response object
     * @throws IOException
     * @throws InterruptedException
     */
    public static <T> T sendAndParseGET(Map<Object, Object> formData, String baseUrl, String token,
                                         HttpClient client, Class<T> responseType) throws IOException, InterruptedException {
        // creating the request body
        String form = formData.entrySet()
                .stream()
                .map(Map.Entry::toString) // method reference to Map.Entry.toString(): key=value
                .collect(Collectors.joining("&"));

        // creating the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?" + form))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        // sending request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // parse the JSON
        T mapped = mapper.readValue(response.body(), responseType);
        return mapped;
    }
}
