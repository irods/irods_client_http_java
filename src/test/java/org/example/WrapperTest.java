package org.example;

import org.junit.Before;
import org.junit.Test;
import org.example.Util.Response;

import static org.junit.Assert.*;


public class WrapperTest {
    private static String baseUrl;
    private static Wrapper rods;
    private Response response;

    @Before
    public void setup() {
        String address = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
        rods = new Wrapper(baseUrl, "rods", "rods");

        // check that baseUrl is correctly configured and can successfully connect with the API
        response = rods.information().info();
        assertEquals(200, response.getHttpStatusCode());
    }

    @Test
    public void authenticate_valid() {
        response = rods.authenticate();
        assertEquals(200, response.getHttpStatusCode());
        assertEquals(response.getBody(), rods.getAuthToken());

    }

    @Test
    public void authenticate_invalid_user() {
        Wrapper test = new Wrapper(baseUrl, "test", "test");
        response = test.authenticate();
        assertEquals(401, response.getHttpStatusCode());
    }
}