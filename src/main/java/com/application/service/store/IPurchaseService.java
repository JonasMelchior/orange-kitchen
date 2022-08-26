package com.application.service.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Purchase;

import java.util.List;
import java.util.Optional;

public interface IPurchaseService {
    List<Purchase> findAll();
    void savePurchase(Purchase purchase);
    void deletePurchase(Purchase purchase);
    Optional<Purchase> findBPurchase(Long id);
}
