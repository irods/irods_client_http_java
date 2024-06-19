package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class IrodsClient {

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();;
    private User user;

    /**
     * Enforces the use of the builder. Making it private ensures that users cannot create an instance of this.
     * @param builder Builder instance with configuration details
     */
    private IrodsClient(Builder builder) {
        this.baseUrl = "http://" + builder.address + ":" + builder.port + "/irods-http-api/" + builder.version;
        this.user = builder.user;
    }

    /**
     * Nested Builder class to construct IrodsClient instances
     */
    public static class Builder {
        private String address;
        private String port;
        private String version;
        private User user;

        /**
         * Sets server address
         * @param address Server address
         * @return Builder instance for chaining
         */
        public Builder address(String address) {
            this.address = address;
            return this;
        }

        /**
         * Sets server port
         * @param port Server port
         * @return Builder instance for chaining
         */
        public Builder port(String port) {
            this.port = port;
            return this;
        }

        /**
         * Sets API version
         * @param version API version
         * @return Builder instance for chaining
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * Sets user for authenticated requests
         * @param user User that will be authenticated
         * @return Builder instance for chaining
         */
        public Builder user(User user) {
            this.user = user;
            return this;
        }

        /**
         * Builds the IrodsClient instance. If user is present, preforms authentication
         * @return Constructed IrodsClient instance
         * @throws IOException
         * @throws InterruptedException
         */
        public IrodsClient build() throws IOException, InterruptedException {
            IrodsClient client = new IrodsClient(this);
            if (this.user != null) {
                client.authenticate(this.user);
            }
            return client;
        }
    }

//    public irodsClient(String address, String port, String version) {
//        this.client = HttpClient.newHttpClient();
//        this.baseUrl = "http://" + address + ":" + port + "/irods-http-api/" + version;
//        //this.baseUrl = "http://52.91.145.195:8888/irods-http-api/0.3.0";
//    }
    //private static final String url = "http://52.91.145.195:8888/irods-http-api/0.3.0";

//    public IrodsClientrodsClient(String address, String port, String version, User user) throws IOException, InterruptedException {
//        this(address, port, version);
//        this.user = user;
//        authenticate(user);
//    }

    /**
     * Authenticates user by sending the following POST reqeust
     * curl -X POST -u rods:rods http://localhost:9000/irods-http-api/0.3.0/authenticate
     *
     * @param user The user object that is being authenticated
     * @throws IOException
     * @throws InterruptedException
     */
    private void authenticate (User user) throws IOException, InterruptedException {
        // creating authentication header
        String auth = user.getUsername() + ":" + user.getPassword();
        // encodes user and password into a suitable format for HTTP basic authentication
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/authenticate"))
                .header("Authorization", authHeader)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            user.setAuthToken(response.body());
        } else {
            throw new RuntimeException("Failed to authenticate: " + response.statusCode());
        }
    }

    /**
     * Sends request to /info endpoint and parses the response
     * @return Info objected parsed from the response JSON
     * @throws IOException
     * @throws InterruptedException
     */
    public Info info() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/info"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //parse JSON into objects
        ObjectMapper mapper = new ObjectMapper();
        Info info = mapper.readValue(response.body(), Info.class);
        return (info);
    }

    // response code of 401 means attempting to use an expired or invalid token
    // may need to reauthenticate

}