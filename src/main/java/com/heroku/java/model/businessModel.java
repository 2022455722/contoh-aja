package com.heroku.java.model;

public class businessModel { 

    private String businessID;
    private String ownerName;
    private String ownerPhone;
    private String ownerNRIC;
    private String ownerEmail;
    private String businessType;

    // Getters and setters
    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerNRIC() {
        return ownerNRIC;
    }

    public void setOwnerNRIC(String ownerNRIC) {
        this.ownerNRIC = ownerNRIC;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

        public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}