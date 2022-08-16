package com.application.repository;

import com.application.entity.FoodClubEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodClubEventRepository extends CrudRepository<FoodClubEvent, Long> { }
