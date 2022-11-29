package com.application.views.components.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Food;
import com.application.service.store.BeverageService;
import com.application.service.store.FoodService;
import com.application.service.store.IBeverageService;
import com.application.service.store.IFoodService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodForm extends FormLayout {

    private IFoodService service;

    ComboBox<String> foodType = new ComboBox<>("Food Type");
    TextField description = new TextField("Description");
    NumberField price = new NumberField("Price");
    Upload imageUpload = new Upload();

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Food food = new Food();

    public FoodForm(@Autowired FoodService service) {
        this.service = service;
        foodType.setItems(getFoodTypes());
        foodType.setAllowCustomValue(false);

        Div percentSuffix = new Div();
        percentSuffix.setText("%");



        Div dkkSuffix = new Div();
        dkkSuffix.setText("DKK");
        price.setSuffixComponent(dkkSuffix);

        configureImageUploader();

        add(
                foodType,
                description,
                price,
                imageUpload,
                createButtonsLayout());
    }

    private List<String> getFoodTypes() {
        List<String> strings = new ArrayList<>();

        strings.add("Pizza");
        strings.add("Ramen");
        strings.add("Fries");
        strings.add("Burgers");
        strings.add("Ice Cream");

        return strings;
    }

    private void configureImageUploader() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        imageUpload = new Upload(buffer);
        imageUpload.setAcceptedFileTypes("image/jpeg","image/jpg", "image/png", "image/gif");


        imageUpload.addSucceededListener(e -> {
            String attachmentName = e.getFileName();
            try {
                // The image can be jpg png or gif, but we store it always as png file in this example
                BufferedImage inputImage = ImageIO.read(buffer.getInputStream(attachmentName));
                ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
                ImageIO.write(inputImage, "png", pngContent);
                food.setFoodPicture(pngContent.toByteArray());
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
    }



    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            food.setFoodType(foodType.getValue());
            food.setDescription(description.getValue());
            food.setPrice(price.getValue());

            service.save(food);
            UI.getCurrent().getPage().reload();
        });

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);


        close.addClickShortcut(Key.ESCAPE);
        close.addClickListener(close -> {
            this.setVisible(false);
        });

        return new HorizontalLayout(save, close);

    }



    public void editFood(Food beverafoodge) {
        this.food = food;
        if (food.getFoodType() != null) {
            foodType.setValue(food.getFoodType());
        }
        if (food.getDescription() != null) {
            description.setValue(food.getDescription());
        }
        price.setValue(food.getPrice());
    }
}
