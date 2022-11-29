package com.application.service.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Food;
import com.application.repository.FoodRepository;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService implements IFoodService{


    @Autowired
    private FoodRepository repository;

    @Override
    public List<Food> findAll() {
        return (List<Food>) repository.findAll();
    }

    @Override
    public Optional<Food> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(Food food) {
        repository.save(food);
    }

    @Override
    public void saveAll(List<Food> foods) {
        repository.saveAll(foods);
    }

    @Override
    public void delete(Food food) {
        repository.delete(food);
    }

    @Override
    public Image generateFoodImage(Food food) {
        Long id = food.getId();

        StreamResource resource = new StreamResource("event", () -> {
            Optional<Food> attached = findById(id);
            return new ByteArrayInputStream(attached.get().getFoodPicture());
        });

        resource.setContentType("image/png");
        return new Image(resource, "food-picture");
    }
}
