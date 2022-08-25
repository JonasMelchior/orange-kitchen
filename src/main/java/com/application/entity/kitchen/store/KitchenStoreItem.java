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
    private byte[] foodPicture;

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
        return foodPicture;
    }

    public void setFoodPicture(byte[] foodPicture) {
        this.foodPicture = foodPicture;
    }
}

