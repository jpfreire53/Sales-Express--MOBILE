package com.example.salesexpress.model;

import java.io.Serializable;

public class ProductModel implements Serializable {

        public String id;
        public String name;
        public String description;
        public String sku;

    public ProductModel(String name, String description, String sku) {
        this.name = name;
        this.description = description;
        this.sku = sku;
    }

    public ProductModel(String id, String name, String description, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public ProductModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
