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
//    private int phoneNumber;
    private int roomNumber;

//    private String name;

    private double purchaseAmount;

    private String brand;
    @Column(columnDefinition = "integer default 1")
    private int quantity;

    private java.sql.Date date;

    public Purchase(int roomNumber, double purchaseAmount, String brand, int quantity, Date date) {
        this.roomNumber = roomNumber;
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


    public double getPurchaseAmount() {
        return purchaseAmount;
    }


    public String getBrand() {
        return brand;
    }


    public int getRoomNumber() {
        return roomNumber;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
