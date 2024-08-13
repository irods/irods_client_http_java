package org.irods.util;

/**
 * Represents the response object that is returned by each method in the endpoints.
 * This class encapsulates the HTTP status code and response body of an HTTP request.
 */
public class Response {
    private int HttpStatusCode;
    private String body;

    /**
     * Constructs a new Response object with the given HTTP status code and body.
     *
     * @param HttpStatusCode The HTTP status code
     * @param body The response body
     */
    public Response(int HttpStatusCode, String body) {
        this.HttpStatusCode = HttpStatusCode;
        this.body = body;
    }

    /**
     * Returns the HTTP status code of the response.
     *
     * @return The HTTP status code.
     */
    public int getHttpStatusCode() {
        return HttpStatusCode;
    }

    /**
     * Returns the body of the response.
     *
     * @return The response body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns a string representation of the Response object.
     *
     * @return A string representation of the Response object.
     */
    @Override
    public String toString() {
        return "HttpStatusCode=" + HttpStatusCode +
                ", body='" + body + '\'';
    }
}