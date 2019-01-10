package com.scraperclub.android.api.model;

public class UserCredentials {

    private String email;
    private String secret;

    public UserCredentials(String email, String secret) {
        this.email = email;
        this.secret = secret;
    }

    public String getEmail() {
        return email;
    }

    public String getSecret() {
        return secret;
    }
}
