package com.example.salesexpress.services;

public class RegisterSalesResponse {
    private String message;
    private String type;
    private String token;

    private String Id;

    public RegisterSalesResponse(String message, String type, String token) {
        this.message = message;
        this.type = type;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

}
