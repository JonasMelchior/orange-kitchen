package com.application.entity.kitchen.club;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;

@Entity
@Table(name = "food_club_archived_recipe")
@EnableAutoConfiguration
public class ArchivedRecipe {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipe;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] foodPicture;

    public ArchivedRecipe(String recipe, byte[] foodPicture) {
        this.recipe = recipe;
        this.foodPicture = foodPicture;
    }

    public ArchivedRecipe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public byte[] getFoodPicture() {
        return foodPicture;
    }

    public void setFoodPicture(byte[] foodPicture) {
        this.foodPicture = foodPicture;
    }
}
