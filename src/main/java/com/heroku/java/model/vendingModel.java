package com.heroku.java.model;

public class vendingModel {
    
    private Long vendingID;
    private String businessID;
    private String goodsType;
    private int quantity;
    private int quantitySold;
    private String location;

    // Getters and Setters
    public Long getVendingID() {
        return vendingID;
    }

    public void setVendingID(Long vendingID) {
        this.vendingID = vendingID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
