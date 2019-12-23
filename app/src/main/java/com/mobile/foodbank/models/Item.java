package com.mobile.foodbank.models;

public class Item {
    private String itemName;
    private String shelfNumber;
    private int threshold;
    private int quantity;
    private String quantityUnit;

    public Item() {

    }

    public Item(String itemName, String shelfNumber, int threshold, int quantity, String quantityUnit) {
        this.itemName = itemName;
        this.shelfNumber = shelfNumber;
        this.threshold = threshold;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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
}
