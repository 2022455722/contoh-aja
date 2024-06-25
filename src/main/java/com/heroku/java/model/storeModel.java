package com.heroku.java.model;

import java.math.BigDecimal;

public class storeModel {
    
    private Long storeID;
    private String businessID;
    private String storeName;
    private String licenseNumber;
    private String address;
    private BigDecimal utilityBill;

    // Getters and Setters
    public Long getStoreID() {
        return storeID;
    }

    public void setStoreID(Long storeID) {
        this.storeID = storeID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getUtilityBill() {
        return utilityBill;
    }

    public void setUtilityBill(BigDecimal utilityBill) {
        this.utilityBill = utilityBill;
    }
}
