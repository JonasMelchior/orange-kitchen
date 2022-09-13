package com.application.service.store;

import com.application.entity.kitchen.store.DailyRevenue;
import com.application.entity.kitchen.store.Purchase;
import com.application.entity.kitchen.store.PurchasedBrand;

import java.util.List;
import java.util.Optional;

public interface IPurchaseService {
    List<Purchase> findAll();
    void savePurchase(Purchase purchase);
    void deletePurchase(Purchase purchase);
    Optional<Purchase> findBPurchase(Long id);
    List<DailyRevenue> getDailyRevenue();
    List<PurchasedBrand> getRevenueBrandBased();
    double getTotalRevenueCurrentMonth();
}
