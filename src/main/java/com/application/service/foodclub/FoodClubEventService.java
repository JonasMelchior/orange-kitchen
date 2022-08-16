package com.application.service.foodclub;

import com.application.entity.FoodClubEvent;
import com.application.repository.FoodClubEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
