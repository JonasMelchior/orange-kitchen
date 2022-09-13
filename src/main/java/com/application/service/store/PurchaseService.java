package com.application.service.store;

import com.application.entity.kitchen.store.DailyRevenue;
import com.application.entity.kitchen.store.Purchase;
import com.application.entity.kitchen.store.PurchasedBrand;
import com.application.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PurchaseService implements IPurchaseService{

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public List<Purchase> findAll() {
        return (List<Purchase>) purchaseRepository.findAll();
    }

    @Override
    public void savePurchase(Purchase purchase) {
        purchaseRepository.save(purchase);
    }

    @Override
    public void deletePurchase(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }

    @Override
    public Optional<Purchase> findBPurchase(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public List<DailyRevenue> getDailyRevenue() {
        List<Purchase> purchases = findAll();

        List<DailyRevenue> dailyRevenues = new ArrayList<>();



        for (int i = 0; i < purchases.size(); i++) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {
                DailyRevenue dailyRevenue = new DailyRevenue(purchases.get(i).getDate().getDate(), purchases.get(i).getPurchaseAmount());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getDate().getDate() == purchases.get(i).getDate().getDate() && i != j) {
                        dailyRevenue.setRevenue(dailyRevenue.getRevenue() + purchases.get(j).getPurchaseAmount());
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                dailyRevenues.add(dailyRevenue);
            }
        }

        return dailyRevenues;
    }

    @Override
    public List<PurchasedBrand> getRevenueBrandBased() {
        List<PurchasedBrand> purchasedBrands = new ArrayList<>();

        List<Purchase> purchases = findAll();

        for (int i = 0; i < purchases.size(); i++) {


            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchases.get(i).getDate().getMonth()) {
                PurchasedBrand purchasedBrand = new PurchasedBrand(purchases.get(i).getBrand(), purchases.get(i).getQuantity());
                for (int j = 0; j < purchases.size(); j++) {
                    if (purchases.get(j).getBrand().equals(purchases.get(i).getBrand()) && i != j) {
                        purchasedBrand.setPurchasedAmount(purchasedBrand.getPurchasedAmount() + purchases.get(j).getQuantity());
                        purchases.remove(j--);
                    }
                }
                purchases.remove(i--);
                purchasedBrands.add(purchasedBrand);
            }
        }

        return purchasedBrands;
    }

    @Override
    public double getTotalRevenueCurrentMonth() {
        List<Purchase> purchases = findAll();

        double totalRevenue = 0;

        for (Purchase purchase : purchases) {
            if (java.time.LocalDateTime.now().getMonthValue() - 1 == purchase.getDate().getMonth()) {
                totalRevenue += purchase.getPurchaseAmount() * purchase.getQuantity();
            }
        }

        return totalRevenue;
    }
}
