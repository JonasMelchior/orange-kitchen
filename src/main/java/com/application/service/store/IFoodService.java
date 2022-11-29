package com.application.service.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Food;
import com.vaadin.flow.component.html.Image;

import java.util.List;
import java.util.Optional;

public interface IFoodService {

    List<Food> findAll();
    Optional<Food> findById(Long id);
    void save(Food food);
    void saveAll(List<Food> foods);
    void delete(Food food);
    Image generateFoodImage(Food food);
}
