//package org.example;
//
//import java.io.IOException;
//
//public class IrodsBuilder {
//    private String address;
//    private String port;
//    private String version;
//
//    /**
//     * Instantiates a new builder
//     */
//    IrodsBuilder() {
//    }
//
//
//    /**
//     * Sets server address
//     * @param address Server address
//     * @return IrodsBuilder instance for chaining
//     */
//    public IrodsBuilder address(String address) {
//        this.address = address;
//        return this;
//    }
//
//    /**
//     * Sets server port
//     * @param port Server port
//     * @return IrodsBuilder instance for chaining
//     */
//    public IrodsBuilder port(String port) {
//        this.port = port;
//        return this;
//    }
//
//    /**
//     * Sets API version
//     * @param version API version
//     * @return IrodsBuilder instance for chaining
//     */
//    public IrodsBuilder version(String version) {
//        this.version = version;
//        return this;
//    }
//
//    /**
//     * Builds the IrodsClient instance. If user is present, preforms authentication
//     * @return Constructed IrodsClient instance
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    public IrodsClient build() throws IOException, InterruptedException {
//        return new IrodsClient(address, port, version);
//    }
//
//}
