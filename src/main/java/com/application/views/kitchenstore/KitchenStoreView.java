package com.application.views.kitchenstore;

import com.application.entity.kitchen.store.Beverage;
import com.application.entity.kitchen.store.Food;
import com.application.entity.kitchen.store.KitchenStoreItem;
import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.*;
import com.application.views.MainLayout;
import com.application.views.components.store.BeverageForm;
import com.application.views.components.store.FoodForm;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Dial;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PageTitle("Kitchen Store")
@Route(value = "kitchen-store", layout = MainLayout.class)
public class KitchenStoreView extends VerticalLayout {
    BeverageForm beverageForm;
    FoodForm foodForm;

    private IBeverageService beverageService;
    private IPurchaseService purchaseService;
    private IFoodService foodService;
    FormLayout formLayout = new FormLayout();
    FlexLayout beveragesLayout;
    FlexLayout foodsLayout;
    VerticalLayout storeItemsLayout;
    Dialog buyDialog = new Dialog();

    public KitchenStoreView(@Autowired BeverageService beverageService, @Autowired PurchaseService purchaseService, @Autowired FoodService foodService) {
        beverageForm = new BeverageForm(beverageService);
        beverageForm.setVisible(false);
        foodForm = new FoodForm(foodService);
        foodForm.setVisible(false);
        this.beverageService = beverageService;
        this.foodService = foodService;
        this.purchaseService = purchaseService;




        List<Beverage> beverages = beverageService.findAll();
        List<Food> foods = foodService.findAll();

        beveragesLayout = new FlexLayout(generateBeveragesLayout(beverages));
        foodsLayout = new FlexLayout(generateFoodsLayout(foods));

        storeItemsLayout = new VerticalLayout(beveragesLayout, new HorizontalGreyLine(), foodsLayout);



        HorizontalLayout mainLayout = new HorizontalLayout(storeItemsLayout, beverageForm);
        mainLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        mainLayout.setFlexGrow(2, storeItemsLayout);
        mainLayout.setFlexGrow(1, beveragesLayout);

        Button addBeverageItemButton = new Button("Add Beverage", new Icon(VaadinIcon.GLASS));
        addBeverageItemButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBeverageItemButton.addClickListener(click -> {
            mainLayout.remove(foodForm);
            mainLayout.add(beverageForm);
            beverageForm.setVisible(!beverageForm.isVisible());
        });

        Button addFoodItemButton = new Button("Add Food", new Icon(VaadinIcon.CUTLERY));
        addFoodItemButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addFoodItemButton.addClickListener( click -> {
            mainLayout.remove(beverageForm);
            mainLayout.add(foodForm);
            foodForm.setVisible(!foodForm.isVisible());
        });

        HorizontalLayout addStoreItemButtonLayout = new HorizontalLayout(addBeverageItemButton, addFoodItemButton);
        addStoreItemButtonLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        searchField.addValueChangeListener( event -> {
            List<Beverage> filteredBeverages = new ArrayList<>();
            List<Food> filteredFoods = new ArrayList<>();

            for (Beverage beverage : beverages) {
                if (beverage.getBrand().toLowerCase().contains(event.getValue().toLowerCase()) || beverage.getDescription().toLowerCase().contains(event.getValue().toLowerCase())) {
                    filteredBeverages.add(beverage);
                }
            }
            for (Food food : foods) {
                if (food.getFoodType().toLowerCase().contains(event.getValue().toLowerCase()) || food.getDescription().toLowerCase().contains(event.getValue().toLowerCase())) {
                    filteredFoods.add(food);
                }
            }

            beveragesLayout = new FlexLayout(generateBeveragesLayout(filteredBeverages));
            foodsLayout = new FlexLayout(generateFoodsLayout(filteredFoods));

            storeItemsLayout.removeAll();
            storeItemsLayout.add(beveragesLayout, new HorizontalGreyLine(), foodsLayout);
        });

        add(addStoreItemButtonLayout, searchField, mainLayout);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }


    private FlexLayout generateBeveragesLayout(List<Beverage> beverages) {
        FlexLayout beveragesLayout = new FlexLayout();
        beveragesLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        beveragesLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);


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

    List<Food> foods;
    private FlexLayout generateFoodsLayout(List<Food> foods) {
        FlexLayout foodsLayout = new FlexLayout();
        foodsLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        foodsLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);


        for (Food food : foods) {
            Image image = foodService.generateFoodImage(food);
            image.setWidth("300px");
            image.setHeight("300px");

            VerticalLayout foodLayout = new VerticalLayout(image);

            Div foodType = new Div();
            foodType.setText("Food Type: " + food.getFoodType());

            Div description = new Div();
            description.setText("Description: " + food.getDescription());

            Div price = new Div();
            price.setText("Price: " + food.getPrice() + " DKK");

            Button buyButton = new Button("Buy", new Icon(VaadinIcon.DOLLAR));
            buyButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

            buyButton.addClickListener(buy -> {
                buyDialog.removeAll();
                buyDialog.add(createBuyItemDialogLayout(food));
                buyDialog.open();
            });


            Button editButton = new Button("Edit Item", edit -> {
                foodForm.editFood(food);
                beverageForm.setVisible(false);
                foodForm.setVisible(true);
            });

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), delete -> {
                foodService.delete(food);
                UI.getCurrent().getPage().reload();
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);


            HorizontalLayout buttonLayout = new HorizontalLayout(buyButton, editButton, deleteButton);
            buttonLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

            foodLayout.add(
                    foodType,
                    description,
                    price,
                    buttonLayout
            );
            foodLayout.addClassNames("layout-with-border-store-item-food");
            foodsLayout.setFlexBasis("200px", foodLayout);
            foodsLayout.add(foodLayout);
        }
        return foodsLayout;
    }


    private VerticalLayout createBuyItemDialogLayout(KitchenStoreItem kitchenStoreItem) {
        IntegerField roomNumberField = new IntegerField("Room Number");

        IntegerField quantity = new IntegerField("Quantity");
        quantity.setHasControls(true);
        quantity.setWidthFull();

        roomNumberField.setRequiredIndicatorVisible(true);
        quantity.setRequiredIndicatorVisible(true);

        Button finishButton = new Button("Finish", finish -> {
            if (roomNumberField.isEmpty() || quantity.isEmpty()){
                Notification notification = Notification.show("You must fill out required fields");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            else {
                if (kitchenStoreItem.getClass() == Food.class) {
                    Food food = (Food) kitchenStoreItem;
                    purchaseService.savePurchase(new Purchase(
                            roomNumberField.getValue(),
                            food.getPrice(),
                            food.getFoodType(),
                            quantity.getValue(),
                            new java.sql.Date(System.currentTimeMillis())
                    ));
                }
                else if (kitchenStoreItem.getClass() == Beverage.class) {
                    Beverage beverage = (Beverage) kitchenStoreItem;
                    purchaseService.savePurchase(new Purchase(
                            roomNumberField.getValue(),
                            beverage.getPrice(),
                            beverage.getBrand(),
                            quantity.getValue(),
                            new java.sql.Date(System.currentTimeMillis())
                    ));
                }
                Notification  notification = Notification.show("Purchase successful. You will be notified regarding payment at the end of the month :)");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                buyDialog.close();
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(roomNumberField, quantity, finishButton);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return dialogLayout;
    }

    @Tag("vaadin-line")
    public class HorizontalGreyLine extends Div {
        public HorizontalGreyLine() {
            getStyle().set("width","100%").set("border-top","3px solid grey");
        }
    }
}
