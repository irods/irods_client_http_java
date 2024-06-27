package org.example;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class IrodsClientTest {
    private static IrodsClient client;
    private String address;
    private String port;
    private String version;
    private User rods = new User("rods", "rods");
    @BeforeEach
    public void setup() {
        this.address = "52.91.145.195";
        this.port = "8888";
        this.version = "0.3.0";
    }
    @Test
    public void testAuthenticateValidUser() throws IOException, InterruptedException {
        client = IrodsClient.newBuilder()
                .address(address)
                .port(port)
                .version(version)
                .build();

        try {
            client.authenticate(rods);
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }
        assertNotNull(rods.getAuthToken());
    }
    @Test
    public void testAuthenticateInvalidUser() throws IOException, InterruptedException {
        client = IrodsClient.newBuilder()
                .address(address)
                .port(port)
                .version(version)
                .build();

        User steve = new User("steve", "password"); // this user doesn't exist
        IrodsException exception = assertThrows(IrodsException.class, () -> {
            client.authenticate(steve);
        });

       assertEquals("Failed to authenticate: 401", exception.getMessage());
    }
    @Test
    public void testClientValidParams() {
        try {
            IrodsClient client = IrodsClient.newBuilder()
                    .address(address)
                    .port(port)
                    .version(version)
                    .build();
        } catch (Exception e) {
            fail("Expected no exception, instead got: " + e.getMessage());
        }
    }

    @Test
    public void testClientInvalidAddress() {
        address = "5291.145.195";

        java.lang.Exception exception = assertThrows(java.lang.Exception.class, () -> {
            IrodsClient client = IrodsClient.newBuilder()
                    .address(address)
                    .port(port)
                    .version(version)
                    .build();

            client.authenticate(rods);
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains("unsupported URI"),
                "Expected message to contain 'unsupported URI' but was: " +
                actualMessage);
    }
}