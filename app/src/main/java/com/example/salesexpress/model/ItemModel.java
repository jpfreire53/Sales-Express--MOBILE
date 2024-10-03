package com.example.salesexpress.model;

import java.io.Serializable;

public class ItemModel implements Serializable {

    public String id;
    public String description;
    public String sales_id;


    public ItemModel(String id, String description, String sales_id) {
        this.id = id;
        this.description = description;
        this.sales_id = sales_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

}