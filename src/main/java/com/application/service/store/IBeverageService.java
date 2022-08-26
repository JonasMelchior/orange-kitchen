package com.application.service.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.KitchenStoreItem;
import com.vaadin.flow.component.html.Image;

import java.util.List;
import java.util.Optional;

public interface IBeverageService {
    List<Beverage> findAll();
    void saveBeverage(Beverage beverage);

    void deleteBeverage(Beverage beverage);
    Optional<Beverage> findBeverage(Long id);
    Image generateBeverageImage(Beverage beverage);
}
