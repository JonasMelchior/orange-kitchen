package com.application.views.pdf;

import com.application.service.pdf.PdfGenerator;
import com.application.views.content.EmbeddedPdfDocument;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

@Route(value = "/purchases-pdf")
public class PurchasesPdfView extends Div {
    public PurchasesPdfView() {

        PdfGenerator pdfGenerator = (PdfGenerator) VaadinSession.getCurrent().getSession().getAttribute("pdf_generator");

        add(new EmbeddedPdfDocument(new StreamResource("kitchen_expenses.pdf", pdfGenerator::toBytearrayInputStream)));
        setHeight("100%");
    }
}
