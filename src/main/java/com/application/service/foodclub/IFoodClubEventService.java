package com.application.service.foodclub;

import com.application.entity.kitchen.club.FoodClubEvent;
import com.vaadin.flow.component.html.Image;

import java.util.List;
import java.util.Optional;

public interface IFoodClubEventService {

    List<FoodClubEvent> findAll();
    void saveFoodClubEvent(FoodClubEvent foodClubEvent);
    void updateFoodClubEvent(FoodClubEvent foodClubEvent);
    void deleteFoodClubEvent(FoodClubEvent foodClubEvent);
    Optional<FoodClubEvent> findFoodClubEventById(Long id);
    Image generateFoodImage(FoodClubEvent foodClubEvent);
}
