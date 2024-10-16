package com.example.salesexpress.model;

public class UserModelResponse {
    private int id;
    private String user;
    private String name;
    private String company;
    private String cnpj;
    private String password;
    private String userType;
    private String role;

    // Construtor
    public UserModelResponse(int id, String user, String name, String company, String cnpj, String password, String userType, String role) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.company = company;
        this.cnpj = cnpj;
        this.password = password;
        this.userType = userType;
        this.role = role;
    }

    public UserModelResponse(){}

    // Getters
    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
