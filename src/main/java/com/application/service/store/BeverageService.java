package com.application.service.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.KitchenStoreItem;
import com.application.repository.BeverageRepository;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class BeverageService implements IBeverageService {


    private BeverageRepository repository;

    public BeverageService(@Autowired BeverageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beverage> findAll() {
        return (List<Beverage>) repository.findAll();
    }

    @Override
    public void saveBeverage(Beverage beverage) {
        repository.save(beverage);
    }

    @Override
    public void deleteBeverage(Beverage beverage) {
        repository.delete(beverage);
    }

    @Override
    public Optional<Beverage> findBeverage(Long id) {
        return repository.findById(id);
    }

    @Override
    public Image generateBeverageImage(Beverage beverage) {
        Long id = beverage.getId();

        StreamResource resource = new StreamResource("event", () -> {
            Optional<Beverage> attached = findBeverage(id);
            return new ByteArrayInputStream(attached.get().getFoodPicture());
        });

        resource.setContentType("image/png");
        return new Image(resource, "food-picture");
    }


}
