package com.application.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class PdfGenerator {
    private Document document = new Document(PageSize.A4, 50, 50, 50, 50);
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);



    public ByteArrayInputStream toBytearrayInputStream(){
        byte[] byteArrayInputStream = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(byteArrayInputStream);
    }
}
