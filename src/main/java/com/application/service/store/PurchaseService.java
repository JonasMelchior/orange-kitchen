package com.application.service.store;

import com.application.entity.kitchen.store.Purchase;
import com.application.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
