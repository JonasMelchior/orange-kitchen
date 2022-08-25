package com.application.entity.kitchen.store;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Entity
@Table(name = "kitchen_store_items_beer")
@EnableAutoConfiguration
public class Beverage extends KitchenStoreItem{

    private String brand;
    private String description;
    private int percentage;
    private boolean orangeFactor;

    public Beverage(String brand, String description, int percentage, boolean orangeFactor) {
        this.brand = brand;
        this.description = description;
        this.percentage = percentage;
        this.orangeFactor = orangeFactor;
    }

    public Beverage() {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public boolean isOrangeFactor() {
        return orangeFactor;
    }

    public void setOrangeFactor(boolean orangeFactor) {
        this.orangeFactor = orangeFactor;
    }
}
