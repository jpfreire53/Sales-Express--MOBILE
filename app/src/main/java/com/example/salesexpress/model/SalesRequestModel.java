package com.example.salesexpress.model;

import java.util.ArrayList;

public class SalesRequestModel {

    public SalesModel getSalesModel() {
        return salesModel;
    }

    public ArrayList<ItemModel> getItems() {
        return (ArrayList<ItemModel>) items;
    }

    private SalesModel salesModel;
    private ArrayList<ItemModel> items;

    public SalesRequestModel(SalesModel salesModel, ArrayList<ItemModel> items) {
        this.salesModel = salesModel;
        this.items = items;
    }
}
