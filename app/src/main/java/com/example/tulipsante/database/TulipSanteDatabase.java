package com.example.tulipsante.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.tulipsante.dao.AddresseDao;
import com.example.tulipsante.dao.AllergieDao;
import com.example.tulipsante.dao.AllergiePatientDao;
import com.example.tulipsante.dao.AntecedentPatientDao;
import com.example.tulipsante.dao.CategorieSigneVitauxDao;
import com.example.tulipsante.dao.CommuneDao;
import com.example.tulipsante.dao.ConferenceContactDao;
import com.example.tulipsante.dao.ConseilDao;
import com.example.tulipsante.dao.ConsultationDao;
import com.example.tulipsante.dao.ContactDao;
import com.example.tulipsante.dao.ContactUrgenceDao;
import com.example.tulipsante.dao.DepartementDao;
import com.example.tulipsante.dao.DiagnosticDao;
import com.example.tulipsante.dao.ExamensDao;
import com.example.tulipsante.dao.HistoriquePresenceDao;
import com.example.tulipsante.dao.HopitalDao;
import com.example.tulipsante.dao.MaladieDao;
import com.example.tulipsante.dao.MedecinDao;
import com.example.tulipsante.dao.NouvellesDao;
import com.example.tulipsante.dao.ParametreDao;
import com.example.tulipsante.dao.PatientDao;
import com.example.tulipsante.dao.PaysDao;
import com.example.tulipsante.dao.PermissionDao;
import com.example.tulipsante.dao.PrescriptionDao;
import com.example.tulipsante.dao.ReferenceDao;
import com.example.tulipsante.dao.RegionDao;
import com.example.tulipsante.dao.RelationDao;
import com.example.tulipsante.dao.SignesVitauxDao;
import com.example.tulipsante.dao.SpecialiteDao;
import com.example.tulipsante.dao.SuperAdminDao;
import com.example.tulipsante.dao.SymptomeDao;
import com.example.tulipsante.dao.SymptomesPatientDao;
import com.example.tulipsante.dao.TypeExamensDao;
import com.example.tulipsante.dao.TypeVaccinationDao;
import com.example.tulipsante.dao.UtilisateurDao;
import com.example.tulipsante.dao.VaccinationPatientDao;
import com.example.tulipsante.dao.ViceDao;
import com.example.tulipsante.dao.VicePatientDao;
import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.models.ConferenceContact;
import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Diagnostic;
import com.example.tulipsante.models.Examen;
import com.example.tulipsante.models.Nouvelles;
import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.models.Prescription;
import com.example.tulipsante.models.Reference;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.models.SignesVitaux;
import com.example.tulipsante.models.SuperAdmin;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.SymptomesPatient;
import com.example.tulipsante.models.TypeExamens;
import com.example.tulipsante.models.TypeVaccination;
import com.example.tulipsante.models.VaccinationPatient;
import com.example.tulipsante.utils.GeneralPurposeFunctions;
import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.AllergiePatient;
import com.example.tulipsante.models.AntecedentPatient;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.Departement;
import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.models.Hopital;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Pays;
import com.example.tulipsante.models.Permission;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.models.Specialite;
import com.example.tulipsante.models.Utilisateur;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.models.VicePatient;

@Database(entities = {
        Patient.class,
        Utilisateur.class,
        Medecin.class,
        Departement.class,
        Specialite.class,
        Hopital.class,
        Addresse.class,
        Contact.class,
        HistoriquePresence.class,
        Pays.class,
        Region.class,
        Commune.class,
        Permission.class,
        Vice.class,
        VicePatient.class,
        Allergie.class,
        AllergiePatient.class,
        Maladie.class,
        AntecedentPatient.class,
        Consultation.class,
        Symptome.class,
        ContactUrgence.class,
        CategorieSigneVitaux.class,
        SignesVitaux.class,
        SymptomesPatient.class,
        Diagnostic.class,
        TypeExamens.class,
        Examen.class,
        Prescription.class,
        Conseil.class,
        Reference.class,
        Nouvelles.class,
        VaccinationPatient.class,
        TypeVaccination.class,
        Relation.class,
        ConferenceContact.class,
        Parametre.class,
        SuperAdmin.class

}, version = 1)
public abstract class TulipSanteDatabase extends RoomDatabase {
    private static TulipSanteDatabase instance;
    public abstract PatientDao patientDao();
    public abstract UtilisateurDao utilisateurDao();
    public abstract DepartementDao departementDao();
    public abstract SpecialiteDao specialiteDao();
    public abstract MedecinDao medecinDao();
    public abstract HopitalDao hopitalDao();
    public abstract AddresseDao addresseDao();
    public abstract ContactDao contactDao();
    public abstract ContactUrgenceDao contactUrgenceDao();
    public abstract HistoriquePresenceDao historiquePresenceDao();
    public abstract PaysDao paysDao();
    public abstract RegionDao regionDao();
    public abstract CommuneDao communeDao();
    public abstract PermissionDao permissionDao();

