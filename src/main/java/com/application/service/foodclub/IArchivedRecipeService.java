package com.application.service.foodclub;


import com.application.entity.kitchen.club.ArchivedRecipe;
import com.application.entity.kitchen.club.FoodClubEvent;
import com.vaadin.flow.component.html.Image;

import java.util.List;
import java.util.Optional;

public interface IArchivedRecipeService {
    List<ArchivedRecipe> findAll();
    Optional<ArchivedRecipe> findArchivedRecipeById(Long id);
    void deleteArchivedRecipe(ArchivedRecipe archivedRecipe);
    void saveArchivedRecipe(ArchivedRecipe archivedRecipe);
    Image generateFoodImage(ArchivedRecipe ArchivedRecipe);
}
