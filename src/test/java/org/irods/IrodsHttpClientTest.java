package org.irods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.irods.Util.Response;
import static org.junit.Assert.*;
public class IrodsHttpClientTest {
    private static String baseUrl;
    private static IrodsHttpClient client;
    private static final Logger logger = LogManager.getLogger(IrodsHttpClientTest.class);
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
        logger.debug(res.getBody());
        assertEquals(200, res.getHttpStatusCode());
        assertEquals(res.getBody(), res.getBody());

    }
    @Test
    public void authenticate_invalid_user() {
        IrodsHttpClient test = new IrodsHttpClient(baseUrl);
        Response res = test.authenticate("test", "test");
        logger.debug(res.getBody());
        assertEquals(401, res.getHttpStatusCode());
    }
}