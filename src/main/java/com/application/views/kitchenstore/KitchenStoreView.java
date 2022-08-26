package com.application.views.kitchenstore;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.BeverageService;
import com.application.service.store.IBeverageService;
import com.application.service.store.IPurchaseService;
import com.application.service.store.PurchaseService;
import com.application.views.MainLayout;
import com.application.views.components.store.BeverageForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Dial;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@PageTitle("Kitchen Store")
@Route(value = "kitchen-store", layout = MainLayout.class)
public class KitchenStoreView extends VerticalLayout {
    BeverageForm beverageForm;

    private IBeverageService beverageService;
    private IPurchaseService purchaseService;

    Dialog buyDialog = new Dialog();

    public KitchenStoreView(@Autowired BeverageService beverageService, @Autowired PurchaseService purchaseService) {
        beverageForm = new BeverageForm(beverageService);
        this.beverageService = beverageService;
        this.purchaseService = purchaseService;

        Button testButton = new Button("Add Store Item (So far only beverages)");
        testButton.addClickListener(click -> {
            beverageForm.setVisible(!beverageForm.isVisible());
        });


        VerticalLayout beveragesLayout = new VerticalLayout(generateBeveragesLayout());

        HorizontalLayout mainLayout = new HorizontalLayout(beveragesLayout, beverageForm);
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        mainLayout.setFlexGrow(2, beveragesLayout);
        mainLayout.setFlexGrow(1, beverageForm);

        add(testButton, mainLayout);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }


    private FlexLayout generateBeveragesLayout() {
        FlexLayout beveragesLayout = new FlexLayout();
        beveragesLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        beveragesLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        List<Beverage> beverages = beverageService.findAll();

        for (Beverage beverage : beverages) {
            Image image = beverageService.generateBeverageImage(beverage);
            image.setWidth("300px");
            image.setHeight("300px");

            VerticalLayout beverageLayout = new VerticalLayout(image);

            Div brand = new Div();
            brand.setText("Brand: " + beverage.getBrand());

            Div description = new Div();
            description.setText("Description: " + beverage.getDescription());

            Div percentage = new Div();
            percentage.setText("Percentage: " + beverage.getPercentage() + " %");

            Checkbox orangeFactor = new Checkbox("Orange Factor");
            orangeFactor.setValue(beverage.isOrangeFactor());
            orangeFactor.setReadOnly(true);

            Div price = new Div();
            price.setText("Price: " + beverage.getPrice() + " DKK");

            Button buyButton = new Button("Buy", buy -> {
                buyDialog.removeAll();
                buyDialog.add(createBuyItemDialogLayout(beverage));
                buyDialog.open();
            });

            beverageLayout.add(
                    brand,
                    description,
                    percentage,
                    orangeFactor,
                    price,
                    buyButton
            );
            beverageLayout.addClassNames("layout-with-border-store-item");
            beveragesLayout.setFlexBasis("200px", beverageLayout);
            beveragesLayout.add(beverageLayout);
        }

        return beveragesLayout;
    }



    private VerticalLayout createBuyItemDialogLayout(Beverage beverage) {
        TextField nameField = new TextField("Name");

        IntegerField roomNumberField = new IntegerField("Room Number");
        roomNumberField.setHelperText("E.g. 8 or 16");

        IntegerField phoneNumberField = new IntegerField("Phone Number");


        Button finishButton = new Button("Finish", finish -> {

            purchaseService.savePurchase(new Purchase(
                    roomNumberField.getValue(),
                    nameField.getValue(),
                    beverage.getPrice(),
                    phoneNumberField.getValue()
            ));
            Notification.show("Purchase successful. You will be notified regarding payment at the end of the month :)");
            buyDialog.close();
        });

        VerticalLayout dialogLayout = new VerticalLayout(nameField, roomNumberField, phoneNumberField, finishButton);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return dialogLayout;
    }
}
