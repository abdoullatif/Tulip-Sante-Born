package com.example.tulipsante.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
//import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.tulipsante.R;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Patient;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Context.WINDOW_SERVICE;

public class PdfGenerator {

    byte[] buf = new byte[1024];

    public void encrypt(InputStream in, OutputStream out) {
        try {
            // Bytes written to out will be encrypted
//            out = new CipherOutputStream(out, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {
        }
    }

    public static void createMyPDF(View view, Activity activity){

//        PdfDocument myPdfDocument = new PdfDocument();
//        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(595, 842,1).create();
//        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
//
//        Paint myPaint = new Paint();
//        String myString = "Tulip Sante";
//        int x = 200, y=25;
//
//        myPage.getCanvas().drawColor(activity.getColor(R.color.colorWhite));
////        myPage.getCanvas().draw
//
//        for (String line:myString.split("\n")){
//            myPage.getCanvas().drawText(line, x, y, myPaint);
//            y+=myPaint.descent()-myPaint.ascent();
//        }
//
//        myPdfDocument.finishPage(myPage);
//
//        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf";
//        File myFile = new File(myFilePath);
//        try {
//            myPdfDocument.writeTo(new FileOutputStream(myFile));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//
//        myPdfDocument.close();
    }

    public static void pdf() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createPdf(
            Consultation consultation,
            Patient patient,
            Context context,
            Activity activity)
            throws FileNotFoundException {
//        String pdfPath = Environment
//                .getExternalStoragePublicDirectory(
//                        "Tulip_sante"
//                                +File.separator
//                                +"Patients"
//                                +File.separator
//                                +patient.getIdPatient()
//                                +File.separator
//                                +"Personal"
//                                +File.separator
//                ).toString();
        String pdfPath = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(pdfPath, "document_"+ consultation.getIdConsultation() + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(5,5,5,5);

        Drawable d = ContextCompat.getDrawable(context,R.drawable.cons_report);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        float[] width = {250f, 100f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // **
        // Patient header details

        table.addCell(new Cell().add(new Paragraph("Patient Id")));
        table.addCell(new Cell().add(new Paragraph(patient.getIdPatient()).setBold()));

        table.addCell(new Cell().add(new Paragraph("Full Name")));
        table.addCell(new Cell().add(new Paragraph(patient.getPrenomPatient() + " " + patient.getNomPatient()).setBold()));

        table.addCell(new Cell().add(new Paragraph("Date of Birth")));
        table.addCell(new Cell().add(new Paragraph(patient.getDateNaissancePatient()).setBold()));

        table.addCell(new Cell().add(new Paragraph("Gender")));
        table.addCell(new Cell().add(new Paragraph(patient.getGenrePatient()).setBold()));

        table.addCell(new Cell().add(new Paragraph("Consultation Date")));
        table.addCell(new Cell().add(new Paragraph(consultation.getDateConsultation()).setBold()));

        table.addCell(new Cell().add(new Paragraph("Report Date")));
        table.addCell(new Cell().add(new Paragraph("")));

        // End patient header details
        // **

        Paragraph title1 = new Paragraph("Vital Signs");
        Paragraph title2 = new Paragraph("Investigation");
        Paragraph title3 = new Paragraph("Diagnostic");
        Paragraph title4 = new Paragraph("Prescription");
        Paragraph title5 = new Paragraph("Advice");
        Paragraph title6 = new Paragraph("Vaccination");



        document.add(image);
        document.add(table);

        document.close();
        Toast.makeText(context, "Pdf Created!", Toast.LENGTH_SHORT).show();
    }

    public static void createPatientCard(
            String dataName,
            String dataDate,
            String dataPhone,
            String dataGender,
            String patientId)  throws FileNotFoundException {
        String pdfPath = Environment
                .getExternalStoragePublicDirectory(
                        "Tulip_sante"
                                +File.separator
                                +"Patients"
                                +File.separator
                                +patientId
                                +File.separator
                                +"Personal"
                                +File.separator
                ).toString();
        File file = new File(pdfPath, patientId + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // fill the document here
        pdfDocument.setDefaultPageSize(PageSize.A6);

        Paragraph name = new Paragraph("Name: " + dataName);
        Paragraph birthDate = new Paragraph("BirthDate: " + dataDate);
        Paragraph phone = new Paragraph("Phone: " + dataPhone);
        Paragraph gender = new Paragraph("Gender: " + dataGender);

        BarcodeQRCode qrCode = new BarcodeQRCode(patientId);
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
        Image qrCodeImage = new Image(qrCodeObject)
                .setWidth(120).setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(name);
        document.add(birthDate);
        document.add(phone);
        document.add(gender);
        document.add(qrCodeImage);
        document.close();
    }
}
