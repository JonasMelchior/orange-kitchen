package com.application.repository;

import com.application.entity.kitchen.store.Beverage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeverageRepository extends CrudRepository<Beverage, Long> {}
