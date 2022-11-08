package com.application.service.foodclub;

import com.application.entity.kitchen.club.ArchivedRecipe;
import com.application.entity.kitchen.club.FoodClubEvent;
import com.application.repository.ArchivedRecipeRepository;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArchivedRecipeService implements IArchivedRecipeService{

    @Autowired
    private ArchivedRecipeRepository repository;

    @Override
    public List<ArchivedRecipe> findAll() {
        return (List<ArchivedRecipe>) repository.findAll();
    }

    @Override
    public Optional<ArchivedRecipe> findArchivedRecipeById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteArchivedRecipe(ArchivedRecipe archivedRecipe) {
        repository.delete(archivedRecipe);
    }

    @Override
    public void saveArchivedRecipe(ArchivedRecipe archivedRecipe) {
        List<ArchivedRecipe> archivedRecipes = findAll();

        boolean alreadyExists = false;
        for (ArchivedRecipe recipe : archivedRecipes) {
            if (archivedRecipe.getRecipe().equals(recipe.getRecipe()) && archivedRecipe.getFoodPicture().equals(recipe.getFoodPicture())) {
                alreadyExists = true;
                Notification notification = Notification.show("Recipe already exists");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }

        if (!alreadyExists) {
            repository.save(archivedRecipe);
            Notification notification = Notification.show("Recipe successfully archived!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }

    }



    @Override
    public Image generateFoodImage(ArchivedRecipe archivedRecipe) {
        Long id = archivedRecipe.getId();
        StreamResource resource = new StreamResource("event", () -> {
            Optional<ArchivedRecipe> attached = findArchivedRecipeById(id);
            return new ByteArrayInputStream(attached.get().getFoodPicture());
        });

        resource.setContentType("image/png");
        return new Image(resource, "food-picture");
    }
}
