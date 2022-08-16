package com.application.service.foodclub;

import com.application.entity.FoodClubEvent;

import java.util.List;

public interface IFoodClubEventService {

    List<FoodClubEvent> findAll();
    void saveFoodClubEvent(FoodClubEvent foodClubEvent);
    void updateFoodClubEvent(FoodClubEvent foodClubEvent);
    void deleteFoodClubEvent(FoodClubEvent foodClubEvent);
}
