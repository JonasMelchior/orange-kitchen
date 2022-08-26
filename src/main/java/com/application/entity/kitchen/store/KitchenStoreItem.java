package com.application.entity.kitchen.store;

import javax.persistence.*;

@MappedSuperclass
public abstract class KitchenStoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getFoodPicture() {
        return picture;
    }

    public void setFoodPicture(byte[] picture) {
        this.picture = picture;
    }
}

