package com.application.entity.kitchen.store;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Entity
@Table(name = "kitchen_store_items_beverage")
@EnableAutoConfiguration
public class Beverage extends KitchenStoreItem{

    private String brand;
    private String description;
    private double percentage;
    private boolean orangeFactor;

    public Beverage(String brand, String description, double percentage, boolean orangeFactor) {
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public boolean isOrangeFactor() {
        return orangeFactor;
    }

    public void setOrangeFactor(boolean orangeFactor) {
        this.orangeFactor = orangeFactor;
    }
}
