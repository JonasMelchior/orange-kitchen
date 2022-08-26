package com.application.views.components.store;

import com.application.entity.kitchen.store.Beverage;
import com.application.service.store.IBeverageService;
import com.application.service.store.BeverageService;
import com.application.views.kitchenstore.KitchenStoreView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

public class BeverageForm extends FormLayout {


    private IBeverageService service;

    ComboBox<String> brand = new ComboBox<>("Brand");
    TextField description = new TextField("Description");
    NumberField percentage = new NumberField("Percentage");
    Checkbox orangeFactor = new Checkbox("Orange Factor");
    NumberField price = new NumberField("Price");
    Upload imageUpload = new Upload();

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Beverage beverage = new Beverage();

    public BeverageForm(@Autowired BeverageService service) {
        this.service = service;
        brand.setItems(getBeerBrands());
        brand.setAllowCustomValue(true);

        Div percentSuffix = new Div();
        percentSuffix.setText("%");
        percentage.setSuffixComponent(percentSuffix);
        percentage.setMin(0);
        percentage.setMax(100);

        Div dkkSuffix = new Div();
        dkkSuffix.setText("DKK");
        price.setSuffixComponent(dkkSuffix);

        configureImageUploader();

        add(
                brand,
                description,
                percentage,
                orangeFactor,
                price,
                imageUpload,
                createButtonsLayout());
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
                beverage.setFoodPicture(pngContent.toByteArray());
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
    }



    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            beverage.setBrand(brand.getValue());
            beverage.setDescription(description.getValue());
            beverage.setPercentage(percentage.getValue());
            beverage.setOrangeFactor(orangeFactor.getValue());
            beverage.setPrice(price.getValue());

            service.saveBeverage(beverage);
            UI.getCurrent().getPage().reload();
        });

        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);


        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close);

    }

    private List<String> getBeerBrands() {
        List<String> strings = new ArrayList<>();

        strings.add("Carlsberg");
        strings.add("Heineken");
        strings.add("Tuborg");
        strings.add("Royal");

        return strings;
    }
}
