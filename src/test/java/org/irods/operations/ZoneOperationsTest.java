package org.irods.operations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.irods.IrodsHttpClient;
import org.irods.util.Response;
import org.irods.util.ZoneProperty;
import org.junit.Before;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.irods.IrodsResponseUtils.getIrodsResponseStatusCode;


public class ZoneOperationsTest {
    private IrodsHttpClient client;
    private String rodsToken;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(ZoneOperationsTest.class);

    @Before
    public void setup() {
        String host = "52.91.145.195";
        String port = "8888";
        String version = "0.3.0";

        String baseUrl = "http://" + host + ":" + port + "/irods-http-api/" + version;

        // Create client
        client = new IrodsHttpClient(baseUrl);

        // Authenticate rods
        Response res = client.authenticate("rods", "rods");
        rodsToken = res.getBody();
    }

    @Test
    public void testReportOperation() {
        Response res = client.zoneOperations().report(rodsToken);
        logger.debug(res.getBody());
        assertEquals("Zone report request failed", 200, res.getHttpStatusCode());
        assertEquals("Zone report failed", 0,
                getIrodsResponseStatusCode(res.getBody()));
    }

    @Test
    public void testAddingRemovingAndModifyingZones() {
        String zoneName = "other_zone";

        try {
            // Add a remote zone to the local zone.
            // The new zone will not have any connection information or anything else.
            Response res = client.zoneOperations().add(rodsToken, zoneName, null);
            logger.debug(res.getBody());
            assertEquals("Adding zone request failed", 200, res.getHttpStatusCode());
            assertEquals("Adding zone failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));

            // The properties to update.
            List<Map.Entry<String, String>> propertyMap = List.of(
                    Map.entry("name", "other_zone_renamed"),
                    Map.entry("connection_info", "example.org:1247"),
                    Map.entry("comment", "updated comment")
            );

            for (Map.Entry<String, String> entry : propertyMap) {
                ZoneProperty zoneProperty = ZoneProperty.valueOf(entry.getKey().toUpperCase());
                String value = entry.getValue();

                res = client.zoneOperations().modify(rodsToken, zoneName, zoneProperty, value);
                logger.debug(res.getBody());
                assertEquals("Modifying zone request failed", 200, res.getHttpStatusCode());
                assertEquals("Modifying zone with property " + zoneProperty.getValue() + " failed", 0,
                        getIrodsResponseStatusCode(res.getBody()));

                // Capture the new name of the zone following its renaming.
                if (ZoneProperty.NAME == zoneProperty) {
                    zoneName = value;
                }
            }

            // Remove the remote zone.
            res = client.zoneOperations().remove(rodsToken, zoneName);
            logger.debug(res.getBody());
            assertEquals("Removing zone request failed", 200, res.getHttpStatusCode());
            assertEquals("Removing zone failed", 0,
                    getIrodsResponseStatusCode(res.getBody()));
        } finally {
            // Remove the remote zone.
            client.zoneOperations().remove(rodsToken, zoneName);
            client.zoneOperations().remove(rodsToken, "other_zone_renamed");
        }
    }
}