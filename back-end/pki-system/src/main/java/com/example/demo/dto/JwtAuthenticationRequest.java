package com.example.demo.dto;

public class JwtAuthenticationRequest {

    private String mail;
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String mail, String password) {
        this.setMail(mail);
        this.setPassword(password);
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
