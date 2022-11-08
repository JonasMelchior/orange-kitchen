package com.application.repository;

import com.application.entity.kitchen.club.ArchivedRecipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivedRecipeRepository extends CrudRepository<ArchivedRecipe, Long> {
}
