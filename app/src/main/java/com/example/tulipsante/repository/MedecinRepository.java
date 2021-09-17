package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.AddresseDao;
import com.example.tulipsante.dao.CommuneDao;
import com.example.tulipsante.dao.ConferenceContactDao;
import com.example.tulipsante.dao.ConsultationDao;
import com.example.tulipsante.dao.ContactDao;
import com.example.tulipsante.dao.ContactUrgenceDao;
import com.example.tulipsante.dao.DepartementDao;
import com.example.tulipsante.dao.HistoriquePresenceDao;
import com.example.tulipsante.dao.HopitalDao;
import com.example.tulipsante.dao.MedecinDao;
import com.example.tulipsante.dao.NouvellesDao;
import com.example.tulipsante.dao.PatientDao;
import com.example.tulipsante.dao.PaysDao;
import com.example.tulipsante.dao.RegionDao;
import com.example.tulipsante.dao.SpecialiteDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Addresse;
import com.example.tulipsante.models.Commune;
import com.example.tulipsante.models.ConferenceContact;
import com.example.tulipsante.models.Contact;
import com.example.tulipsante.models.ContactUrgence;
import com.example.tulipsante.models.Departement;
import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.models.Hopital;
import com.example.tulipsante.models.Medecin;
import com.example.tulipsante.models.Nouvelles;
import com.example.tulipsante.models.Pays;
import com.example.tulipsante.models.Reference;
import com.example.tulipsante.models.Region;
import com.example.tulipsante.models.Specialite;
import com.example.tulipsante.models.uIModels.HistoriquePresenceMedecin;
import com.example.tulipsante.models.uIModels.HistoriqueTacheMedecin;

import java.util.List;

public class MedecinRepository {
    private MedecinDao medecinDao;
    private SpecialiteDao specialiteDao;
    private DepartementDao departementDao;
    private HopitalDao hopitalDao;
    private AddresseDao addresseDao;
    private ContactDao contactDao;
    private ContactUrgenceDao contactUrgenceDao;
    private HistoriquePresenceDao historiquePresenceDao;
    private CommuneDao communeDao;
    private RegionDao regionDao;
    private PaysDao paysDao;
    private PatientDao patientDao;
    private NouvellesDao nouvellesDao;
    private ConsultationDao consultationDao;
    private ConferenceContactDao conferenceContactDao;


