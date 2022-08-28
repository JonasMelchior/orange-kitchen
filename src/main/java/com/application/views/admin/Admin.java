package com.application.views.admin;

import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.IPurchaseService;
import com.application.service.store.PurchaseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "admin")
public class Admin extends VerticalLayout implements BeforeEnterObserver {

    Dialog adminPasswordDialog;
    VerticalLayout mainLayout = new VerticalLayout();

    IPurchaseService purchaseService;

    public Admin(@Autowired PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        Grid<Purchase> purchaseGrid = new Grid<>(Purchase.class, false);
        purchaseGrid.addColumn(Purchase::getName).setHeader("Name");
        purchaseGrid.addColumn(Purchase::getRoomNumber).setHeader("Room Number");
        purchaseGrid.addColumn(Purchase::getPhoneNumber).setHeader("Phone Number");
        purchaseGrid.addColumn(Purchase::getBrand).setHeader("Bought Beverage Brand");
        purchaseGrid.addColumn(Purchase::getPurchaseAmount).setHeader("Purchase Amount (DKK)");
        purchaseGrid.addColumn(Purchase::getQuantity).setHeader("Quantity");


        List<Purchase> purchases = this.purchaseService.findAll();

        purchaseGrid.setItems(purchases);

        mainLayout.add(purchaseGrid);
        mainLayout.setVisible(false);
        add(mainLayout);
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

}
