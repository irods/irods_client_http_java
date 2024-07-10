package org.example.Util;

public class Response {
    private int HttpStatusCode;
    private String body;

    public Response(int HttpStatusCode, String body) {
        this.HttpStatusCode = HttpStatusCode;
        this.body = body;
    }

    public int getHttpStatusCode() {
        return HttpStatusCode;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpStatusCode=" + HttpStatusCode +
                ", body='" + body + '\'';
    }
}
