package org.example;

public class User {
    private final String username;
    private final String password;
    private String authToken;

    public User (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }

   // since token can potentially expire, would want to renew it
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
