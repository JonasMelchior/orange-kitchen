package com.application;

import com.application.entity.kitchen.store.Purchase;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.*;
import java.sql.Date;
import java.util.*;
import java.util.List;

public class Test {

    private static Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    private static OutputStream outputStream;

    static {
        try {
            outputStream = new FileOutputStream(new File("/home/jonas/test.pdf"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Purchase> purchases = new ArrayList<>();

    public Test() throws FileNotFoundException {
    }

    public static void main(String[] args) {
        purchases.add(new Purchase(
                1,
                5.6,
                "Coke",
                2,
                new Date(20)
        ));

        purchases.add(new Purchase(
                1,
                5.6,
                "Coke",
                1,
                new Date(20)
        ));

        purchases.add(new Purchase(
                3,
                7.6,
                "Coke",
                2,
                new Date(20)
        ));

        purchases.add(new Purchase(
                8,
                6,
                "Coke",
                2,
                new Date(20)
        ));

        purchases.add(new Purchase(
                15,
                5.5,
                "Coke",
                6,
                new Date(20)
        ));

        purchases.add(new Purchase(
                20,
                8.4,
                "Vodka",
                1,
                new Date(20)
        ));

        purchases.add(new Purchase(
                35,
                5.6,
                "Coke",
                2,
                new Date(20)
        ));

        for (int i = 0; i < purchases.size(); i++) {
            double purchaseAmount = purchases.get(i).getQuantity() * purchases.get(i).getPurchaseAmount();
            for (int j = 0; j < purchases.size(); j++) {
                if (purchases.get(j).getRoomNumber() == purchases.get(i).getRoomNumber() && i != j) {
                    purchaseAmount += purchases.get(j).getQuantity() * purchases.get(j).getPurchaseAmount();
                    purchases.remove(j--);
                }
            }
            System.out.println("Room " + purchases.get(i).getRoomNumber() + ": " + purchaseAmount + " DKK");
            purchases.remove(i--);
        }


    }

    private static void generatePdf() {
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            generatePdfLayout();

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        document.close();
    }

    private static void generatePdfLayout() throws DocumentException {
        Font grayFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.GRAY);

        Map<String, String> parsedPurchases = parsePurchases();


        for (String room : parsedPurchases.keySet()) {
            Paragraph roomDebtPar = new Paragraph("Room " + room + ": " + parsedPurchases.get(room) + " DKK");
            document.add(roomDebtPar);
            document.add(new LineSeparator(grayFont));
        }
    }

    /*
     * Sum purchases for same room numbers
     * */
    private static Map<String, String> parsePurchases() {
        Map<String, String> parsedPurchases = new HashMap<>();

        for (int i = 0; i < purchases.size(); i++) {
            parsedPurchases.put(String.valueOf(purchases.get(i).getRoomNumber()), String.valueOf(purchases.get(i).getPurchaseAmount()));
            for (int j = 0; j < purchases.size(); j++) {
                if (purchases.get(j).getRoomNumber() == purchases.get(i).getRoomNumber() && i != j) {
                    int quantity1 = purchases.get(j).getQuantity();
                    double currentPurchaseAmount = Double.parseDouble(parsedPurchases.get(String.valueOf(purchases.get(i).getRoomNumber())));
                    System.out.println("Current Purchase Amounr: " + currentPurchaseAmount);
                    System.out.println("Purchase Amount: " + purchases.get(j).getPurchaseAmount());
                    double purchaseAmount = currentPurchaseAmount + purchases.get(j).getPurchaseAmount();
                    System.out.println(purchaseAmount);
                    parsedPurchases.put(String.valueOf(purchases.get(j).getRoomNumber()), parsedPurchases.get(String.valueOf(purchases.get(j).getRoomNumber())) + purchaseAmount);
                }
            }
            purchases.remove(i--);
        }
        return parsedPurchases;
    }
}