    public abstract ViceDao viceDao();
    public abstract VicePatientDao vicePatientDao();

    public abstract AllergieDao allergieDao();
    public abstract AllergiePatientDao allergiePatientDao();

    public abstract MaladieDao maladieDao();
    public abstract AntecedentPatientDao antecedentPatientDao();

    public abstract ConsultationDao consultationDao();

    public abstract SymptomeDao symptomeDao();
    public abstract SymptomesPatientDao symptomesPatientDao();

    public abstract CategorieSigneVitauxDao categorieSigneVitauxDao();
    public abstract SignesVitauxDao signesVitauxDao();

    public abstract DiagnosticDao diagnosticDao();

    public abstract ExamensDao examensDao();
    public abstract TypeExamensDao typeExamensDao();

    public abstract PrescriptionDao prescriptionDao();
    public abstract ConseilDao conseilDao();

    public abstract ReferenceDao referenceDao();
    public abstract NouvellesDao nouvellesDao();

    public abstract VaccinationPatientDao vaccinationPatientDao();
    public abstract TypeVaccinationDao typeVaccinationDao();

    public abstract RelationDao relationDao();
    public abstract ConferenceContactDao conferenceContactDao();

    public abstract ParametreDao parametreDao();
    public abstract SuperAdminDao superAdminDao();

    public static synchronized TulipSanteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TulipSanteDatabase.class, "tulip_sante_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PatientDao patientDao;
        private UtilisateurDao utilisateurDao;
        private DepartementDao departementDao;
        private SpecialiteDao specialiteDao;
        private MedecinDao medecinDao;
        private HopitalDao hopitalDao;
        private AddresseDao addresseDao;
        private ContactDao contactDao;
        private ContactUrgenceDao contactUrgenceDao;

        private PaysDao paysDao;
        private RegionDao regionDao;
        private CommuneDao communeDao;
        private PermissionDao permissionDao;
        private ViceDao viceDao;
        private AllergieDao allergieDao;
        private MaladieDao maladieDao;
        private ConsultationDao consultationDao;
        private SymptomeDao symptomeDao;

        private CategorieSigneVitauxDao categorieSigneVitauxDao;
        private SignesVitauxDao signesVitauxDao;

        private TypeExamensDao typeExamensDao;
        private NouvellesDao nouvellesDao;

        private AllergiePatientDao allergiePatientDao;

        private TypeVaccinationDao typeVaccinationDao;

        private RelationDao relationDao;
        private ConferenceContactDao conferenceContactDao;

        private SuperAdminDao superAdminDao;

        private PopulateDbAsyncTask(TulipSanteDatabase db) {
            patientDao = db.patientDao();
            utilisateurDao = db.utilisateurDao();
            departementDao = db.departementDao();
            specialiteDao = db.specialiteDao();
            medecinDao = db.medecinDao();
            hopitalDao = db.hopitalDao();
            addresseDao = db.addresseDao();
            contactDao = db.contactDao();
            paysDao = db.paysDao();
            regionDao = db.regionDao();
            communeDao = db.communeDao();
            permissionDao = db.permissionDao();
            viceDao = db.viceDao();
            allergieDao = db.allergieDao();
            maladieDao = db.maladieDao();
            consultationDao = db.consultationDao();
            symptomeDao = db.symptomeDao();
            contactUrgenceDao = db.contactUrgenceDao();
            categorieSigneVitauxDao = db.categorieSigneVitauxDao();
            signesVitauxDao = db.signesVitauxDao();
            typeExamensDao = db.typeExamensDao();
            nouvellesDao = db.nouvellesDao();
            allergiePatientDao = db.allergiePatientDao();
            typeVaccinationDao = db.typeVaccinationDao();
            relationDao = db.relationDao();
            conferenceContactDao = db.conferenceContactDao();
            superAdminDao = db.superAdminDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // TODO: 28/05/21 check that later
            superAdminDao.insert(new SuperAdmin(
                    GeneralPurposeFunctions.idTable(),
                    "superAdmin",
                    "123",
                    ""
            ));
            return null;
        }
    }
}