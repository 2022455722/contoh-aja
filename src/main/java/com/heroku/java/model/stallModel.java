package com.heroku.java.model;

public class stallModel {
    
    private Long stallID;
    private String businessID;
    private String stallName;
    private String licenseNumber;
    private String productSpecification;
    private String location;

    // Getters and Setters
    public Long getStallID() {
        return stallID;
    }

    public void setStallID(Long stallID) {
        this.stallID = stallID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
