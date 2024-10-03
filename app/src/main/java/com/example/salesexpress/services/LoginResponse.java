package com.example.salesexpress.services;

import com.example.salesexpress.model.UserModelResponse;

public class LoginResponse {

    private String type;
    private String message;
    private UserModelResponse user;

    public LoginResponse(String type, String message, UserModelResponse user) {
        this.type = type;
        this.message = message;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public UserModelResponse getUser() {
        return user;
    }


}
