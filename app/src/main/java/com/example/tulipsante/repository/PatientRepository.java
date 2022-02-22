package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.AddresseDao;
import com.example.tulipsante.dao.AllergieDao;
import com.example.tulipsante.dao.AllergiePatientDao;
import com.example.tulipsante.dao.AntecedentPatientDao;
import com.example.tulipsante.dao.CommuneDao;
import com.example.tulipsante.dao.ConsultationDao;
import com.example.tulipsante.dao.ContactDao;
import com.example.tulipsante.dao.ContactUrgenceDao;
import com.example.tulipsante.dao.MaladieDao;
import com.example.tulipsante.dao.PatientDao;
import com.example.tulipsante.dao.RegionDao;
import com.example.tulipsante.dao.RelationDao;
import com.example.tulipsante.dao.SignesVitauxDao;
import com.example.tulipsante.dao.VaccinationPatientDao;
import com.example.tulipsante.dao.ViceDao;
import com.example.tulipsante.dao.VicePatientDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Allergie;
import com.example.tulipsante.models.AllergiePatient;
import com.example.tulipsante.models.AntecedentPatient;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Patient;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.models.Relation;
import com.example.tulipsante.models.uIModels.ConsultationUMedecin;
import com.example.tulipsante.models.uIModels.DashPatientProfileData;
import com.example.tulipsante.models.uIModels.RelationXPatient;
import com.example.tulipsante.models.uIModels.VaccinePatient;
import com.example.tulipsante.models.uIModels.VaccineTableDialog;
import com.example.tulipsante.models.Vice;
import com.example.tulipsante.models.VicePatient;

import java.util.List;

public class PatientRepository {
    private PatientDao patientDao;
    private AddresseDao addresseDao;
    private ContactDao contactDao;
    private RegionDao regionDao;
    private CommuneDao communeDao;
    // *
    private ContactUrgenceDao contactUrgenceDao;
    private ConsultationDao consultationDao;

    private ViceDao viceDao;
    private AllergieDao allergieDao;
    private MaladieDao maladieDao;

    private SignesVitauxDao signesVitauxDao;
    //Patient
    private VicePatientDao vicePatientDao;
    private AllergiePatientDao allergiePatientDao;
    private AntecedentPatientDao antecedentPatientDao;

    private VaccinationPatientDao vaccinationPatientDao;

    private RelationDao relationDao;


    private LiveData<List<Patient>> allPatients;
    private LiveData<List<DashPatientProfileData>> weighData;

    public PatientRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        patientDao = tulipSanteDatabase.patientDao();
        addresseDao = tulipSanteDatabase.addresseDao();
        contactDao = tulipSanteDatabase.contactDao();
        contactUrgenceDao = tulipSanteDatabase.contactUrgenceDao();

        regionDao = tulipSanteDatabase.regionDao();
        communeDao = tulipSanteDatabase.communeDao();

        viceDao = tulipSanteDatabase.viceDao();
        allergieDao = tulipSanteDatabase.allergieDao();
        maladieDao = tulipSanteDatabase.maladieDao();
        signesVitauxDao = tulipSanteDatabase.signesVitauxDao();

        //patient
        vicePatientDao = tulipSanteDatabase.vicePatientDao();
        allergiePatientDao = tulipSanteDatabase.allergiePatientDao();
        antecedentPatientDao = tulipSanteDatabase.antecedentPatientDao();
        consultationDao = tulipSanteDatabase.consultationDao();
        vaccinationPatientDao = tulipSanteDatabase.vaccinationPatientDao();


