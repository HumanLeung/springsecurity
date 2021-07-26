package com.company.security.jwt;

public class EmailPassAuthenticationRequest {
    private String email;
    private String password;

    public EmailPassAuthenticationRequest() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
