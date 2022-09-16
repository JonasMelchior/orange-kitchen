package com.application.service.pdf;

import com.application.entity.kitchen.store.DailyRevenue;
import com.application.entity.kitchen.store.Purchase;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

public class PdfGenerator {
    private Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);

    private List<Purchase> purchases;

    public PdfGenerator(List<Purchase> purchases) {
        this.purchases = purchases;
        this.purchases.sort(Comparator.comparingInt(Purchase::getRoomNumber));

        generatePdf();
    }

    private void generatePdf() {
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            generatePdfLayout();

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        document.close();
    }

    private void generatePdfLayout() throws DocumentException {
        Font grayFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.GRAY);

        for (int i = 0; i < purchases.size(); i++) {
            double purchaseAmount = purchases.get(i).getQuantity() * purchases.get(i).getPurchaseAmount();
            for (int j = 0; j < purchases.size(); j++) {
                if (purchases.get(j).getRoomNumber() == purchases.get(i).getRoomNumber() && i != j) {
                    purchaseAmount += purchases.get(j).getQuantity() * purchases.get(j).getPurchaseAmount();
                    purchases.remove(j--);
                }
            }
            Paragraph roomDebtPar = new Paragraph("Room " + purchases.get(i).getRoomNumber() + ": " + purchaseAmount + " DKK");
            document.add(roomDebtPar);
            document.add(new LineSeparator(grayFont));
            purchases.remove(i--);
        }

    }

    public ByteArrayInputStream toBytearrayInputStream(){
        byte[] byteArrayInputStream = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(byteArrayInputStream);
    }
}
