package com.example.tulipsante.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.tulipsante.views.activities.CarouselActivity;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;


class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "tulip_sante_database";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase sqlite_connection;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        // copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        dbFile.getName();
        return dbFile.exists();
    }

    String db_name() {
        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.getName();
    }

    public SQLiteDatabase getDatabase() {

       return this.sqlite_connection;
    }


    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        sqlite_connection = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        if(!sqlite_connection.isOpen()){
            System.out.println("trying shit");
        }else {

        }

        return sqlite_connection != null;
    }

    @Override
    public synchronized void close() {
        if (this.sqlite_connection != null)
            this.sqlite_connection.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}

public class synchro {
    String classs = "com.mysql.jdbc.Driver";
    Context context;
    String onlineIpAddress;
    String onlineDatabase;
    String onlineDatabaseUser;
    String onlineDatabasePassword;
    String ftp_user;
    String ftp_pass;
    Connection mysql_connection;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    Date date = new Date();
    String now = DateFor.format(date);
    String datePermission = sdf.format(date);

    private static SQLiteDatabase sqlite_connection1;


    Activity activity;

    public synchro(
            Activity activity,
            Context context,
            String onlineDatabase,
            String onlineDatabaseUser,
            String onlineDatabasePassword ,
            String onlineIpAddress,
            String ftp_user,
            String ftp_mdp) throws IOException, SQLException {

        this.activity = activity;
        this.context = context;
        this.onlineIpAddress = onlineIpAddress;
        this.onlineDatabase = onlineDatabase;
        this.onlineDatabaseUser = onlineDatabaseUser;
        this.onlineDatabasePassword = onlineDatabasePassword;
        this.ftp_user = ftp_user;
        this.ftp_pass = ftp_mdp;

        setMysql_connection();
        DatabaseHelper db  =    new DatabaseHelper(context);
        if(db.openDataBase()){
            sqlite_connection1 =   db.getDatabase();
            System.out.println(" sqlite data open ");
        } else {
            System.out.println(" sqlite not open");
        }
    }


