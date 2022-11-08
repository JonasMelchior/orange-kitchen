package com.application.views.foodclub;

import com.application.entity.kitchen.club.ArchivedRecipe;
import com.application.entity.kitchen.club.FoodClubEvent;
import com.application.service.foodclub.IArchivedRecipeService;
import com.application.views.MainLayout;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@PageTitle("Archived Recipes")
@Route(value = "archived-recipes", layout = MainLayout.class)
public class ArchivedRecipeView extends VerticalLayout {

    @Autowired
    private IArchivedRecipeService recipeService;

    VerticalLayout mainLayout = new VerticalLayout();

    public ArchivedRecipeView() {
        add(mainLayout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    @PostConstruct
    public void postConstructor() {
        createRecipesLayout(recipeService.findAll());
    }

    private void createRecipesLayout(List<ArchivedRecipe> archivedRecipes) {
        for (ArchivedRecipe archivedRecipe : archivedRecipes) {
            TextArea recipeTextArea = new TextArea("Recipe");
            recipeTextArea.setValue(archivedRecipe.getRecipe());

            recipeTextArea.addValueChangeListener( write -> {
                recipeService.saveArchivedRecipe(archivedRecipe);
            });

            VerticalLayout imageContainer = new VerticalLayout();

            MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
            Upload upload = new Upload(buffer);
            upload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");

            upload.addSucceededListener(e -> {
                String attachmentName = e.getFileName();
                try {
                    // The image can be jpg png or gif, but we store it always as png file in this example
                    BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                    ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                    ImageIO.write(inputImage, "png", pngContent);
                    saveFoodPicture(pngContent.toByteArray(), archivedRecipe);
                    showImage(archivedRecipe, imageContainer);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            });
            showImage(archivedRecipe, imageContainer);
            VerticalLayout ImageLayout = new VerticalLayout(imageContainer, upload);

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> {
                recipeService.deleteArchivedRecipe(archivedRecipe);
                UI.getCurrent().getPage().reload();
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            HorizontalLayout recipeLayout = new HorizontalLayout(recipeTextArea, ImageLayout);
            recipeLayout.setDefaultVerticalComponentAlignment(Alignment.STRETCH);

            mainLayout.add(recipeLayout, deleteButton, new HorizontalGreyLine());
        }
    }

    private void saveFoodPicture(byte[] imageBytes, ArchivedRecipe archivedRecipe) {
        archivedRecipe.setFoodPicture(imageBytes);
        recipeService.saveArchivedRecipe(archivedRecipe);
    }

    private void showImage(ArchivedRecipe archivedRecipe, VerticalLayout imageContainer) {
        Image image = recipeService.generateFoodImage(archivedRecipe);
        image.setWidth("200px");
        image.setHeight("200px");
        imageContainer.removeAll();
        imageContainer.add(image);
    }

    @Tag("vaadin-line")
    public class HorizontalGreyLine extends Div {
        public HorizontalGreyLine() {
            getStyle().set("width","100%").set("border-top","3px solid grey");
        }
    }
}
