package com.application.views.admin;

import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.IPurchaseService;
import com.application.service.store.PurchaseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "admin")
public class Admin extends VerticalLayout implements BeforeEnterObserver {

    Dialog adminPasswordDialog;
    VerticalLayout mainLayout = new VerticalLayout();

    Grid<Purchase> purchaseGrid = new Grid<>(Purchase.class, false);
    List<Purchase> purchases;

    Tabs tabs;
    private final Tab january;
    private final Tab february;
    private final Tab march;
    private final Tab april;
    private final Tab may;
    private final Tab june;
    private final Tab july;
    private final Tab august;
    private final Tab september;
    private final Tab october;
    private final Tab november;
    private final Tab december;

    IPurchaseService purchaseService;


    public Admin(@Autowired PurchaseService purchaseService) {
        january = new Tab("January");
        february = new Tab("February");
        march = new Tab("March");
        april = new Tab("April");
        may = new Tab("May");
        june = new Tab("June");
        july = new Tab("July");
        august = new Tab("August");
        september = new Tab("September");
        october = new Tab("October");
        november = new Tab("November");
        december = new Tab("December");

        tabs = new Tabs(january, february, march, april, may, june, july, august, september, october, november, december);
        tabs.addSelectedChangeListener(event -> {
           setTabContent(event.getSelectedTab());
        });

        this.purchaseService = purchaseService;
        //purchaseGrid.addColumn(Purchase::getName).setHeader("Name");
        purchaseGrid.addColumn(Purchase::getRoomNumber).setHeader("Room Number");
        //purchaseGrid.addColumn(Purchase::getPhoneNumber).setHeader("Phone Number");
        purchaseGrid.addColumn(Purchase::getBrand).setHeader("Bought Beverage Brand");
        purchaseGrid.addColumn(Purchase::getPurchaseAmount).setHeader("Purchase Amount (DKK)");
        purchaseGrid.addColumn(Purchase::getQuantity).setHeader("Quantity");
        purchaseGrid.addComponentColumn(purchase -> {
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), delete -> createDeletePurchaseDialog(purchase).open());
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return deleteButton;
        }).setHeader("Delete Purchase");

        purchases = this.purchaseService.findAll();

        int currentMonth = java.time.LocalDateTime.now().getMonthValue() - 1;
        setGridContent(currentMonth);
        setSelectedTab(currentMonth);

        mainLayout.add(purchaseGrid);
        mainLayout.setVisible(false);
        mainLayout.setSizeFull();
        add(tabs, mainLayout);
        setSizeFull();
    }

    private void setSelectedTab(int currentMonth) {
        if (currentMonth == 0) {
            tabs.setSelectedTab(january);
        }
        else if (currentMonth == 1) {
            tabs.setSelectedTab(february);
        }
        else if (currentMonth == 2) {
            tabs.setSelectedTab(march);
        }
        else if (currentMonth == 3) {
            tabs.setSelectedTab(april);
        }
        else if (currentMonth == 4) {
            tabs.setSelectedTab(may);
        }
        else if (currentMonth == 5) {
            tabs.setSelectedTab(june);
        }
        else if (currentMonth == 6) {
            tabs.setSelectedTab(july);
        }
        else if (currentMonth == 7) {
            tabs.setSelectedTab(august);
        }
        else if (currentMonth == 8) {
            tabs.setSelectedTab(september);
        }
        else if (currentMonth == 9) {
            tabs.setSelectedTab(october);
        }
        else if (currentMonth == 10) {
            tabs.setSelectedTab(november);
        }
        else if (currentMonth == 11) {
            tabs.setSelectedTab(december);
        }
    }

    private void setTabContent(Tab selectedTab) {

        if (selectedTab.equals(january)) {
            setGridContent(0);
        }
        else if (selectedTab.equals(february)) {
            setGridContent(1);
        }
        else if (selectedTab.equals(march)) {
            setGridContent(2);
        }
        else if (selectedTab.equals(april)) {
            setGridContent(3);
        }
        else if (selectedTab.equals(may)) {
            setGridContent(4);
        }
        else if (selectedTab.equals(june)) {
            setGridContent(5);
        }
        else if (selectedTab.equals(july)) {
            setGridContent(6);
        }
        else if (selectedTab.equals(august)) {
            setGridContent(7);
        }
        else if (selectedTab.equals(september)) {
            setGridContent(8);
        }
        else if (selectedTab.equals(october)) {
            setGridContent(9);
        }
        else if (selectedTab.equals(november)) {
            setGridContent(10);
        }
        else if (selectedTab.equals(december)) {
            setGridContent(11);
        }

    }

    private void setGridContent(int month) {
        List<Purchase> relevantPurchases = new ArrayList<>();

        for (Purchase purchase : purchases) {
            if (purchase.getDate().getMonth() == month) {
                relevantPurchases.add(purchase);
            }
        }

        purchaseGrid.setItems(relevantPurchases);
    }


    private Dialog createDeletePurchaseDialog(Purchase purchase) {
        Dialog deletePurchaseDialog = new Dialog();

        Button deleteButton = new Button("Delete", delete -> {
            purchaseService.deletePurchase(purchase);
            purchases.remove(purchase);
            purchaseGrid.setItems(purchases);
            deletePurchaseDialog.close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancel", cancel -> deletePurchaseDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, cancelButton);
        buttonLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        VerticalLayout dialogLayout = new VerticalLayout(new H2("Are you sure you want to delete this purchase ?"), buttonLayout);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        deletePurchaseDialog.add(dialogLayout);
        return deletePurchaseDialog;
    }

    private VerticalLayout createAdminPasswordDialogLayout(BeforeEnterEvent event) {
        PasswordField passwordField = new PasswordField("Password");

        Button finishButton = new Button("Continue", finish -> {
            if (passwordField.getValue().equals("2357")) {
                adminPasswordDialog.close();
                mainLayout.setVisible(true);
            }
            else {
                Notification.show("Wrong admin password");
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(passwordField, finishButton);
        dialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        return dialogLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        adminPasswordDialog = new Dialog(createAdminPasswordDialogLayout(beforeEnterEvent));
        adminPasswordDialog.open();
        adminPasswordDialog.setCloseOnEsc(false);
        adminPasswordDialog.setCloseOnOutsideClick(false);
        add(adminPasswordDialog);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

}
