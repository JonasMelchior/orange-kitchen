package com.application.views.kitchenstore;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.BeverageService;
import com.application.service.store.IBeverageService;
import com.application.service.store.IPurchaseService;
import com.application.service.store.PurchaseService;
import com.application.views.MainLayout;
import com.application.views.components.store.BeverageForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Dial;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import java.util.Date;
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


        FlexLayout beveragesLayout = new FlexLayout(generateBeveragesLayout());

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

            Button buyButton = new Button("Buy", new Icon(VaadinIcon.DOLLAR));
            buyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

            buyButton.addClickListener(buy -> {
                buyDialog.removeAll();
                buyDialog.add(createBuyItemDialogLayout(beverage));
                buyDialog.open();
            });


            Button editButton = new Button("Edit Item", edit -> {
               beverageForm.editBeverage(beverage);
               beverageForm.setVisible(true);
            });

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), delete -> {
                beverageService.deleteBeverage(beverage);
                UI.getCurrent().getPage().reload();
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);


            HorizontalLayout buttonLayout = new HorizontalLayout(buyButton, editButton, deleteButton);
            buttonLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

            beverageLayout.add(
                    brand,
                    description,
                    percentage,
                    orangeFactor,
                    price,
                    buttonLayout
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

        HorizontalLayout row1 = new HorizontalLayout(nameField, roomNumberField);
        row1.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        IntegerField phoneNumberField = new IntegerField("Phone Number");
        phoneNumberField.setWidthFull();

        IntegerField quantity = new IntegerField("Quantity");
        quantity.setHasControls(true);
        quantity.setWidthFull();

        Button finishButton = new Button("Finish", finish -> {

            purchaseService.savePurchase(new Purchase(
                    phoneNumberField.getValue(),
                    roomNumberField.getValue(),
                    nameField.getValue(),
                    beverage.getPrice(),
                    beverage.getBrand(),
                    quantity.getValue(),
                    new java.sql.Date(System.currentTimeMillis())
            ));
            Notification.show("Purchase successful. You will be notified regarding payment at the end of the month :)");
            buyDialog.close();
        });

        VerticalLayout dialogLayout = new VerticalLayout(row1, phoneNumberField, quantity, finishButton);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return dialogLayout;
    }
}
