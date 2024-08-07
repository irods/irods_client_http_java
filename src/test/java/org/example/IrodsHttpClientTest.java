package org.example;

import org.junit.Before;
import org.junit.Test;
import org.example.Util.Response;
import static org.junit.Assert.*;
public class IrodsHttpClientTest {
    private static String baseUrl;
    private static IrodsHttpClient client;
    @Before
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
        client = new IrodsHttpClient(baseUrl);

        // check that baseUrl is correctly configured and can successfully connect with the API
        Response res = client.information().get();
        assertEquals(200, res.getHttpStatusCode());
    }
    @Test
    public void authenticate_valid() {
        Response res = client.authenticate("rods", "rods");
        assertEquals(200, res.getHttpStatusCode());
        assertEquals(res.getBody(), res.getBody());

    }
    @Test
    public void authenticate_invalid_user() {
        IrodsHttpClient test = new IrodsHttpClient(baseUrl);
        Response res = test.authenticate("test", "test");
        assertEquals(401, res.getHttpStatusCode());
    }
}