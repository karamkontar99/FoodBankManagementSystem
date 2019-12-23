package com.mobile.foodbank.models;

public class Donation {

    String key;
    private String itemName;
    private int quantity;
    private String quantityUnit;
    private String donorUsername;
    private String image;
    private String date;
    private String status;

    public Donation() {

    }

    public Donation(String itemName, int quantity, String quantityUnit, String donorUsername, String image, String date, String status) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
        this.donorUsername = donorUsername;
        this.image = image;
        this.date = date;
        this.status = status;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getDonorUsername() {
        return donorUsername;
    }

    public void setDonorUsername(String donorUsername) {
        this.donorUsername = donorUsername;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
