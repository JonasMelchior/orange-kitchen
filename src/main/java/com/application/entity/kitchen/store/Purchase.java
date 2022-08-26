package com.application.entity.kitchen.store;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Purchase(Long id, int roomNumber, String name, double debt) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.name = name;
        this.purchaseAmount = debt;
    }

    public Purchase(int roomNumber, String name, double debt, int phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.roomNumber = roomNumber;
        this.name = name;
        this.purchaseAmount = debt;
    }

    public Purchase() {
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