        relationDao = tulipSanteDatabase.relationDao();
    }

    public void insert(Patient patient){
        new InsertPatientAsyncTask(patientDao).execute(patient);
    }

    public void insertPatientAddresse(Addresse addresse) {
        new InsertAddressPatientAsyncTask(addresseDao).execute(addresse);
    }

    public void insertPatientContact(Contact contact) {
        new InsertContactPatientAsyncTask(contactDao).execute(contact);
    }

    // INSERT EMERGENCY CONTACT
    public void insertContactUrgenceP(ContactUrgence contactUrgence) {
        new InsertContactUrgenceAsyncTask(contactUrgenceDao).execute(contactUrgence);
    }

    public List<Commune> getCommuneFromRegion(String idRegion) {
        List<Commune> commune = null;
        try {
            commune = new GetCommuneFromRAsyncTask(communeDao).execute(idRegion).get();
        } catch (Exception ignored){}
        return commune;
    }

    public List<Region> regionList() {
        List<Region> regions = null;
        try {
            regions = new GetRegionsAsyncTask(regionDao).execute().get();
        } catch (Exception ignored){}
        return regions;
    }

    public void update(Patient patient) {
        new UpdatePatientAsyncTask(patientDao).execute(patient);
    }

    public void delete(Patient patient) {
        new DeletePatientAsyncTask(patientDao).execute(patient);
    }

    public LiveData<List<Patient>> getAllPatients() {
        return allPatients;
    }

    public LiveData<List<Patient>> getAllPatientsLive(List<String> data) {
        LiveData<List<Patient>> patients = null;
        if(data.get(0).equals("Newest")) {
            patients = patientDao.getAllPatientsLive(data.get(1));
        }
        else if(data.get(0).equals("Oldest")) {
            patients = patientDao.getAllPatientsListOldest(data.get(1));
        }
        else {
            if(data.get(2).length() == 8 && (Character.isUpperCase(data.get(2).charAt(0)) || Character.isDefined(data.get(2).charAt(0)))) {
                patients = patientDao.getPatientWithTag(data.get(2));
            }
            else {
                patients = patientDao.getPatientWithFirstNameAndId(data.get(2));
            }
        }
        return patients;
    }

    public LiveData<List<Patient>> getPatientFromSearchAttr(String searchAttr) {
        LiveData<List<Patient>> patients = null;
        patients = patientDao.getPatientWithFirstNameAndId(searchAttr);
        return patients;
    }

    // **
    // Get all Vices
    public LiveData<List<Vice>> getAllVices() {
        return viceDao.getAllVices();
    }

    // **
    // Get all Allergies
    public LiveData<List<Allergie>> getAllergies(String typeAllergies) {
        return allergieDao.getAllAllergies(typeAllergies);
    }

    // **
    // GET ALL MALADIES
    public LiveData<List<Maladie>> getMaladies() {
        return maladieDao.getAllMaladies();
    }

    // ***
    // Insert Vice Patient
    public void insertVicePatient(VicePatient vicePatient){
        new InsertVicePatientAsyncTask(vicePatientDao).execute(vicePatient);
    }

    // ***
    // Inset Vice Patient Async
    private static class InsertVicePatientAsyncTask extends AsyncTask<VicePatient, Void, Void> {
        private VicePatientDao vicePatientDao;
        private InsertVicePatientAsyncTask(VicePatientDao vicePatientDao) {
            this.vicePatientDao = vicePatientDao;
        }
        @Override
        protected Void doInBackground(VicePatient... vicePatients) {
            vicePatientDao.insertVicePatient(vicePatients[0]);
            return null;
        }
    }

    // **
    // Insert Allergie Patient
    public void insertAllergiePatient(AllergiePatient allergiePatient){
        new InsertAllergiePatientAsyncTask(allergiePatientDao).execute(allergiePatient);
    }

    // ***
    // Inset Allergie Patient Async
    private static class InsertAllergiePatientAsyncTask extends AsyncTask<AllergiePatient, Void, Void> {
        private AllergiePatientDao allergiePatientDao;
        private InsertAllergiePatientAsyncTask(AllergiePatientDao allergiePatientDao) {
            this.allergiePatientDao = allergiePatientDao;
        }
        @Override
        protected Void doInBackground(AllergiePatient... allergiePatients) {
            allergiePatientDao.insertAllergiePatient(allergiePatients[0]);
            return null;
        }
    }

    // **
    // Insert Antecedent Patient
    public void insertAntecedentPatient(AntecedentPatient antecedentPatient){
        new InsertAntecedentPatientAsyncTask(antecedentPatientDao).execute(antecedentPatient);
    }

    // ***
    // Inset Antecedent Patient Async
    private static class InsertAntecedentPatientAsyncTask extends AsyncTask<AntecedentPatient, Void, Void> {
        private AntecedentPatientDao antecedentPatientDao;
        private InsertAntecedentPatientAsyncTask(AntecedentPatientDao antecedentPatientDao) {
            this.antecedentPatientDao = antecedentPatientDao;
        }
        @Override
        protected Void doInBackground(AntecedentPatient... antecedentPatients) {
            antecedentPatientDao.insertAntecedentPatient(antecedentPatients[0]);
            return null;
        }
    }


    private static class InsertPatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;
        private InsertPatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patient) {
            patientDao.insert(patient[0]);
            return null;
        }
    }


    private static class InsertAddressPatientAsyncTask extends AsyncTask<Addresse, Void, Void> {
        private AddresseDao addresseDao;
        private InsertAddressPatientAsyncTask(AddresseDao addresseDao) {
            this.addresseDao = addresseDao;
        }
        @Override
        protected Void doInBackground(Addresse... addresses) {
            addresseDao.insert(addresses[0]);
            return null;
        }
    }

    private static class InsertContactPatientAsyncTask extends AsyncTask<Contact, Void, Void> {
        private ContactDao contactDao;
        private InsertContactPatientAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insert(contacts[0]);
            return null;
        }
    }

    private static class InsertContactUrgenceAsyncTask extends AsyncTask<ContactUrgence, Void, Void> {
        private ContactUrgenceDao contactUrgenceDao;
        private InsertContactUrgenceAsyncTask(ContactUrgenceDao contactUrgenceDao) {
            this.contactUrgenceDao = contactUrgenceDao;
        }
        @Override
        protected Void doInBackground(ContactUrgence... contactUrgences) {
            contactUrgenceDao.insertContactUrgence(contactUrgences[0]);
            return null;
        }
    }

    private static class UpdatePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;
        private UpdatePatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patient) {
            patientDao.update(patient[0]);
            return null;
        }
    }

    private static class DeletePatientAsyncTask extends AsyncTask<Patient, Void, Void> {
        private PatientDao patientDao;
        private DeletePatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected Void doInBackground(Patient... patients) {
            patientDao.delete(patients[0]);
            return null;
        }
    }

    private static class GetCommuneFromRAsyncTask extends AsyncTask<String, Void, List<Commune>> {
        private CommuneDao communeDao;
        private GetCommuneFromRAsyncTask(CommuneDao communeDao) {
            this.communeDao = communeDao;
        }
        @Override
        protected List<Commune> doInBackground(String... data) {
            return communeDao.getCommuneFromRegion(data[0]);
        }
    }

    private static class GetRegionsAsyncTask extends AsyncTask<String, Void, List<Region>> {
        private RegionDao regionDao;

        private GetRegionsAsyncTask(RegionDao regionDao) {
            this.regionDao = regionDao;
        }
        @Override
        protected List<Region> doInBackground(String... data) {
            return regionDao.getAllRegions();
        }
    }

    // **
    // Get patient details
    public Patient getPatient(String idPatient) {
        Patient patient = null;
        try {
            patient = new GetPatientAsyncTask(patientDao).execute(idPatient).get();
        } catch (Exception e){
        }
        return patient;
    }

    // **
    // Get patient details async
    private static class GetPatientAsyncTask extends AsyncTask<String, Void, Patient> {
        private PatientDao patientDao;
        private Patient patient;
        private GetPatientAsyncTask(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected Patient doInBackground(String... data) {
            patient = patientDao.patient(data[0]);
            return patient;
        }
    }

    // **
    // Get patient details
    public ContactUrgence getPatientEmergency(String idPatient) {
        ContactUrgence urgence = null;
        try {
            urgence = new GetPatientEmergencyAsyncTask(contactUrgenceDao).execute(idPatient).get();
        } catch (Exception e){
        }
        return urgence;
    }

    // **
    // Get patient details async
    private static class GetPatientEmergencyAsyncTask extends AsyncTask<String, Void, ContactUrgence> {
        private ContactUrgenceDao contactUrgenceDao;
        private ContactUrgence contactUrgence;
        private GetPatientEmergencyAsyncTask(ContactUrgenceDao contactUrgenceDao) {
            this.contactUrgenceDao = contactUrgenceDao;
        }
        @Override
        protected ContactUrgence doInBackground(String... data) {
            contactUrgence = contactUrgenceDao.getContactUrgence(data[0]);
            return contactUrgence;
        }
    }

    // **
    // Get patient contact details
    public Contact getPatientContact(String idPatient) {
        Contact con = null;
        try {
            con = new GetContactAsyncTask(contactDao).execute(idPatient).get();
        } catch (Exception e){
        }
        return con;
    }

    // **
    // Get patient contact details async
    private static class GetContactAsyncTask extends AsyncTask<String, Void, Contact> {
        private ContactDao contactDao;
        private Contact contact;
        private GetContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected Contact doInBackground(String... data) {
            contact = contactDao.getContact(data[0]);
            return contact;
        }
    }
    // **
    // Get patient address details
    public Addresse getPatientAddresse(String idAddresse) {
        Addresse addresse = null;
        try {
            addresse = new GetAddressAsyncTask(addresseDao).execute(idAddresse).get();
        } catch (Exception e){
        }
        return addresse;
    }

    // **
    // Get patient address details async
    private static class GetAddressAsyncTask extends AsyncTask<String, Void, Addresse> {
        private AddresseDao addresseDao;
        private Addresse addresse;
        private GetAddressAsyncTask(AddresseDao addresseDao) {
            this.addresseDao = addresseDao;
        }
        @Override
        protected Addresse doInBackground(String... data) {
            addresse = addresseDao.getAddresse(data[0]);
            return addresse;
        }
    }

    // **
    // Get patient address details
    public Commune getPatientCommune(String idCommune) {
        Commune commune = null;
        try {
            commune = new GetCommuneAsyncTask(communeDao).execute(idCommune).get();
        } catch (Exception e){
        }
        return commune;
    }

    // **
    // Get patient address details async
    private static class GetCommuneAsyncTask extends AsyncTask<String, Void, Commune> {
        private CommuneDao communeDao;
        private Commune commune;
        private GetCommuneAsyncTask(CommuneDao communeDao) {
            this.communeDao = communeDao;
        }
        @Override
        protected Commune doInBackground(String... data) {
            commune = communeDao.getCommuneFomPK(data[0]);
            return commune;
        }
    }

    // **
    // Get patient address details
    public Region getPatientRegion(String idRegion) {
        Region region = null;
        try {
            region = new GetRegionAsyncTask(regionDao).execute(idRegion).get();
        } catch (Exception e){
        }
        return region;
    }

    // **
    // Get patient address details async
    private static class GetRegionAsyncTask extends AsyncTask<String, Void, Region> {
        private RegionDao regionDao;
        private Region region;
        private GetRegionAsyncTask(RegionDao regionDao) {
            this.regionDao = regionDao;
        }
        @Override
        protected Region doInBackground(String... data) {
            region = regionDao.getRegions(data[0]);
            return region;
        }
    }

    // **
    // Get patient Allergies details
    public List<Allergie> getPatientAllergies(String idPatient,String type) {
        List<Allergie> allergieList = null;
        List<String> idAllergies;
        idAllergies = allergiePatientDao.getAllergiesPatient(idPatient);
        if (type.equals("food")) {
            allergieList = allergieDao.getPatientAllergies(idAllergies,type);
        }
        else {
            allergieList = allergieDao.getPatientAllergies(idAllergies,type);
        }
        return allergieList;
    }
    // **
    // Get patient vice details
    public List<Vice> getPatientVice(String idPatient) {
        List<Vice> viceList = null;
        List<String> idVices;
        idVices = vicePatientDao.getPatientIdVice(idPatient);
        viceList = viceDao.viceList(idVices);
        return viceList;
    }
    // **
    // Get patient Maladies details
    public List<Maladie> getPatientAntecedent(String idPatient) {
        List<Maladie> maladieList = null;
        List<String> idMaladie;
        idMaladie = antecedentPatientDao.getPatientIdMaladie(idPatient);
        maladieList = maladieDao.maladieList(idMaladie);
        return maladieList;
    }

    public String numberOfConsRecord(String idPatient) {
        String res = null;
        try {
            res = new GetNumberOfConsRecordAsync(consultationDao).execute(idPatient).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetNumberOfConsRecordAsync extends AsyncTask<String, Void, String> {
        private ConsultationDao consultationDao;
        private String res;
        private GetNumberOfConsRecordAsync(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected String doInBackground(String... data) {
            res = consultationDao.numberOfConsultationRecord(data[0]);
            return res;
        }
    }

    // **
    // Get patient consultation details
    public LiveData<List<ConsultationUMedecin>> getPatientConsultation(
            String idPatient, String index) {
        LiveData<List<ConsultationUMedecin>> consultationList = null;
        if(index.length() > 8) {
            consultationList = consultationDao.getConsultationRecordSearch(idPatient,index);
        }
        else {
            consultationList = consultationDao.getConsultationRecordLive(idPatient,index);
        }
        return consultationList;
    }


    // **
    // Get basic vital details
    public String getBasicVitals(String idPatient, String typeSV) {
        String res = null;
        String idConsultation = consultationDao.getConsultationId(idPatient);
        System.out.println(idConsultation);
        if(typeSV.equals("height")) {
            res = signesVitauxDao.getValeurSV(idConsultation,"%taille%");
        }
        if (typeSV.equals("weight")) {
            res = signesVitauxDao.getValeurSV(idConsultation,"%poids%");
        }
        return res;
    }

    // **
    // Get patient contact details
    public List<DashPatientProfileData> getDashPatient(String idPatient) {
        List<DashPatientProfileData> res = null;
        // TODO: 15/04/21 cat signes vitaux id 
        res = consultationDao.getWeightData(idPatient,"%poids%");
        return res;
    }

    // **
    // Get patient consultation details
    public List<VaccinePatient> getPatientVaccination(String idPatient, String sort) {
        List<VaccinePatient> vaccinePatientList = null;
        try {
            vaccinePatientList = new GetPatientVaccinationAsyncTask(vaccinationPatientDao)
                    .execute(idPatient,sort).get();
        } catch (Exception e){
        }
        return vaccinePatientList;
    }

    // **
    // Get patient consultation details async
    private static class GetPatientVaccinationAsyncTask
            extends AsyncTask<String, Void, List<VaccinePatient>> {
        private VaccinationPatientDao vaccinationPatientDao;
        private List<VaccinePatient> vaccinePatientList;
        private GetPatientVaccinationAsyncTask(VaccinationPatientDao vaccinationPatientDao) {
            this.vaccinationPatientDao = vaccinationPatientDao;
        }
        @Override
        protected List<VaccinePatient> doInBackground(String... data) {
            if(data[1].equals("Valid")) {
                vaccinePatientList = vaccinationPatientDao.getVaccinePatient(data[0]);
            }
            else {
                vaccinePatientList = vaccinationPatientDao.getVaccinePatientSearch(data[0],"%" + data[1] + "%");
            }
            return vaccinePatientList;
        }
    }
    // **
    // Get vaccination details
    public List<VaccineTableDialog> getVaccineMore(String idPatient, String type) {
        List<VaccineTableDialog> vaccineTableDialogList = null;
        try {
            vaccineTableDialogList = new GetPatientVaccinationDialogAsyncTask(vaccinationPatientDao)
                    .execute(idPatient,type).get();
        } catch (Exception e){
        }
        return vaccineTableDialogList;
    }

    // **
    // Get vaccination details async
    private static class GetPatientVaccinationDialogAsyncTask
            extends AsyncTask<String, Void, List<VaccineTableDialog>> {
        private VaccinationPatientDao vaccinationPatientDao;
        private List<VaccineTableDialog> vaccinePatientList;
        private GetPatientVaccinationDialogAsyncTask(VaccinationPatientDao vaccinationPatientDao) {
            this.vaccinationPatientDao = vaccinationPatientDao;
        }
        @Override
        protected List<VaccineTableDialog> doInBackground(String... data) {
            vaccinePatientList = vaccinationPatientDao.getVaccineMore(data[0],data[1]);
            return vaccinePatientList;
        }
    }

    // ***
    // Insert relation Patient
    public void insertPatientRelation(Relation relation){
        new InsertRelationPatientAsyncTask(relationDao).execute(relation);
    }

    // ***
    // Inset relation patient Async
    private static class InsertRelationPatientAsyncTask extends AsyncTask<Relation, Void, Void> {
        private RelationDao relationDao;
        private InsertRelationPatientAsyncTask(RelationDao relationDao) {
            this.relationDao = relationDao;
        }
        @Override
        protected Void doInBackground(Relation... relations) {
            relationDao.insertRelation(relations[0]);
            return null;
        }
    }

    // **
    // Get relation patient
    public List<RelationXPatient> getRelationPatient(String idPatient) {
        List<RelationXPatient> relationPatientList = null;
        try {
            relationPatientList = relationDao.getRelations(idPatient);
        } catch (Exception e){
        }
        return relationPatientList;
    }

    // ***
    // Delete relation Patient
    public void deleteRelation(String idRelation){
        new DeleteRelationPatientAsyncTask(relationDao).execute(idRelation);
    }

    // ***
    // Delete relation patient Async
    private static class DeleteRelationPatientAsyncTask extends AsyncTask<String, Void, Void> {
        private RelationDao relationDao;
        private DeleteRelationPatientAsyncTask(RelationDao relationDao) {
            this.relationDao = relationDao;
        }
        @Override
        protected Void doInBackground(String... strings) {
            relationDao.deleteRelation(strings[0]);
            return null;
        }
    }

    // ***
    // Check relation Patient
    public String checkRelation(String idPatient, String idRelationPatient){
        String res = null;
        try {
            res = new CheckRelationPatientAsyncTask(relationDao)
                    .execute(idPatient, idRelationPatient).get();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // ***
    // check relation patient Async
    private static class CheckRelationPatientAsyncTask extends AsyncTask<String, Void, String> {
        private RelationDao relationDao;
        private CheckRelationPatientAsyncTask(RelationDao relationDao) {
            this.relationDao = relationDao;
        }
        @Override
        protected String doInBackground(String... strings) {
            return relationDao.checkRelation(strings[0],strings[1]);
        }
    }

    public String numberOfPatient() {
        String res = null;
        try {
            res = new GetNumberOfRecordAsync(patientDao).execute().get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetNumberOfRecordAsync extends AsyncTask<String, Void, String> {
        private PatientDao patientDao;
        private String res;
        private GetNumberOfRecordAsync(PatientDao patientDao) {
            this.patientDao = patientDao;
        }
        @Override
        protected String doInBackground(String... data) {
            res = patientDao.patientSize();
            return res;
        }
    }
}