    public MedecinRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        medecinDao = tulipSanteDatabase.medecinDao();
        specialiteDao = tulipSanteDatabase.specialiteDao();
        departementDao = tulipSanteDatabase.departementDao();
        hopitalDao = tulipSanteDatabase.hopitalDao();
        addresseDao = tulipSanteDatabase.addresseDao();
        contactDao = tulipSanteDatabase.contactDao();
        contactUrgenceDao = tulipSanteDatabase.contactUrgenceDao();
        historiquePresenceDao = tulipSanteDatabase.historiquePresenceDao();
        communeDao = tulipSanteDatabase.communeDao();
        regionDao = tulipSanteDatabase.regionDao();
        paysDao = tulipSanteDatabase.paysDao();
        patientDao = tulipSanteDatabase.patientDao();
        nouvellesDao = tulipSanteDatabase.nouvellesDao();
        consultationDao = tulipSanteDatabase.consultationDao();
        conferenceContactDao = tulipSanteDatabase.conferenceContactDao();
    }

    public Medecin getMedecin(String idMedecin) {
        Medecin medecin = null;
        try {
            medecin = new GetMedecinAsyncTask(medecinDao).execute(idMedecin).get();
        } catch (Exception e){
        }
        return medecin;
    }

    public Specialite getSpecialite(String idSpecialite) {
        Specialite specialite = null;
        try {
            specialite = new SpecialiteAsyncTask(specialiteDao).execute(idSpecialite).get();
        } catch (Exception e){
        }
        return specialite;
    }

    public Departement getDepartement(String idDepartment) {
        Departement departement = null;
        try {
            departement = new GetDepartementAsyncTask(departementDao).execute(idDepartment).get();
        } catch (Exception e){
        }
        return departement;
    }

    public Hopital getHopital(String idHopital) {
        Hopital hopital = null;
        try {
            hopital = new GetHopitalAsyncTask(hopitalDao).execute(idHopital).get();
        } catch (Exception e){
        }
        return hopital;
    }

    public Addresse getAddresse(String idAddresse) {
        Addresse addresse = null;
        try {
            addresse = new GetAddresseAsyncTask(addresseDao).execute(idAddresse).get();
        } catch (Exception e){
        }
        return addresse;
    }

    public Commune getCommune(String idCommune) {
        Commune commune = null;
        try {
            commune = new GetCommuneAsyncTask(communeDao).execute(idCommune).get();
        } catch (Exception e){
        }
        return commune;
    }

    public Region getRegion(String idRegion) {
        Region region = null;
        try {
            region = new GetRegionAsyncTask(regionDao).execute(idRegion).get();
        } catch (Exception e){
        }
        return region;
    }

    public Pays getPaysMedecin(String idPays) {
        Pays pays = null;
        try {
            pays = new GetPaysAsyncTask(paysDao).execute(idPays).get();
        } catch (Exception e){
        }
        return pays;
    }

    public Contact getContact(String idPersonne) {
        Contact contact = null;
        try {
            contact = new GetContactAsyncTask(contactDao).execute(idPersonne).get();
        } catch (Exception e){
        }
        return contact;
    }

    public ContactUrgence getContactUrgence(String idPersonne) {
        ContactUrgence contact = null;
        try {
            contact = new GetContactUrgenceAsyncTask(contactUrgenceDao).execute(idPersonne).get();
        } catch (Exception e){
        }
        return contact;
    }

    public void insert(HistoriquePresence historiquePresence) {
        new InsertHistoriquePresenceAsyncTask(historiquePresenceDao).execute(historiquePresence);
    }

    private static class InsertHistoriquePresenceAsyncTask extends AsyncTask<HistoriquePresence, Void, Void> {
        private HistoriquePresenceDao historiquePresenceDao;
        private InsertHistoriquePresenceAsyncTask(HistoriquePresenceDao historiquePresenceDao) {
            this.historiquePresenceDao = historiquePresenceDao;
        }
        @Override
        protected Void doInBackground(HistoriquePresence... historiquePresences) {
            historiquePresenceDao.insert(historiquePresences[0]);
            return null;
        }
    }

    private static class GetAddresseAsyncTask extends AsyncTask<String, Void, Addresse> {
        private AddresseDao addresseDao;
        private Addresse addresse;
        private GetAddresseAsyncTask(AddresseDao addresseDao) {
            this.addresseDao = addresseDao;
        }
        @Override
        protected Addresse doInBackground(String... data) {
            addresse = addresseDao.getAddresse(data[0]);
            return addresse;
        }
    }

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

    private static class GetPaysAsyncTask extends AsyncTask<String, Void, Pays> {
        private PaysDao paysDao;
        private Pays pays;
        private GetPaysAsyncTask(PaysDao paysDao) {
            this.paysDao = paysDao;
        }
        @Override
        protected Pays doInBackground(String... data) {
            pays = paysDao.getPays(data[0]);
            return pays;
        }
    }

    private static class GetContactAsyncTask extends AsyncTask<String, Void, Contact> {
        private ContactDao contactDao;
        private Contact con;
        private GetContactAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }
        @Override
        protected Contact doInBackground(String... data) {
            con = contactDao.getContact(data[0]);
            return con;
        }
    }

    private static class GetContactUrgenceAsyncTask extends AsyncTask<String, Void, ContactUrgence> {
        private ContactUrgenceDao contactUrgenceDao;
        private ContactUrgence con;
        private GetContactUrgenceAsyncTask(ContactUrgenceDao contactUrgenceDao) {
            this.contactUrgenceDao = contactUrgenceDao;
        }
        @Override
        protected ContactUrgence doInBackground(String... data) {
            con = contactUrgenceDao.getContactUrgence(data[0]);
            return con;
        }
    }

    private static class GetDepartementAsyncTask extends AsyncTask<String, Void, Departement> {
        private DepartementDao departementDao;
        private Departement departement;
        private GetDepartementAsyncTask(DepartementDao departementDao) {
            this.departementDao = departementDao;
        }
        @Override
        protected Departement doInBackground(String... data) {
            departement = departementDao.getDepartement(data[0]);
            return departement;
        }
    }

    private static class GetHopitalAsyncTask extends AsyncTask<String, Void, Hopital> {
        private HopitalDao hopitalDao;
        private Hopital hopital;
        private GetHopitalAsyncTask(HopitalDao hopitalDao) {
            this.hopitalDao = hopitalDao;
        }
        @Override
        protected Hopital doInBackground(String... data) {
            hopital = hopitalDao.getHopital(data[0]);
            return hopital;
        }
    }

    private static class GetMedecinAsyncTask extends AsyncTask<String, Void, Medecin> {
        private MedecinDao medecinDao;
        private Medecin medecin;
        private GetMedecinAsyncTask(MedecinDao medecinDao) {
            this.medecinDao = medecinDao;
        }
        @Override
        protected Medecin doInBackground(String... data) {
            medecin = medecinDao.getMedecin(data[0]);
            return medecin;
        }
    }

    private static class SpecialiteAsyncTask extends AsyncTask<String, Void, Specialite> {
        private SpecialiteDao specialiteDao;
        private Specialite specialite;
        private SpecialiteAsyncTask(SpecialiteDao specialiteDao) {
            this.specialiteDao = specialiteDao;
        }
        @Override
        protected Specialite doInBackground(String... data) {
            specialite = specialiteDao.getSpecialite(data[0]);
            return specialite;
        }
    }

    public List<Nouvelles> getNouvelles() {
        List<Nouvelles> nouvelles = null;
        try {
            nouvelles = new NouvellesAsyncTask(nouvellesDao).execute().get();
        } catch (Exception e){
        }
        return nouvelles;
    }

    private static class NouvellesAsyncTask extends AsyncTask<String, Void, List<Nouvelles>> {
        private NouvellesDao nouvellesDao;
        private List<Nouvelles> nouvelles;
        private NouvellesAsyncTask(NouvellesDao nouvellesDao) {
            this.nouvellesDao = nouvellesDao;
        }
        @Override
        protected List<Nouvelles> doInBackground(String... data) {
            nouvelles = nouvellesDao.news();
            return nouvelles;
        }
    }

    public LiveData<List<HistoriquePresenceMedecin>> getPresenceHistory(String idMedecin, String index) {
        LiveData<List<HistoriquePresenceMedecin>> data;
        if(index.length() > 8) {
            data = historiquePresenceDao.getHistoriquePresenceSearch(idMedecin, index);
        }
        else {
            data = historiquePresenceDao.getPresenceHistory(idMedecin, index);
        }
        return data;
    }

    public LiveData<List<HistoriqueTacheMedecin>> getHistoriqueTache(String idMedecin, String index) {
        LiveData<List<HistoriqueTacheMedecin>> data;
        if(index.length() > 8) {
            data = consultationDao.getHistoriqueTacheSearch(idMedecin, index);
        }
        else {
            data = consultationDao.getHistoriqueTache(idMedecin, index);
        }
        return data;
    }

    public boolean checkIfTagExist(String uid) {
        boolean res = false;
        try {
            res = new CheckTagAsync(medecinDao).execute(uid).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class CheckTagAsync extends AsyncTask<String, Void, Boolean> {
        private MedecinDao medecinDao;
        private boolean res = false;
        private CheckTagAsync(MedecinDao medecinDao) {
            this.medecinDao = medecinDao;
        }
        @Override
        protected Boolean doInBackground(String... data) {
            if(Integer.parseInt(medecinDao.checkTag(data[0])) > 0) {
                res = true;
            }
            return res;
        }
    }

    public String getIdMed(String uid) {
        String res = null;
        try {
            res = new GetIdAsync(medecinDao).execute(uid).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetIdAsync extends AsyncTask<String, Void, String> {
        private MedecinDao medecinDao;
        private String res;
        private GetIdAsync(MedecinDao medecinDao) {
            this.medecinDao = medecinDao;
        }
        @Override
        protected String doInBackground(String... data) {
            res = medecinDao.idMedecin(data[0]);
            return res;
        }
    }

    public String numberOfRecord(String idMedecin) {
        String res = null;
        try {
            res = new GetNumberOfRecordAsync(historiquePresenceDao).execute(idMedecin).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetNumberOfRecordAsync extends AsyncTask<String, Void, String> {
        private HistoriquePresenceDao historiquePresenceDao;
        private String res;
        private GetNumberOfRecordAsync(HistoriquePresenceDao historiquePresenceDao) {
            this.historiquePresenceDao = historiquePresenceDao;
        }
        @Override
        protected String doInBackground(String... data) {
            res = historiquePresenceDao.numberOfRecord(data[0]);
            return res;
        }
    }

    public String numberOfConsRecord(String idMedecin) {
        String res = null;
        try {
            res = new GetNumberOfConsRecordAsync(consultationDao).execute(idMedecin).get();
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
            res = consultationDao.numberOfRecord(data[0]);
            return res;
        }
    }

    public void insertConfContact(ConferenceContact conferenceContact) {
        new InsertConfContactAsync(conferenceContactDao).execute(conferenceContact);
    }

    private static class InsertConfContactAsync extends AsyncTask<ConferenceContact, Void, Void> {
        private ConferenceContactDao conferenceContactDao;
        private InsertConfContactAsync(ConferenceContactDao conferenceContactDao) {
            this.conferenceContactDao = conferenceContactDao;
        }
        @Override
        protected Void doInBackground(ConferenceContact... data) {
            conferenceContactDao.insert(data[0]);
            return null;
        }
    }

    public void deleteConfContact(ConferenceContact conferenceContact) {
        new DeleteConfContactAsync(conferenceContactDao).execute(conferenceContact);
    }

    private static class DeleteConfContactAsync extends AsyncTask<ConferenceContact, Void, Void> {
        private ConferenceContactDao conferenceContactDao;
        private DeleteConfContactAsync(ConferenceContactDao conferenceContactDao) {
            this.conferenceContactDao = conferenceContactDao;
        }
        @Override
        protected Void doInBackground(ConferenceContact... data) {
            conferenceContactDao.delete(data[0]);
            return null;
        }
    }

    public List<ConferenceContact> getConfContacts(String idMedecin) {
        List<ConferenceContact> res = null;
        try {
            res = new GetConfContactdAsync(conferenceContactDao).execute(idMedecin).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetConfContactdAsync extends AsyncTask<String, Void, List<ConferenceContact>> {
        private ConferenceContactDao conferenceContactDao;
        private List<ConferenceContact> res;
        private GetConfContactdAsync(ConferenceContactDao conferenceContactDao) {
            this.conferenceContactDao = conferenceContactDao;
        }
        @Override
        protected List<ConferenceContact> doInBackground(String... data) {
            res = conferenceContactDao.getConferenceContact(data[0]);
            return res;
        }
    }

    public List<ConferenceContact> getConfByName(String nomComplet) {
        List<ConferenceContact> res = null;
        try {
            res = new GetConfByNamedAsync(conferenceContactDao).execute(nomComplet).get();
        } catch (Exception e){
        }
        return res;
    }

    private static class GetConfByNamedAsync extends AsyncTask<String, Void, List<ConferenceContact>> {
        private ConferenceContactDao conferenceContactDao;
        private List<ConferenceContact> res;
        private GetConfByNamedAsync(ConferenceContactDao conferenceContactDao) {
            this.conferenceContactDao = conferenceContactDao;
        }
        @Override
        protected List<ConferenceContact> doInBackground(String... data) {
            res = conferenceContactDao.getConfByName(data[0]);
            return res;
        }
    }

    public LiveData<List<Medecin>> getMedecinList(String data) {
        LiveData<List<Medecin>> questions = null;
        questions = medecinDao.getMedecinList(data);
        return questions;
    }

}
