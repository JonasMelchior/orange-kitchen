package com.application.views;


import com.application.components.appnav.AppNav;
import com.application.components.appnav.AppNavItem;
import com.application.views.about.DeclarationOfIndependenceView;
import com.application.views.foodclub.ArchivedRecipeView;
import com.application.views.foodclub.FoodClubView;
import com.application.views.kitchenstore.KitchenStoreView;
import com.application.views.statistics.KitchenStatisticsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        Image image = new Image("icons/orange_kitchen.png", "Orange kitchen logo placeholder");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(image,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        nav.addClassNames("app-nav");

        nav.addItem(new AppNavItem("Home", FoodClubView.class, new Icon(VaadinIcon.HOME)));
        nav.addItem(new AppNavItem("Archived Recipes", ArchivedRecipeView.class, new Icon(VaadinIcon.ARCHIVES)));
        nav.addItem(new AppNavItem("Declaration of Independence", DeclarationOfIndependenceView.class, new Icon(VaadinIcon.FILE_TEXT_O)));
        nav.addItem(new AppNavItem("Kitchen Store", KitchenStoreView.class, new Icon(VaadinIcon.SHOP)));
        nav.addItem(new AppNavItem("Kitchen Statistics", KitchenStatisticsView.class, new Icon(VaadinIcon.LINE_BAR_CHART)));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