    @SuppressLint("NewApi")
    public void setMysql_connection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);

            mysql_connection = DriverManager.getConnection(
                    "jdbc:mysql://"+this.onlineIpAddress+"/"+this.onlineDatabase, this.onlineDatabaseUser, this.onlineDatabasePassword);


          //  conn = DriverManager.getConnection(ConnURL);

            activity.runOnUiThread(() -> {
                System.out.println(" mysql connected");
            });
        } catch (SQLException ex) {
            activity.runOnUiThread(() -> {
                System.out.println("error to connect to Server "+ex.getMessage());
                System.out.println(" "+ex.getLocalizedMessage());
            });
        } catch (ClassNotFoundException e) {
            activity.runOnUiThread(() -> {
                System.out.println(" "+e.getLocalizedMessage());
            });
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(" "+e.getLocalizedMessage());
            });
        }

    }

    public void synchronizeMedecin() {
        pays();
        region();
        commune();
        addresse();
        hopital();
        departement();
        specialite();
        medecin();
        utilisateur();
        historiquePresence();
        contact();
        contactUrgence();
        // close connection
        closeConn();
    }

    public void synchronize() {
        // Server to local
        addresse();
        allergie();
        allergiepatient();
        antecedentPatient();
        categorieSigneViteaux();
        commune();
        conseil();
        consultation();
        contact();
        contactUrgence();
        departement();
        diagnostic();
        examen();
        hopital();
        maladie();
        medecin();
        patient();
        pays();
        permission();
        prescription();
        region();
        historiquePresence();
        signesVitaux();
        specialite();
        symptome();
        symptomePatient();
        typeExamens();
        utilisateur();
        vice();
        vicePatient();
        typeVaccination();
        vaccinationPatient();
        conferenceContact();

        // Local to server
        local_addresse();
        local_allergiePatient();
        local_antecedentPatient();
        local_conferencecontact();
        local_patient();
        local_conseil();
        local_consultation();
        local_contact();
        local_contacturgence();
        local_diagnostic();
        local_examen();
        local_historiquepresence();
        local_permission();
        local_prescription();
        local_signesvitaux();
        local_symptomespatient();
        local_vaccinationpatient();
        local_vicepatient();
        local_relation();
//       close connection
        closeConn();
    }

    void closeConn() {
        try {
            sqlite_connection1.close();
            mysql_connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean hasPermission(String idMedecin, String idPatient) {
        /*
        * if given doc has permission with the patient, then
        * download the patient folder.
        * */
//        int[] data;
        boolean result = false;
        String sql = "SELECT * FROM patient inner join permission on permission.idPatient = patient.idPatient WHERE permission.idMedecin = "  +"'" + idMedecin  +"'"+  " and permission.idPatient = " +"'"+ idPatient +"'"+ " and permission.dateExpiration > " +"'"+ datePermission +"'"+ " and permission.type = 'public' ORDER BY patient.dateRegistration ";
        Cursor res = sqlite_connection1.rawQuery(sql, null);
        res.moveToFirst();
        try{
            result = res.getCount() != 0;
        } catch (Exception e) {
            System.out.println(" sql"+e.getMessage());
        }
        System.out.println("permission checking!");
        return result;
    }

    @SuppressLint("NewApi")
    private  void addresse(){
        String sql = "SELECT *  FROM addresse order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{

                            if(check_if_existe("addresse",rs.getString("idAddresse"),"idAddresse")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("addresse",rs.getString("idAddresse"),"idAddresse"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("premiereAddresse",rs.getString("premiereAddresse")); //These Fields should be your String values of actual column names
                                    cv.put("idCommune",rs.getString("idCommune"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("addresse", cv, "idAddresse = ?", new String[]{rs.getString("idAddresse")});
                                    n++;
                                }

                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idAddresse", rs.getString("idAddresse"));
                                insertValues.put("premiereAddresse", rs.getString("premiereAddresse"));
                                insertValues.put("idCommune", rs.getString("idCommune"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("addresse", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql"+e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN+" has been add to 'addresse' table");
                    });
                   // sqlite_connection1.close();
                }catch(Exception e){
                    activity.runOnUiThread(() -> {
                        System.out.println("ggg "+e.getLocalizedMessage());
                    });
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }



    @SuppressLint("NewApi")
    private  void allergie(){
        String sql = "SELECT *  FROM allergie order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("allergie",rs.getString("idAllergie"),"idAllergie")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("allergie",rs.getString("idAllergie"),"idAllergie"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("type",rs.getString("type")); //These Fields should be your String values of actual column names
                                    cv.put("description",rs.getString("description"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("allergie", cv, "idAllergie = ?", new String[]{rs.getString("idAllergie")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idAllergie", rs.getString("idAllergie"));
                                insertValues.put("type", rs.getString("type"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("allergie", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> System.out.println(" sql"+e.getMessage()));
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> System.out.println(finalN +" has been add to 'allergie' table"));
                }catch(Exception e){
                    activity.runOnUiThread(() -> System.out.println("ggg "+e.getLocalizedMessage()));
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> System.out.println(e.getMessage()));
        }

    }



    @SuppressLint("NewApi")
    private  void allergiepatient(){
        String sql = "SELECT *  FROM allergiePatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{

                            if(check_if_existe("allergiePatient",rs.getString("idAllergiePatient"),"idAllergiePatient")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("allergiePatient",rs.getString("idAllergiePatient"),"idAllergiePatient"))>0){

                                    ContentValues cv = new ContentValues();
                                    cv.put("idAllergie",rs.getString("idAllergie")); //These Fields should be your String values of actual column names
                                    cv.put("idPatient",rs.getString("idPatient"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("allergiePatient", cv, "idAllergiePatient = ?", new String[]{rs.getString("idAllergiePatient")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idAllergiePatient", rs.getString("idAllergiePatient"));
                                insertValues.put("idAllergie", rs.getString("idAllergie"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("allergiePatient", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql"+e.getMessage());
                        }
                    }
                    System.out.println(n+" has been add to 'allergiepatient' table");
                }catch(Exception e){
                    System.out.println("ggg "+e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @SuppressLint("NewApi")
    private  void antecedentPatient(){
        String sql = "SELECT *  FROM antecedentPatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("antecedentPatient",rs.getString("idAntecedentPatient"),"idAntecedentPatient")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("antecedentPatient",rs.getString("idAntecedentPatient"),"idAntecedentPatient"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMaladie",rs.getString("idMaladie")); //These Fields should be your String values of actual column names
                                    cv.put("idPatient",rs.getString("idPatient"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("antecedentPatient", cv, "idAntecedentPatient = ?", new String[]{rs.getString("idAntecedentPatient")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idAntecedentPatient", rs.getString("idAntecedentPatient"));
                                insertValues.put("idMaladie", rs.getString("idMaladie"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("antecedentPatient", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql"+e.getMessage());
                        }
                    }
                    System.out.println(n+" has been add to 'antecedentPatient' table");
                }catch(Exception e){
                    System.out.println("ggg "+e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



    @SuppressLint("NewApi")
    private  void categorieSigneViteaux(){
        String sql = "SELECT *  FROM categorieSigneVitaux order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("categorieSigneVitaux",rs.getString("idCatSV"),"idCatSV")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("categorieSigneVitaux",rs.getString("idCatSV"),"idCatSV"))>0){

                                    ContentValues cv = new ContentValues();
                                    cv.put("description",rs.getString("description")); //These Fields should be your String values of actual column names
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("categorieSigneVitaux", cv, "idCatSV = ?", new String[]{rs.getString("idCatSV")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idCatSV", rs.getString("idCatSV"));
                                insertValues.put("description", rs.getString("description"));

                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("categorieSigneVitaux", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql"+e.getMessage());
                        }
                    }
                    System.out.println(n+" has been add to 'categorieSigneViteaux' table");
                }catch(Exception e){
                    System.out.println("ggg" + e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @SuppressLint("NewApi")
    private  void commune(){
        String sql = "SELECT *  FROM commune order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    // String last_sqlite_row =check_new_record("ecole") ; // check_new_record("personne");
                    while (rs.next()) {
                        // System.out.println("Server"+rs.getString("flagtransmis"));
                        // System.out.println("local"+last_sqlite_row);
                        try{
                            if(check_if_existe("commune",rs.getString("idCommune"),"idCommune")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("commune",rs.getString("idCommune"),"idCommune"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("idRegion",rs.getString("idRegion")); //These Fields should be your String values of actual column names
                                    cv.put("nomCommune",rs.getString("nomCommune"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("commune", cv, "idCommune = ?", new String[]{rs.getString("idCommune")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idCommune", rs.getString("idCommune"));
                                insertValues.put("idRegion", rs.getString("idRegion"));
                                insertValues.put("nomCommune", rs.getString("nomCommune"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("commune", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql"+e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    System.out.println(finalN +" has been added to 'commune' table");
                }catch(Exception e){
                    activity.runOnUiThread(() -> System.out.println("ggg "+e.getLocalizedMessage()));
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> System.out.println(e.getMessage()));
        }

    }

    @SuppressLint("NewApi")
    private  void conseil(){

        String sql = "SELECT *  FROM conseil order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("conseil",rs.getString("idConseil"),"idConseil")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("conseil",rs.getString("idConseil"),"idConseil"))>0){

                                    ContentValues cv = new ContentValues();
                                    cv.put("idConsultation",rs.getString("idConsultation")); //These Fields should be your String values of actual column names
                                    cv.put("description",rs.getString("description"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("conseil", cv, "idConseil = ?", new String[]{rs.getString("idConseil")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idConseil", rs.getString("idConseil"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("conseil", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql"+e.getMessage());
                        }
                    }
                    System.out.println(n+" has been add to 'conseil' table");
                }catch(Exception e){
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @SuppressLint("NewApi")
    private  void consultation(){
        String sql = "SELECT *  FROM consultation order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("consultation",rs.getString("idConsultation"),"idConsultation")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("consultation",rs.getString("idConsultation"),"idConsultation"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("idPatient",rs.getString("idPatient")); //These Fields should be your String values of actual column names
                                    cv.put("idMedecin",rs.getString("idMedecin"));
                                    cv.put("dateConsultation",rs.getString("dateConsultation")); //These Fields should be your String values of actual column names
                                    cv.put("description",rs.getString("description"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("consultation", cv, "idConsultation = ?", new String[]{rs.getString("idConsultation")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));

                                insertValues.put("dateConsultation", rs.getString("dateConsultation"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("consultation", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql"+e.getMessage());
                        }
                    }
                    System.out.println(n+" has been add to 'consultation' table");
                }catch(Exception e){
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @SuppressLint("NewApi")
    private  void contact(){
        String sql = "SELECT *  FROM contact order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("contact",rs.getString("idContact"),"idContact")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("contact",rs.getString("idContact"),"idContact"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("idPersonne",rs.getString("idPersonne")); //These Fields should be your String values of actual column names
                                    cv.put("telephoneContact",rs.getString("telephoneContact"));
                                    cv.put("telephoneUrgence",rs.getString("telephoneUrgence")); //These Fields should be your String values of actual column names
                                    cv.put("email",rs.getString("email"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("contact", cv, "idContact = ?", new String[]{rs.getString("idContact")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idContact", rs.getString("idContact"));
                                insertValues.put("idPersonne", rs.getString("idPersonne"));
                                insertValues.put("telephoneContact", rs.getString("telephoneContact"));

                                insertValues.put("telephoneUrgence", rs.getString("telephoneUrgence"));
                                insertValues.put("email", rs.getString("email"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("contact", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql"+e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN +" has been add to 'contact' table");
                    });
                }catch(Exception e){
                    activity.runOnUiThread(() -> {
                        System.out.println(e.getLocalizedMessage());
                    });
                }
            }
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }



    @SuppressLint("NewApi")
    private  void contactUrgence(){
        String sql = "SELECT *  FROM contactUrgence order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs    = stmt.executeQuery(sql);
                try  {
                    n = 0;
                    while (rs.next()) {
                        try{
                            if(check_if_existe("contactUrgence",rs.getString("idEmergencyContact"),"idEmergencyContact")>0){
                                if(rs.getString("flagTransmis").compareTo(check_new_record("contactUrgence",rs.getString("idEmergencyContact"),"idEmergencyContact"))>0){
                                    ContentValues cv = new ContentValues();
                                    cv.put("idPersonne",rs.getString("idPersonne")); //These Fields should be your String values of actual column names
                                    cv.put("relation",rs.getString("relation"));
                                    cv.put("nomRelation",rs.getString("nomRelation")); //These Fields should be your String values of actual column names
                                    cv.put("telephoneRelation",rs.getString("telephoneRelation"));
                                    cv.put("flagTransmis",rs.getString("flagTransmis"));
                                    sqlite_connection1.update("contactUrgence", cv, "idEmergencyContact = ?", new String[]{rs.getString("idEmergencyContact")});
                                    n++;
                                }
                            } else{
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idEmergencyContact", rs.getString("idEmergencyContact"));
                                insertValues.put("idPersonne", rs.getString("idPersonne"));
                                insertValues.put("relation", rs.getString("relation"));

                                insertValues.put("nomRelation", rs.getString("nomRelation"));
                                insertValues.put("telephoneRelation", rs.getString("telephoneRelation"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("contactUrgence", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql"+e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN +" has been added to 'contactUrgence' table");
                    });
                }catch(Exception e){
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }


    @SuppressLint("NewApi")
    private  void departement() {
        String sql = "SELECT *  FROM departement order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("departement", rs.getString("idDepartement"), "idDepartement") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("departement", rs.getString("idDepartement"), "idDepartement")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("description", rs.getString("description")); //These Fields should be your String values of actual column names
                                    cv.put("idHopital", rs.getString("idHopital"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("departement", cv, "idDepartement = ?", new String[]{rs.getString("idDepartement")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idDepartement", rs.getString("idDepartement"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("idHopital", rs.getString("idHopital"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("departement", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'departement' table");
                    });
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }


    @SuppressLint("NewApi")
    private  void diagnostic() {
        String sql = "SELECT *  FROM diagnostic order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("diagnostic", rs.getString("idDiagnostic"), "idDiagnostic") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("diagnostic", rs.getString("idDiagnostic"), "idDiagnostic")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMaladie", rs.getString("idMaladie")); //These Fields should be your String values of actual column names
                                    cv.put("idConsultation", rs.getString("idConsultation"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("diagnostic", cv, "idDiagnostic = ?", new String[]{rs.getString("idDiagnostic")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idDiagnostic", rs.getString("idDiagnostic"));
                                insertValues.put("idMaladie", rs.getString("idMaladie"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("diagnostic", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'diagnostic' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void examen() {
        String sql = "SELECT *  FROM examen order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("examen", rs.getString("idExamen"), "idExamen") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("examen", rs.getString("idExamen"), "idExamen")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idConsultation", rs.getString("idConsultation")); //These Fields should be your String values of actual column names
                                    cv.put("idTypeExamens", rs.getString("idTypeExamens"));
                                    cv.put("valeur", rs.getString("valeur"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("examen", cv, "idExamen = ?", new String[]{rs.getString("idExamen")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idExamen", rs.getString("idExamen"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("idTypeExamens", rs.getString("idTypeExamens"));
                                insertValues.put("valeur", rs.getString("valeur"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("examen", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'examen' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void historiqueMedecein() {
        String sql = "SELECT *  FROM historiqueMedecein order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("historiqueMedecein", rs.getString("idHistoriqueMedecin"), "idHistoriqueMedecin") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("historiqueMedecein", rs.getString("idHistoriqueMedecin"), "idHistoriqueMedecin")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMedecin", rs.getString("idMedecin")); //These Fields should be your String values of actual column names
                                    cv.put("dateHistoriqueMedecin", rs.getString("dateHistoriqueMedecin"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("historiqueMedecein", cv, "idHistoriqueMedecin = ?", new String[]{rs.getString("idHistoriqueMedecin")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idHistoriqueMedecin", rs.getString("idHistoriqueMedecin"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("dateHistoriqueMedecin", rs.getString("dateHistoriqueMedecin"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("historiqueMedecein", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'historiqueMedecein' table");
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void historiquePatient() {
        String sql = "SELECT *  FROM historiquePatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("historiquePatient", rs.getString("idHistoriquePatient"), "idHistoriquePatient") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("historiquePatient", rs.getString("idHistoriquePatient"), "idHistoriquePatient")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idPatient", rs.getString("idPatient")); //These Fields should be your String values of actual column names
                                    cv.put("idMedecin", rs.getString("idMedecin"));
                                    cv.put("dateEnregistrement", rs.getString("dateEnregistrement"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("historiquePatient", cv, "idHistoriquePatient = ?", new String[]{rs.getString("idHistoriquePatient")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idHistoriquePatient", rs.getString("idHistoriquePatient"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("dateEnregistrement", rs.getString("dateEnregistrement"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("historiquePatient", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'historiquePatient' table");
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void historiquePresence() {
        String sql = "SELECT *  FROM historiquePresence order by flagTransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("historiquePresence", rs.getString("idPresence"), "idPresence") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("historiquePresence", rs.getString("idPresence"), "idPresence")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMedecin", rs.getString("idMedecin")); //These Fields should be your String values of actual column names
                                    cv.put("dateHistorique", rs.getString("dateHistorique"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("historiquePresence", cv, "idPresence = ?", new String[]{rs.getString("idPresence")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idPresence", rs.getString("idPresence"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("dateHistorique", rs.getString("dateHistorique"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("historiquePresence", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'historiquePresence' table");
                    });
                } catch (Exception e) {
                    activity.runOnUiThread(() -> {
                    });
                }
            }
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }




    @SuppressLint("NewApi")
    private  void hopital() {
        String sql = "SELECT *  FROM hopital order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("hopital", rs.getString("idHopital"), "idHopital") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("hopital", rs.getString("idHopital"), "idHopital")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idAddresse", rs.getString("idAddresse")); //These Fields should be your String values of actual column names
                                    cv.put("nomHopital", rs.getString("nomHopital"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("hopital", cv, "idHopital = ?", new String[]{rs.getString("idHopital")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idHopital", rs.getString("idHopital"));
                                insertValues.put("idAddresse", rs.getString("idAddresse"));
                                insertValues.put("nomHopital", rs.getString("nomHopital"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("hopital", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(" sql" + e.getMessage());
                                }
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(finalN + " has been added to 'hopital' table");
                        }
                    });
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(e.getMessage());
                }
            });
        }
    }


    @SuppressLint("NewApi")
    private  void maladie() {
        String sql = "SELECT *  FROM maladie order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("maladie", rs.getString("idMaladie"), "idMaladie") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("maladie", rs.getString("idMaladie"), "idMaladie")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("description", rs.getString("description")); //These Fields should be your String values of actual column names
                                    cv.put("type", rs.getString("type"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("maladie", cv, "idMaladie = ?", new String[]{rs.getString("idMaladie")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idMaladie", rs.getString("idMaladie"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("type", rs.getString("type"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("maladie", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'maladie' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private  void medecin() {
        String sql = "SELECT *  FROM medecin order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("medecin", rs.getString("idMedecin"), "idMedecin") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("medecin", rs.getString("idMedecin"), "idMedecin")) > 0) {
                                      if(download(rs.getString("photo"))) {
                                          System.out.println(rs.getString("photo"));
                                          ContentValues cv = new ContentValues();
                                          cv.put("idSpecialite", rs.getString("idSpecialite"));
                                          cv.put("idDepartement", rs.getString("idDepartement"));
                                          cv.put("idAddresse", rs.getString("idAddresse"));
                                          cv.put("nomMedecin", rs.getString("nomMedecin"));
                                          cv.put("prenomMedecin", rs.getString("prenomMedecin"));
                                          cv.put("genreMedecin", rs.getString("genreMedecin"));
                                          cv.put("dateDeNaissance", rs.getString("dateDeNaissance"));
                                          cv.put("statusMatrimonialMedecin", rs.getString("statusMatrimonialMedecin"));
                                          cv.put("uidMedecin", rs.getString("uidMedecin"));

                                          cv.put("flagTransmis", rs.getString("flagTransmis"));
                                          cv.put("photo", rs.getString("photo"));
                                          sqlite_connection1.update("medecin", cv, "idMedecin = ?", new String[]{rs.getString("idMedecin")});
                                          n++;
                                      }
                                }
                            } else {
                                if(download(rs.getString("photo"))) {
                                    ContentValues insertValues = new ContentValues();
                                    insertValues.put("idMedecin", rs.getString("idMedecin"));
                                    insertValues.put("idSpecialite", rs.getString("idSpecialite"));
                                    insertValues.put("idDepartement", rs.getString("idDepartement"));
                                    insertValues.put("idAddresse", rs.getString("idAddresse"));
                                    insertValues.put("nomMedecin", rs.getString("nomMedecin"));
                                    insertValues.put("prenomMedecin", rs.getString("prenomMedecin"));

                                    insertValues.put("genreMedecin", rs.getString("genreMedecin"));
                                    insertValues.put("dateDeNaissance", rs.getString("dateDeNaissance"));
                                    insertValues.put("statusMatrimonialMedecin", rs.getString("statusMatrimonialMedecin"));
                                    insertValues.put("uidMedecin", rs.getString("uidMedecin"));

                                    insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                    insertValues.put("photo", rs.getString("photo"));
                                    sqlite_connection1.insert("medecin", null, insertValues);
                                    n++;
                                }
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'medecin' table");
                    });
                } catch (Exception e) {
                    activity.runOnUiThread(() -> {
                    });
                }
            }
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }

    public void create_repo(String idPatient) {
        File createPersonal = new File(
                Environment
                        .getExternalStorageDirectory()
                        +File.separator
                        +"Tulip_sante/Patients/"
                        +idPatient+"/Personal/");
        File createDiag = new File(
                Environment
                        .getExternalStorageDirectory()
                        +File.separator
                        +"Tulip_sante/Patients/"
                        +idPatient+"/Diagnostic/");
        if(!createPersonal.exists()){
            createPersonal.mkdirs();
        }
        if(!createDiag.exists()) {
            createDiag.mkdirs();
        }
    }

    @SuppressLint("NewApi")
    private void patient() {
        String sql = "SELECT *  FROM patient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            // Create repository
                            create_repo(rs.getString("idPatient"));
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            String idMedecin = sharedPreferences.getString("IDMEDECIN",null);
                            // download patient documents if permission
                            if(idMedecin != null) {
                                System.out.println("_____ Id Medecin Present _____");
                                System.out.println(rs.getString("idPatient"));
                                if (hasPermission(idMedecin, rs.getString("idPatient"))) {
                                    try {
                                        downloadPatient(rs.getString("idPatient"));
                                        downloadDiagnostic(rs.getString("idPatient"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else {
                                activity.runOnUiThread(() -> {
                                    Toast toast = Toast.makeText(
                                            activity, "Connect to get patient documents!", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM | Gravity.END,1,1);
                                    toast.show();
                                });
                            }
                            // Update and insert patient
                            if (check_if_existe("patient", rs.getString("idPatient"), "idPatient") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("patient", rs.getString("idPatient"), "idPatient")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idAddresse", rs.getString("idAddresse"));
                                    cv.put("nomPatient", rs.getString("nomPatient"));
                                    cv.put("idAddresse", rs.getString("idAddresse"));
                                    cv.put("nomPatient", rs.getString("nomPatient"));
                                    cv.put("prenomPatient", rs.getString("prenomPatient"));
                                    cv.put("genrePatient", rs.getString("genrePatient"));
                                    cv.put("dateNaissancePatient", rs.getString("dateNaissancePatient"));
                                    cv.put("groupeSanguinPatient", rs.getString("groupeSanguinPatient"));
                                    cv.put("photoPatient", rs.getString("photoPatient"));
                                    cv.put("numeroIdentitePatient", rs.getString("numeroIdentitePatient"));
                                    cv.put("uidPatient", rs.getString("uidPatient"));
                                    cv.put("nationalitePatient", rs.getString("nationalitePatient"));
                                    cv.put("statusMatrimonialPatient", rs.getString("statusMatrimonialPatient"));
                                    cv.put("dateRegistration", rs.getString("dateRegistration"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("patient", cv, "idPatient = ?", new String[]{rs.getString("idPatient")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idAddresse", rs.getString("idAddresse"));
                                insertValues.put("nomPatient", rs.getString("nomPatient"));
                                insertValues.put("prenomPatient", rs.getString("prenomPatient"));
                                insertValues.put("genrePatient", rs.getString("genrePatient"));
                                insertValues.put("dateNaissancePatient", rs.getString("dateNaissancePatient"));

                                insertValues.put("groupeSanguinPatient", rs.getString("groupeSanguinPatient"));
                                insertValues.put("photoPatient", rs.getString("photoPatient"));
                                insertValues.put("numeroIdentitePatient", rs.getString("numeroIdentitePatient"));
                                insertValues.put("uidPatient", rs.getString("uidPatient"));

                                insertValues.put("nationalitePatient", rs.getString("nationalitePatient"));
                                insertValues.put("statusMatrimonialPatient", rs.getString("statusMatrimonialPatient"));
                                insertValues.put("dateRegistration", rs.getString("dateRegistration"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("patient", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'patient' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private  void pays() {
        String sql = "SELECT *  FROM pays order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("pays", rs.getString("idPays"), "idPays") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("pays", rs.getString("idPays"), "idPays")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("nomPays", rs.getString("nomPays"));

                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("pays", cv, "idPays = ?", new String[]{rs.getString("idPays")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idPays", rs.getString("idPays"));
                                insertValues.put("nomPays", rs.getString("nomPays"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("pays", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'pays' table");
                    });
                } catch (Exception e) {
                    activity.runOnUiThread(() -> {
                    });
                }
            }
        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }

    @SuppressLint("NewApi")
    private  void permission() {
        String sql = "SELECT *  FROM permission order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("permission", rs.getString("idPermission"), "idPermission") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("permission", rs.getString("idPermission"), "idPermission")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idPatient", rs.getString("idPatient"));
                                    cv.put("idMedecin", rs.getString("idMedecin"));
                                    cv.put("dateExpiration", rs.getString("dateExpiration"));
                                    cv.put("type", rs.getString("type"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("permission", cv, "idPermission = ?", new String[]{rs.getString("idPermission")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idPermission", rs.getString("idPermission"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("dateExpiration", rs.getString("dateExpiration"));
                                insertValues.put("type", rs.getString("type"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("permission", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'permission' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private  void prescription() {
        String sql = "SELECT *  FROM prescription order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("prescription", rs.getString("idPrescription"), "idPrescription") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("prescription", rs.getString("idPrescription"), "idPrescription")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idConsultation", rs.getString("idConsultation")); //These Fields should be your String values of actual column names
                                    cv.put("datePrescription", rs.getString("datePrescription"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("prescription", cv, "idPrescription = ?", new String[]{rs.getString("idPrescription")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idPrescription", rs.getString("idPrescription"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("datePrescription", rs.getString("datePrescription"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("prescription", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'prescription' table");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private  void referencePatient() {
        String sql = "SELECT *  FROM referencePatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("referencePatient", rs.getString("idReference"), "idReference") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("referencePatient", rs.getString("idReference"), "idReference")) > 0) {

                                    ContentValues cv = new ContentValues();
                                    cv.put("idPatient", rs.getString("idPatient")); //These Fields should be your String values of actual column names
                                    cv.put("idMedecin1", rs.getString("idMedecin1"));
                                    cv.put("idMedecin2", rs.getString("idMedecin2"));
                                    cv.put("raison", rs.getString("raison")); //These Fields should be your String values of actual column names
                                    cv.put("typeVisite", rs.getString("typeVisite"));
                                    cv.put("date", rs.getString("date"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("referencePatient", cv, "idReference = ?", new String[]{rs.getString("idReference")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idReference", rs.getString("idReference"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idMedecin1", rs.getString("idMedecin1"));
                                insertValues.put("idMedecin2", rs.getString("idMedecin2"));
                                insertValues.put("raison", rs.getString("raison"));
                                insertValues.put("typeVisite", rs.getString("typeVisite"));
                                insertValues.put("date", rs.getString("date"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("referencePatient", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'referencePatient' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void region() {
        String sql = "SELECT *  FROM region order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("region", rs.getString("idRegion"), "idRegion") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("region", rs.getString("idRegion"), "idRegion")) > 0) {

                                    ContentValues cv = new ContentValues();
                                    cv.put("idPays", rs.getString("idPays")); //These Fields should be your String values of actual column names
                                    cv.put("nomRegion", rs.getString("nomRegion"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("region", cv, "idRegion = ?", new String[]{rs.getString("idRegion")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idRegion", rs.getString("idRegion"));
                                insertValues.put("idPays", rs.getString("idPays"));
                                insertValues.put("nomRegion", rs.getString("nomRegion"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("region", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> System.out.println(" sql" + e.getMessage()));
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> System.out.println(finalN + " has been added to 'region' table"));
                } catch (Exception e) {
                    activity.runOnUiThread(() -> {
                        System.out.println(e.getLocalizedMessage());
                    });
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }

    @SuppressLint("NewApi")
    private  void relation() {
        String sql = "SELECT *  FROM relation order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("relation", rs.getString("idRelation"), "idRelation") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("relation", rs.getString("idRelation"), "idRelation")) > 0) {

                                    ContentValues cv = new ContentValues();
                                    cv.put("idPatient", rs.getString("idPatient")); //These Fields should be your String values of actual column names
                                    cv.put("idRelationPatient", rs.getString("idRelationPatient"));
                                    cv.put("typeRelation", rs.getString("typeRelation")); //These Fields should be your String values of actual column names
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("relation", cv, "idRelation = ?", new String[]{rs.getString("idRelation")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idRelation", rs.getString("idRelation"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("idRelationPatient", rs.getString("idRelationPatient"));
                                insertValues.put("typeRelation", rs.getString("typeRelation"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("relation", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'relation' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void signesVitaux() {
        String sql = "SELECT *  FROM signesVitaux order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("signesVitaux", rs.getString("idSignesVitaux"), "idSignesVitaux") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("signesVitaux", rs.getString("idSignesVitaux"), "idSignesVitaux")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idCatSV", rs.getString("idCatSV")); //These Fields should be your String values of actual column names
                                    cv.put("idConsultation", rs.getString("idConsultation"));
                                    cv.put("valeur", rs.getString("valeur")); //These Fields should be your String values of actual column names
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("signesVitaux", cv, "idSignesVitaux = ?", new String[]{rs.getString("idSignesVitaux")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idSignesVitaux", rs.getString("idSignesVitaux"));
                                insertValues.put("idCatSV", rs.getString("idCatSV"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("valeur", rs.getString("valeur"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("signesVitaux", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'signesVitaux' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void specialite() {
        String sql = "SELECT *  FROM specialite order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("specialite", rs.getString("idSpecialite"), "idSpecialite") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("specialite", rs.getString("idSpecialite"), "idSpecialite")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("description", rs.getString("description")); //These Fields should be your String values of actual column names
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("specialite", cv, "idSpecialite = ?", new String[]{rs.getString("idSpecialite")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idSpecialite", rs.getString("idSpecialite"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("specialite", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'specialite' table");
                    });
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }



    @SuppressLint("NewApi")
    private  void symptome() {
        String sql = "SELECT *  FROM symptome order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("symptome", rs.getString("idSymptome"), "idSymptome") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("symptome", rs.getString("idSymptome"), "idSymptome")) > 0) {

                                    ContentValues cv = new ContentValues();
                                    cv.put("type", rs.getString("type")); //These Fields should be your String values of actual column names
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("symptome", cv, "idSymptome = ?", new String[]{rs.getString("idSymptome")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idSymptome", rs.getString("idSymptome"));
                                insertValues.put("type", rs.getString("type"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("symptome", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'symptome' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }





    @SuppressLint("NewApi")
    private  void symptomePatient() {
        String sql = "SELECT *  FROM symptomesPatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("symptomesPatient", rs.getString("idSymptomesPatient"), "idSymptomesPatient") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("symptomesPatient", rs.getString("idSymptomesPatient"), "idSymptomesPatient")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idSymptome", rs.getString("idSymptome")); //These Fields should be your String values of actual column names
                                    cv.put("idConsultation", rs.getString("idConsultation"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("symptomesPatient", cv, "idSymptomesPatient = ?", new String[]{rs.getString("idSymptomesPatient")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idSymptomesPatient", rs.getString("idSymptomesPatient"));
                                insertValues.put("idSymptome", rs.getString("idSymptome"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("symptomesPatient", null, insertValues);


                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'symptomesPatient' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void typeExamens() {

        String sql = "SELECT *  FROM typeExamens order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);


                try {


                    n = 0;
                    // String last_sqlite_row =check_new_record("ecole") ; // check_new_record("personne");
                    while (rs.next()) {


                        // System.out.println("Server"+rs.getString("flagtransmis"));
                        // System.out.println("local"+last_sqlite_row);
                        try {

                            if (check_if_existe("typeExamens", rs.getString("idTypeExamens"), "idTypeExamens") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("typeExamens", rs.getString("idTypeExamens"), "idTypeExamens")) > 0) {

                                    ContentValues cv = new ContentValues();
                                    cv.put("typeExamens", rs.getString("typeExamens")); //These Fields should be your String values of actual column names
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("typeExamens", cv, "idTypeExamens = ?", new String[]{rs.getString("idTypeExamens")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idTypeExamens", rs.getString("idTypeExamens"));
                                insertValues.put("typeExamens", rs.getString("typeExamens"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("typeExamens", null, insertValues);


                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'typeExamens' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void utilisateur() {
        String sql = "SELECT *  FROM utilisateur order by flagTransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {
                            if (check_if_existe("utilisateur", rs.getString("idUser"), "idUser") > 0) {
                                if (rs.getString("flagTransmis").compareTo(check_new_record("utilisateur", rs.getString("idUser"), "idUser")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMedecin", rs.getString("idMedecin")); //These Fields should be your String values of actual column names
                                    cv.put("username", rs.getString("username"));
                                    cv.put("password", rs.getString("password"));
                                    cv.put("email", rs.getString("email"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("utilisateur", cv, "idUser = ?", new String[]{rs.getString("idUser")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idUser", rs.getString("idUser"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("username", rs.getString("username"));
                                insertValues.put("password", rs.getString("password"));
                                insertValues.put("email", rs.getString("email"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("utilisateur", null, insertValues);

                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            activity.runOnUiThread(() -> {
                                System.out.println(" sql" + e.getMessage());
                            });
                        }
                    }
                    int finalN = n;
                    activity.runOnUiThread(() -> {
                        System.out.println(finalN + " has been added to 'utilisateur' table");
                    });
                } catch (Exception e) {
                    activity.runOnUiThread(() -> System.out.println(e.getLocalizedMessage()));
                }
            }

        } catch (Exception e) {
            activity.runOnUiThread(() -> {
                System.out.println(e.getMessage());
            });
        }
    }



    @SuppressLint("NewApi")
    private  void vice() {
        String sql = "SELECT *  FROM vice order by flagTransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("vice", rs.getString("idVice"), "idVice") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("vice", rs.getString("idVice"), "idVice")) > 0) {
                                    ContentValues cv = new ContentValues();
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("vice", cv, "idVice = ?", new String[]{rs.getString("idVice")});
                                    n++;
                                }
                            } else {
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idVice", rs.getString("idVice"));
                                insertValues.put("description", rs.getString("description"));

                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("vice", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'vice' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @SuppressLint("NewApi")
    private  void vicePatient() {
        String sql = "SELECT *  FROM vicePatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("vicePatient", rs.getString("idVicePatient"), "idVicePatient") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("vicePatient", rs.getString("idVicePatient"), "idVicePatient")) > 0) {
                                    System.out.println("need update");
                                    ContentValues cv = new ContentValues();
                                    cv.put("idVice", rs.getString("idVice"));
                                    cv.put("idPatient", rs.getString("idPatient"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("vicePatient", cv, "idVicePatient = ?", new String[]{rs.getString("idVicePatient")});
                                    n++;
                                }
                            } else {
                                System.out.println("insert");
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idVicePatient", rs.getString("idVicePatient"));
                                insertValues.put("idVice", rs.getString("idVice"));
                                insertValues.put("idPatient", rs.getString("idPatient"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("vicePatient", null, insertValues);
                                n++;
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'vicePatient' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void typeVaccination() {
        String sql = "SELECT *  FROM typeVaccination order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("typeVaccination", rs.getString("idTypeVaccination"), "idTypeVaccination") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("typeVaccination", rs.getString("idTypeVaccination"), "idTypeVaccination")) > 0) {
                                    System.out.println("need update");
                                    ContentValues cv = new ContentValues();
                                    cv.put("type", rs.getString("type"));
                                    cv.put("duree", rs.getString("duree"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("typeVaccination", cv, "idTypeVaccination = ?", new String[]{rs.getString("idTypeVaccination")});
                                    n++;
                                }
                            } else {
                                System.out.println("insert");
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idTypeVaccination", rs.getString("idTypeVaccination"));
                                insertValues.put("type", rs.getString("type"));
                                insertValues.put("duree", rs.getString("duree"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("typeVaccination", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'typeVaccination' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void vaccinationPatient() {
        String sql = "SELECT *  FROM vaccinationPatient order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    while (rs.next()) {
                        try {

                            if (check_if_existe("vaccinationPatient", rs.getString("idVaccinationPatient"), "idVaccinationPatient") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("vaccinationPatient", rs.getString("idVaccinationPatient"), "idVaccinationPatient")) > 0) {
                                    System.out.println("need update");
                                    ContentValues cv = new ContentValues();
                                    cv.put("idConsultation", rs.getString("idConsultation"));
                                    cv.put("idTypeVaccination", rs.getString("idTypeVaccination"));
                                    cv.put("dateVaccination", rs.getString("dateVaccination"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("vaccinationPatient", cv, "idVaccinationPatient = ?", new String[]{rs.getString("idVaccinationPatient")});
                                    n++;
                                }
                            } else {
                                System.out.println("insert");
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idVaccinationPatient", rs.getString("idVaccinationPatient"));
                                insertValues.put("idConsultation", rs.getString("idConsultation"));
                                insertValues.put("idTypeVaccination", rs.getString("idTypeVaccination"));
                                insertValues.put("dateVaccination", rs.getString("dateVaccination"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("vaccinationPatient", null, insertValues);
                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'vaccinationPatient' table");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @SuppressLint("NewApi")
    private  void conferenceContact() {

        String sql = "SELECT *  FROM conferenceContact order by flagtransmis asc ";
        try {
            int n;
            try (Statement stmt = mysql_connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    n = 0;
                    // String last_sqlite_row =check_new_record("ecole") ; // check_new_record("personne");
                    while (rs.next()) {
                        // System.out.println("Server"+rs.getString("flagtransmis"));
                        // System.out.println("local"+last_sqlite_row);
                        try {

                            if (check_if_existe("conferenceContact", rs.getString("idConferenceContact"), "idConferenceContact") > 0) {
                                //Toast.makeText(context,"existe",Toast.LENGTH_SHORT).show();
                                if (rs.getString("flagTransmis").compareTo(check_new_record("conferenceContact", rs.getString("idConferenceContact"), "idConferenceContact")) > 0) {
                                    System.out.println("need update");
                                    ContentValues cv = new ContentValues();
                                    cv.put("idMedecin", rs.getString("idMedecin"));
                                    cv.put("nomComplet", rs.getString("nomComplet"));
                                    cv.put("description", rs.getString("description"));
                                    cv.put("flagTransmis", rs.getString("flagTransmis"));
                                    sqlite_connection1.update("conferenceContact", cv, "idConferenceContact = ?", new String[]{rs.getString("idConferenceContact")});
                                    n++;
                                }
                            } else {
                                System.out.println("insert");
                                ContentValues insertValues = new ContentValues();
                                insertValues.put("idConferenceContact", rs.getString("idConferenceContact"));
                                insertValues.put("idMedecin", rs.getString("idMedecin"));
                                insertValues.put("nomComplet", rs.getString("nomComplet"));
                                insertValues.put("description", rs.getString("description"));
                                insertValues.put("flagTransmis", rs.getString("flagTransmis"));
                                sqlite_connection1.insert("conferenceContact", null, insertValues);


                                n++;
                                //  this.update_person("personne","true",rs.getString("id"));
                            }
                        } catch (Exception e) {
                            System.out.println(" sql" + e.getMessage());
                        }
                    }
                    System.out.println(n + " has been added to 'conferenceContact' table");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // local
    public int check_if_existe(String t_name,String id,String col_name ){

        int k =0 ;
        ArrayList<String> array_list = new ArrayList<String>();
        String sql = "SELECT * FROM "+t_name+" WHERE "+col_name+"='"+id+"' ";
        try {
        //  Cursor res =  sqlite_connection1.rawQuery("DROP TABLE  medecin ",null);


      Cursor res = sqlite_connection1.rawQuery( "SELECT * FROM "+t_name.trim()+" WHERE ("+col_name+"='"+id+"') ", null );
            res.moveToFirst();
            while(res.isAfterLast() == false) {
                array_list.add(res.getString(0));
               // Toast.makeText(context,"check "+res.getString(0),Toast.LENGTH_SHORT).show();
                res.moveToNext();

            }
        } catch (Exception e) {
            k=0;
            System.out.println("if eixste "+e.getMessage());
        }
        return  array_list.size();

    }

    public String check_new_record(String t_name,String value,String id){

        String sql = "SELECT * FROM "+t_name+"  where "+id+"='"+value+"'";

        try {
            String result;
             // Connection conn = this.connect();

               Cursor res = sqlite_connection1.rawQuery( sql, null );
            res.moveToFirst();
                result = res.getString(res.getColumnIndex("flagTransmis"));
               //  System.out.println(rs.getString("flagtransmis"));
           // }

          if(!result.equals(""))
                return result;
           else

                  return "";
        }catch (Exception e) {
            System.out.println("check new"+e.getMessage());
            return "";
            // JOptionPane.showMessageDialog(null,"you've to restart the application ");

        }


    }

    private void local_addresse(){
        try {
            int n = 0;
            try  {
                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM addresse where flagTransmis =''", null );
                res.moveToFirst();
                String sql_mysql = "INSERT INTO `addresse`(`idAddresse`, `premiereAddresse`, `idCommune`, `flagTransmis`) VALUES (?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        try{
                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idAddresse")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("premiereAddresse")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idCommune")));
                            stmt_mysql.setString(4,now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("addresse",now,res.getString(res.getColumnIndex("idAddresse")),"idAddresse");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local addresse' table");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void local_allergiePatient(){

        try {
            int n = 0;
            try  {

                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM allergiePatient where flagTransmis =''", null );
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `allergiepatient`(`idAllergiePatient`, `idAllergie`, `idPatient`, `flagTransmis`) VALUES (?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try{

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idAllergiePatient")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idAllergie")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idPatient")));
                            stmt_mysql.setString(4,now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("allergiepatient",now,res.getString(res.getColumnIndex("idAllergiePatient")),"idAllergiePatient");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local allergiepatient' table");
            //  Toast.makeText(context,n+" has been add to 'local allergiepatient' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }




    private void local_antecedentPatient(){

        try {
            int n = 0;
            try  {

                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM antecedentPatient where flagTransmis ='' ", null );
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `antecedentPatient`(`idAntecedentPatient`, `idMaladie`, `idPatient`, `flagTransmis`)  VALUES(?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try{

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idAntecedentPatient")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idMaladie")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idPatient")));
                            stmt_mysql.setString(4, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("antecedentPatient",now,res.getString(res.getColumnIndex("idAntecedentPatient")),"idAntecedentPatient");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local antecedentPatient' table");
            // Toast.makeText(context,n+" has been add to 'local antecedentPatient' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_conferencecontact(){

        try {
            int n = 0;
            try  {

                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM conferencecontact where flagTransmis ='' ", null );
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `conferencecontact`(`idConferenceContact`, `idMedecin`, `nomComplet`, `description`, `flagTransmis`) VALUES (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try{

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idConferenceContact")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idMedecin")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("nomComplet")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("description")));
                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("conferencecontact",now,res.getString(res.getColumnIndex("idConferenceContact")),"idConferenceContact");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local conferencecontact' table");
            // Toast.makeText(context,n+" has been add to 'local conferencecontact' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



    private void local_conseil(){

        try {
            int n = 0;
            try  {

                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM conseil where flagTransmis ='' ", null );
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `conseil`(`idConseil`, `idConsultation`, `description`, `flagTransmis`) VALUES (?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try{

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idConseil")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("description")));
                            stmt_mysql.setString(4, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("conseil",now,res.getString(res.getColumnIndex("idConseil")),"idConseil");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local conseil' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_consultation(){

        try {
            int n = 0;
            try  {

                Cursor res = sqlite_connection1.rawQuery( "SELECT *  FROM consultation where flagTransmis ='' ", null );
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `consultation`(`idConsultation`, `dateConsultation`, `idPatient`, `idMedecin`, `description`, `flagTransmis` ) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try{

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("dateConsultation")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idPatient")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("idMedecin")));
                            stmt_mysql.setString(5, res.getString(res.getColumnIndex("description")));
                            stmt_mysql.setString(6, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("consultation",now,res.getString(res.getColumnIndex("idConsultation")),"idConsultation");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n+" has been add to 'local consultation' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



    private void local_contact() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM contact where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `contact`(`idContact`, `idPersonne`, `telephoneContact`, `telephoneUrgence`, `email`, `flagTransmis`) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idContact")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idPersonne")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("telephoneContact")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("telephoneUrgence")));
                            stmt_mysql.setString(5, res.getString(res.getColumnIndex("email")));
                            stmt_mysql.setString(6, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("contact", now, res.getString(res.getColumnIndex("idContact")), "idContact");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local contact' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_contacturgence() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM contacturgence where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `contactUrgence`(`idEmergencyContact`, `idPersonne`, `relation`, `nomRelation`, `telephoneRelation`, `flagTransmis`) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idEmergencyContact")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idPersonne")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("relation")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("nomRelation")));
                            stmt_mysql.setString(5, res.getString(res.getColumnIndex("telephoneRelation")));
                            stmt_mysql.setString(6, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("contactUrgence", now, res.getString(res.getColumnIndex("idEmergencyContact")), "idEmergencyContact");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local contactUrgence' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



    private void local_diagnostic() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM diagnostic where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `diagnostic`(`idDiagnostic`, `idMaladie`, `idConsultation`, `description`, `flagTransmis`) VALUES (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idDiagnostic")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idMaladie")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("description")));
                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("diagnostic", now, res.getString(res.getColumnIndex("idDiagnostic")), "idDiagnostic");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local diagnostic' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_examen() {
        try {
            int n = 0;
            try {
                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM examen where flagTransmis ='' ", null);
                res.moveToFirst();
                String sql_mysql = "INSERT INTO `examen`(`idExamen`, `idConsultation`, `idTypeExamens`, `valeur`, `flagTransmis`) VALUES (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();
                        try {
//                            System.out.println(send_diagnostic(res.getString(res.getColumnIndex("valeur"))));
                                  if(send_diagnostic(res.getString(res.getColumnIndex("valeur")))) {
                                      n++;

                                      stmt_mysql.setString(1, res.getString(res.getColumnIndex("idExamen")));
                                      stmt_mysql.setString(2, res.getString(res.getColumnIndex("idConsultation")));
                                      stmt_mysql.setString(3, res.getString(res.getColumnIndex("idTypeExamens")));
                                      stmt_mysql.setString(4, res.getString(res.getColumnIndex("valeur")));
                                      stmt_mysql.setString(5, now);

                                      stmt_mysql.executeUpdate();

                                      this.setRowSended("examen", now, res.getString(res.getColumnIndex("idExamen")), "idExamen");
                                  }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local examen' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void local_historiquepresence() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM historiquepresence where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `historiquePresence`(`idPresence`, `idMedecin`, `dateHistorique`, `description`, `flagTransmis`) VALUES (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();
                        try {
                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idPresence")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idMedecin")));

                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("dateHistorique")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("description")));
                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("historiquePresence", now, res.getString(res.getColumnIndex("idPresence")), "idPresence");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local historiquePresence' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void local_patient() {
        try {
            int n = 0;
            try {
                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM patient where flagTransmis ='' ", null);
                res.moveToFirst();
                String sql_mysql = "INSERT INTO `patient`(`idPatient`, `nomPatient`, `prenomPatient`, `genrePatient`, `dateNaissancePatient`, `groupeSanguinPatient`, `photoPatient`, `numeroIdentitePatient`, `uidPatient`, `nationalitePatient`, `statusMatrimonialPatient`, `idAddresse`, `dateRegistration`, `flagTransmis`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();
                        try {
                            if(send(res.getString(res.getColumnIndex("idPatient")))) {
                                n++;
                                stmt_mysql.setString(1, res.getString(res.getColumnIndex("idPatient")));
                                stmt_mysql.setString(2, res.getString(res.getColumnIndex("nomPatient")));
                                stmt_mysql.setString(3, res.getString(res.getColumnIndex("prenomPatient")));
                                stmt_mysql.setString(4, res.getString(res.getColumnIndex("genrePatient")));
                                stmt_mysql.setString(5, res.getString(res.getColumnIndex("dateNaissancePatient")));
                                stmt_mysql.setString(6, res.getString(res.getColumnIndex("groupeSanguinPatient")));
                                stmt_mysql.setString(7, res.getString(res.getColumnIndex("photoPatient")));
                                stmt_mysql.setString(8, res.getString(res.getColumnIndex("numeroIdentitePatient")));
                                stmt_mysql.setString(9, res.getString(res.getColumnIndex("uidPatient")));
                                stmt_mysql.setString(10, res.getString(res.getColumnIndex("nationalitePatient")));
                                stmt_mysql.setString(11, res.getString(res.getColumnIndex("statusMatrimonialPatient")));
                                stmt_mysql.setString(12, res.getString(res.getColumnIndex("idAddresse")));
                                stmt_mysql.setString(13, res.getString(res.getColumnIndex("dateRegistration")));
                                stmt_mysql.setString(14, now);
                                stmt_mysql.executeUpdate();
                                this.setRowSended("patient", now, res.getString(res.getColumnIndex("idPatient")), "idPatient");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local patient' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void local_permission() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM permission where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `permission`(`idPermission`, `idPatient`, `idMedecin`, `dateExpiration`, `type`, `flagTransmis`) VALUES  (?,?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idPermission")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idPatient")));

                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idMedecin")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("dateExpiration")));
                            stmt_mysql.setString(5, res.getString(res.getColumnIndex("type")));
                            stmt_mysql.setString(6, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("permission", now, res.getString(res.getColumnIndex("idPermission")), "idPermission");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local permission' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_prescription() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM prescription where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `prescription`(`idPrescription`, `idConsultation`, `datePrescription`, `description`, `flagTransmis`) VALUES  (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idPrescription")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idConsultation")));

                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("datePrescription")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("description")));

                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("prescription", now, res.getString(res.getColumnIndex("idPresence")), "idPresence");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local prescription' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }




    private void local_signesvitaux() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM signesvitaux where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `signesVitaux`(`idSignesVitaux`, `idCatSV`, `idConsultation`, `valeur`, `flagTransmis`)  VALUES  (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idSignesVitaux")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idCatSV")));

                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("valeur")));

                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("signesVitaux", now, res.getString(res.getColumnIndex("idSignesVitaux")), "idSignesVitaux");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local signesVitaux' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_symptomespatient() {
        try {
            int n = 0;
            try {
                Cursor res = sqlite_connection1.rawQuery("SELECT * FROM symptomespatient where flagTransmis ='' ", null);
                res.moveToFirst();
                String sql_mysql = "INSERT INTO `symptomesPatient`(`idSymptomesPatient`, `idSymptome`, `idConsultation`, `flagTransmis`)  VALUES  (?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idSymptomesPatient")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idSymptome")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(4, now);

                            stmt_mysql.executeUpdate();

                            this.setRowSended("symptomesPatient", now, res.getString(res.getColumnIndex("idSymptomesPatient")), "idSymptomesPatient");

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been add to 'local symptomesPatient' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }



    private void local_vaccinationpatient() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM vaccinationpatient where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `vaccinationPatient`(`idVaccinationPatient`, `idConsultation`, `idTypeVaccination`, `dateVaccination`, `flagTransmis`)  VALUES  (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idVaccinationPatient")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idConsultation")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idTypeVaccination")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("dateVaccination")));
                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();
                            this.setRowSended("vaccinationPatient", now, res.getString(res.getColumnIndex("idVaccinationPatient")), "idVaccinationPatient");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been added to 'local vaccinationPatient' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_vicepatient() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM vicepatient where flagTransmis ='' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `vicePatient`(`idVicePatient`, `idVice`, `idPatient`, `flagTransmis`)  VALUES  (?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idVicePatient")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idVice")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idPatient")));
                            stmt_mysql.setString(4, now);

                            stmt_mysql.executeUpdate();
                            this.setRowSended("vicePatient", now, res.getString(res.getColumnIndex("idVicePatient")), "idVicePatient");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been added to 'local vicepatient' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    private void local_relation() {

        try {
            int n = 0;
            try {

                Cursor res = sqlite_connection1.rawQuery("SELECT *  FROM relation where flagTransmis <>'' ", null);
                res.moveToFirst();


                String sql_mysql = "INSERT INTO `relation`(`idRelation`, `idPatient`, `idRelationPatient`, `typeRelation`, `flagTransmis`) VALUES  (?,?,?,?,?)";
                try (PreparedStatement stmt_mysql = mysql_connection.prepareStatement(sql_mysql)) {
                    n = 0;
                    while ((res.isAfterLast() == false)) {
                        //  Toast.makeText(context,res.getString(res.getColumnIndex("idAllergiePatient")),Toast.LENGTH_SHORT).show();

                        try {

                            n++;
                            stmt_mysql.setString(1, res.getString(res.getColumnIndex("idRelation")));
                            stmt_mysql.setString(2, res.getString(res.getColumnIndex("idPatient")));
                            stmt_mysql.setString(3, res.getString(res.getColumnIndex("idRelationPatient")));
                            stmt_mysql.setString(4, res.getString(res.getColumnIndex("typeRelation")));
                            stmt_mysql.setString(5, now);

                            stmt_mysql.executeUpdate();
                            this.setRowSended("relation", now, res.getString(res.getColumnIndex("idRelation")), "idRelation");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        res.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(n + " has been added to 'local relation' table");
            //  Toast.makeText(context,n+" has been add to 'local conseil' table",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void setRowSended(String t_name,String check,String id,String col){
        try {
            ContentValues cv = new ContentValues();
            cv.put("flagTransmis", check);
            sqlite_connection1.update(t_name, cv, col+" = ?", new String[]{id});
            // set the corresponding param

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean download (String fileName) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;

        System.out.println(fileName);
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;

            File downloadFile1 = new File(Environment.getExternalStorageDirectory()+File.separator+"Tulip_sante/Medecin/"+fileName);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));

            channelSftp.get("sante_ftp_folder/public/uploads"+"/"+fileName, outputStream1);
            System.out.println("File downloaded successfully ");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }

    public boolean send(String idPatient) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;

        System.out.println("idPatient"+idPatient);
//        System.out.println("fileName"+fileName);

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            channelSftp.mkdir("sante_ftp_folder/public/uploads/Patients/"+idPatient);
            channelSftp.mkdir("sante_ftp_folder/public/uploads/Patients/"+idPatient+"/Personal");
            channelSftp.mkdir("sante_ftp_folder/public/uploads/Patients/"+idPatient+"/Diagnostic");
            File f = new File(Environment.getExternalStorageDirectory()+File.separator+"Tulip_sante/Patients/"+idPatient+"/Personal");
            System.out.println("---- File Size ----");
            System.out.println(f.length());
            File[] files = f.listFiles();
            for (File file : files) {
                channelSftp.put(new FileInputStream(
                        f + File.separator + file.getName()),
                        "sante_ftp_folder/public/uploads/Patients/" + idPatient + "/Personal/" + file.getName());
                System.out.println(file.getName());
            }

            System.out.println("File uploaded successfully to server.");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }

    public boolean send_diagnostic(String fileName) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;


        System.out.println("fileName"+fileName);

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
             // channelSftp.cd("sante_ftp_folder/public/uploads/Patients/");
          //  channelSftp.mkdir("sante_ftp_folder/public/uploads/Patients/"+idPatient);
          //  channelSftp.mkdir("sante_ftp_folder/public/uploads/Patients/"+idPatient+"/Personal");
            File f = new File(Environment.getExternalStorageDirectory()+File.separator+"Tulip_sante/Patients/"+fileName);
            channelSftp.put(new FileInputStream(f), "sante_ftp_folder/public/uploads/Patients/"+fileName);

            System.out.println("File uploaded successfully to server.");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }

    public boolean downloadPatient(String idPatient) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;

            downloadFromFolder(channelSftp,"sante_ftp_folder/public/uploads/Patients/"+
                    idPatient+File.separator+"Personal/","Personal",idPatient);

            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }

    public boolean downloadDiagnostic(String idPatient) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;

            downloadFromFolder(channelSftp,"sante_ftp_folder/public/uploads/Patients/"+
                    idPatient+File.separator+"Diagnostic/","Diagnostic",idPatient);
            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }


    static void downloadFromFolder(ChannelSftp channelSftp, String folder,String type, String idPatient) throws SftpException {
        Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(folder);
        System.out.println(entries);

        //download all from root folder
        for (ChannelSftp.LsEntry en : entries) {
            if (en.getFilename().equals(".") || en.getFilename().equals("..") || en.getAttrs().isDir()) {
                continue;
            }

            System.out.println(en.getFilename());

            File downloadFile = new File(
                    Environment
                            .getExternalStorageDirectory()
                            +File.separator
                            +"Tulip_sante/Patients/"
                            +idPatient+"/"+ type +"/"+ en.getFilename());
            try {
                if(!downloadFile.exists()) {
                    OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
                    channelSftp.get(folder + en.getFilename(), outputStream1);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /*public boolean downloadPatient(String fileName,String idPatient) {
        String SFTPHOST ="192.168.1.28";
        int SFTPPORT = 2223;
        String SFTPUSER = ftp_user;
        String SFTPPASS = ftp_pass;

        System.out.println(fileName);
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;

            File downloadFile1 = new File(
                    Environment
                            .getExternalStorageDirectory()
                            +File.separator
                            +"Tulip_sante/Patients/"
                            +idPatient+"/Personal/");
            if(!downloadFile1.exists()){
                downloadFile1.mkdirs();
            }
            File downloadFile = new File(
                    Environment
                            .getExternalStorageDirectory()
                            +File.separator
                            +"Tulip_sante/Patients/"
                            +idPatient+"/Personal/"+fileName);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));

            channelSftp.get("sante_ftp_folder/public/uploads/Patients/"+
                    idPatient+File.separator+"Personal"+File.separator+fileName, outputStream1);
            System.out.println("File downloaded successfully ");
            return true;
        } catch (Exception ex) {
            System.out.println("Exception found while tranfer the response."+ex);
            return false;
        } finally {
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }*/

}

