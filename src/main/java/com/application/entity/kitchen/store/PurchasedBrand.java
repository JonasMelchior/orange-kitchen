package com.application.entity.kitchen.store;

public class PurchasedBrand {
    String brand;
    int purchasedAmount;

    public PurchasedBrand(String brand, int purchasedAmount) {
        this.brand = brand;
        this.purchasedAmount = purchasedAmount;
    }

    public PurchasedBrand() {

    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPurchasedAmount() {
        return purchasedAmount;
    }

    public void setPurchasedAmount(int purchasedAmount) {
        this.purchasedAmount = purchasedAmount;
    }
}
