package com.application.entity.kitchen.store;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "kitchen_store_user_purchases")
@EnableAutoConfiguration
public class Purchase {

    @Id
    @GeneratedValue
    private Long id;
    private int phoneNumber;
    private int roomNumber;

    private String name;

    private double purchaseAmount;

    private String brand;
    @Column(columnDefinition = "integer default 1")
    private int quantity;

    private java.sql.Date date;


    public Purchase(Long id, int phoneNumber, int roomNumber, String name, double purchaseAmount, String brand, int quantity) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.name = name;
        this.purchaseAmount = purchaseAmount;
        this.brand = brand;
        this.quantity = quantity;
    }

    public Purchase(int roomNumber, String name, double debt, int phoneNumber, String brand, int quantity) {
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.name = name;
        this.purchaseAmount = debt;
        this.brand = brand;
        this.quantity = quantity;
    }

    public Purchase(int phoneNumber, int roomNumber, String name, double purchaseAmount, String brand, int quantity, Date date) {
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.name = name;
        this.purchaseAmount = purchaseAmount;
        this.brand = brand;
        this.quantity = quantity;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Purchase() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDebt() {
        return purchaseAmount;
    }

    public void setDebt(double debt) {
        this.purchaseAmount = debt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
