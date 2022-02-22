package com.example.tulipsante.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralPurposeFunctions {

    public static String idTable() {
        String id = ""+Math.random();
        try {
            MessageDigest AlphaNumeric= MessageDigest.getInstance("SHA-256");
            byte[] result = AlphaNumeric.digest(id.getBytes());
            StringBuffer value = new StringBuffer();
            for (byte b : result) {
                value.append(Integer.toString((b & 0xff) + 0x100, 16));
            }
            return "TAB-TLP-"+value.toString().toUpperCase(Locale.ITALY).substring(0,8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if(src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (byte b : src) {
            buffer[0] = Character.forDigit((b >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(b & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age;
        String ageString;
        if(today.get(Calendar.YEAR) == dob.get(Calendar.YEAR)) {
            age = today.get(Calendar.MONTH) - month;
            ageString = age + " M";
            if(age == 0) {
                age = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
                ageString = age + " D";
            }
        }
        else {
            age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            ageString = age + " Y";
        }

        return ageString;
    }

    public static boolean hasVaccineExpired(String dateExpiration){
        boolean hasExpired = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date srtDate = sdf.parse(dateExpiration);
            if (new Date().after(srtDate) ) {
                hasExpired = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hasExpired;
    }

    public static boolean hasExpired(String dateExpiration){
        boolean hasExpired = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date srtDate = sdf.parse(dateExpiration);
            if (new Date().after(srtDate) ) {
                hasExpired = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hasExpired;
    }

    public static Date getStringDate(String date) {
        Date dateD = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            dateD = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateD;
    }

    public static void moveFile(String inputPath, String outputPath) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;
            new File(inputPath).delete();

            System.out.println("moved successfully");
        }
        catch (Exception fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }


    /***
     * @Auteur
     * @DABO
     ***/
    public static Boolean decrypePassword( String passwordUser, String oldPassword) {

        // Etat de la valeur
        Boolean etat = false;
        try
        {
            // Définition de l'instance de l'algorithme utilisé
            MessageDigest instanceAlgoritheme = MessageDigest.getInstance("SHA");

            // Récupération du contenu hacher par l'algorithme
            instanceAlgoritheme.update(passwordUser.getBytes());

            // Transformation en bit
            byte byteData[] = instanceAlgoritheme.digest();

            //convertir le tableau de bits provenant du Buffer en format hexadécimal
            StringBuffer newPassword = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                newPassword.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Verification de l'égalité des
            if(newPassword.toString().equals ( oldPassword ))
            {
                etat = true;
            }
        }
        catch (NoSuchAlgorithmException e  )
        {
            System.out.println ("Erreur dans l'instance de l'algorithme utilisé veuillez verifié "+e.getLocalizedMessage ());
        }
        // Le Chache à vérifier
        return etat;
    }

    // close keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if(view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
