//package org.example;
//
//import java.io.IOException;
//
//public class IrodsBuilder {
//    private String address;
//    private String port;
//    private String version;
//    private User user;
//
//    public IrodsBuilder address(String address) {
//        this.address = address;
//        return this;
//    }
//
//    public IrodsBuilder port(String port) {
//        this.port = port;
//        return this;
//    }
//
//    public IrodsBuilder version(String version) {
//        this.version = version;
//        return this;
//    }
//
//    public IrodsBuilder user(User user) {
//        this.user = user;
//        return this;
//    }
//
//    public IrodsClient build() throws IOException, InterruptedException {
//        IrodsClient client = new IrodsClient(this);
//        if (this.user != null) {
//            client.authenticate(user);
//        }
//        return client;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public String getPort() {
//        return port;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public User getUser() {
//        return user;
//    }
//}