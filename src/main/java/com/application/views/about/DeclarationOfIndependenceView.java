package com.application.views.about;

import com.application.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Declaration of Independence")
@Route(value = "declaration-of-independence", layout = MainLayout.class)
public class DeclarationOfIndependenceView extends VerticalLayout {

    public DeclarationOfIndependenceView() {
        setSpacing(false);

        Image img = new Image("images/declaration_of_independence.png", "placeholder plant");
        //img.setSizeFull();
        add(img);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
