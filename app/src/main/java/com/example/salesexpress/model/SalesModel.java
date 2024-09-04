package com.example.salesexpress.model;

import java.io.Serializable;

public class SalesModel implements Serializable {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private double value;
    private double moneyChange;
    private boolean isReprocessed;
    private boolean isSend;


    public SalesModel(String  id, String name, String cpf, String email, double value, double moneyChange) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.value = value;
        this.moneyChange = moneyChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMoneyChange() {
        return moneyChange;
    }

    public void setMoneyChange(double moneyChange) {
        this.moneyChange = moneyChange;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReprocessed() {
        return isReprocessed;
    }

    public void setReprocessed(boolean reprocessed) {
        isReprocessed = reprocessed;
    }


    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }


}
