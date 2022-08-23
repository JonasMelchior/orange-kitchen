package com.application.service.foodclub;

import com.application.entity.FoodClubEvent;
import com.application.repository.FoodClubEventRepository;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodClubEventService implements IFoodClubEventService{

    @Autowired
    private FoodClubEventRepository repository;

    @Override
    public List<FoodClubEvent> findAll() {
        List<FoodClubEvent> foodClubEventList = new ArrayList<>();
        repository.findAll().forEach(foodClubEventList::add);
        return foodClubEventList;
    }

    @Override
    public void saveFoodClubEvent(FoodClubEvent foodClubEvent) {
        repository.save(foodClubEvent);
    }

    @Override
    public void updateFoodClubEvent(FoodClubEvent foodClubEvent) {
        repository.save(foodClubEvent);
    }

    @Override
    public void deleteFoodClubEvent(FoodClubEvent foodClubEvent) {
        repository.delete(foodClubEvent);
    }

    @Override
    public Optional<FoodClubEvent> findFoodClubEventById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Image generateFoodImage(FoodClubEvent foodClubEvent) {
        Long id = foodClubEvent.getId();
        StreamResource resource = new StreamResource("event", () -> {
           Optional<FoodClubEvent> attached = findFoodClubEventById(id);
           return new ByteArrayInputStream(attached.get().getFoodPicture());
        });

        resource.setContentType("image/png");
        return new Image(resource, "food-picture");
    }
}
