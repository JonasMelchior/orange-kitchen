/*
package com.application.entity;

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

    public KitchenStoreItem(Long id, double price) {
        this.id = id;
        this.price = price;
    }

    public KitchenStoreItem() {
    }

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
}
*/

