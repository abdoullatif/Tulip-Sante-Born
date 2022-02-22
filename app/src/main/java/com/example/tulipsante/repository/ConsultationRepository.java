package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.CategorieSigneVitauxDao;
import com.example.tulipsante.dao.ConseilDao;
import com.example.tulipsante.dao.ConsultationDao;
import com.example.tulipsante.dao.DiagnosticDao;
import com.example.tulipsante.dao.ExamensDao;
import com.example.tulipsante.dao.MaladieDao;
import com.example.tulipsante.dao.PrescriptionDao;
import com.example.tulipsante.dao.SignesVitauxDao;
import com.example.tulipsante.dao.SymptomeDao;
import com.example.tulipsante.dao.SymptomesPatientDao;
import com.example.tulipsante.dao.TypeExamensDao;
import com.example.tulipsante.dao.TypeVaccinationDao;
import com.example.tulipsante.dao.VaccinationPatientDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.CategorieSigneVitaux;
import com.example.tulipsante.models.Conseil;
import com.example.tulipsante.models.Consultation;
import com.example.tulipsante.models.Diagnostic;
import com.example.tulipsante.models.Examen;
import com.example.tulipsante.models.Maladie;
import com.example.tulipsante.models.Prescription;
import com.example.tulipsante.models.SignesVitaux;
import com.example.tulipsante.models.Symptome;
import com.example.tulipsante.models.SymptomesPatient;
import com.example.tulipsante.models.TypeExamens;
import com.example.tulipsante.models.TypeVaccination;
import com.example.tulipsante.models.uIModels.DashProfileData;
import com.example.tulipsante.models.uIModels.ExamenXTypeExamen;
import com.example.tulipsante.models.uIModels.SignesVitauxXCatSignesVitaux;
import com.example.tulipsante.models.uIModels.VaccinationData;
import com.example.tulipsante.models.VaccinationPatient;

import java.util.List;

public class ConsultationRepository {
    private ConsultationDao consultationDao;
    private CategorieSigneVitauxDao categorieSigneVitauxDao;
    private SymptomeDao symptomeDao;
    private SymptomesPatientDao symptomesPatientDao;
    private DiagnosticDao diagnosticDao;
    private MaladieDao maladieDao;

    private SignesVitauxDao signesVitauxDao;
    private ExamensDao examensDao;
    private TypeExamensDao typeExamensDao;
    private PrescriptionDao prescriptionDao;
    private TypeVaccinationDao typeVaccinationDao;
    private VaccinationPatientDao vaccinationPatientDao;

    private ConseilDao conseilDao;

    public ConsultationRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        consultationDao = tulipSanteDatabase.consultationDao();
        symptomeDao = tulipSanteDatabase.symptomeDao();
        categorieSigneVitauxDao = tulipSanteDatabase.categorieSigneVitauxDao();
        signesVitauxDao = tulipSanteDatabase.signesVitauxDao();
        symptomesPatientDao = tulipSanteDatabase.symptomesPatientDao();
        diagnosticDao = tulipSanteDatabase.diagnosticDao();
        maladieDao = tulipSanteDatabase.maladieDao();
        examensDao = tulipSanteDatabase.examensDao();
        typeExamensDao = tulipSanteDatabase.typeExamensDao();
        prescriptionDao = tulipSanteDatabase.prescriptionDao();
        conseilDao = tulipSanteDatabase.conseilDao();
        typeVaccinationDao = tulipSanteDatabase.typeVaccinationDao();
        vaccinationPatientDao = tulipSanteDatabase.vaccinationPatientDao();
    }

    // ***
    // Insert Consultation
    public void insertConsultation(Consultation consultation){
        new InsertConsultationAsyncTask(consultationDao).execute(consultation);
    }

    // ***
    // Inset Consultation Async
    private static class InsertConsultationAsyncTask extends AsyncTask<Consultation, Void, Void> {
        private ConsultationDao consultationDao;
        private InsertConsultationAsyncTask(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected Void doInBackground(Consultation... consultations) {
            consultationDao.insertNewConsultation(consultations[0]);
            return null;
        }
    }

    // ***
    // Check Whether Consultation Record exist
    public List<Consultation> getConsultationRecord(String idPatient) {
        List<Consultation> consultations = null;
        try {
            consultations = new GetConsultationRecord(consultationDao).execute(idPatient).get();
        } catch (Exception ignored){}
        return consultations;
    }

    // ***
    // Get Consultation Record Async
    private static class GetConsultationRecord extends AsyncTask<String, Void, List<Consultation>> {
        private ConsultationDao consultationDao;
        private GetConsultationRecord(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected List<Consultation> doInBackground(String... data) {
            return consultationDao.getConsultationRecord(data[0]);
        }
    }

    // ***
    // Get Consultation Record
    public boolean consultationRecordExist(String idConsultation) {
        boolean exist = false;
        List<Consultation> consultations;
        try {
            consultations = new GetConsultationWithIdAsyncTask(consultationDao).execute(idConsultation).get();
            if (!consultations.isEmpty()) {
                exist = true;
            }
        } catch (Exception ignored){}
        return exist;
    }

    // ***
    // Get Consultation Record Async
    private static class GetConsultationWithIdAsyncTask extends AsyncTask<String, Void, List<Consultation>> {
        private ConsultationDao consultationDao;
        private GetConsultationWithIdAsyncTask(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected List<Consultation> doInBackground(String... data) {
            return consultationDao.getConsultationWithId(data[0]);
        }
    }

    // ***
    // Get ALL Symptomes Record
    public LiveData<List<Symptome>> getAllSymptomes() {
        return symptomeDao.getAllSymptomes();
    }

    // ***
    // Get ALL signes vitaux Record
    public List<CategorieSigneVitaux> getSignesVitaux() {
        List<CategorieSigneVitaux> categorieSigneVitauxes = null;
        try {
            categorieSigneVitauxes = new GetSigneVitauxAsyncTask(categorieSigneVitauxDao).execute().get();
        } catch (Exception ignored){}
        return categorieSigneVitauxes;
    }

    // ***
    // Get ALL SV Record Async
    private static class GetSigneVitauxAsyncTask extends AsyncTask<String, Void, List<CategorieSigneVitaux>> {
        private CategorieSigneVitauxDao categorieSigneVitauxDao;
        private GetSigneVitauxAsyncTask(CategorieSigneVitauxDao categorieSigneVitauxDao) {
            this.categorieSigneVitauxDao = categorieSigneVitauxDao;
        }
        @Override
        protected List<CategorieSigneVitaux> doInBackground(String... data) {
            return categorieSigneVitauxDao.catSignesVitaux();
        }
    }

    // ***
    // Insert Signes Vitaux
    public void insertSignesVitaux(SignesVitaux signesVitaux){
        new InsertSignesVitauxAsyncTask(signesVitauxDao).execute(signesVitaux);
    }

    // ***
    // Inset Signes Vitaux Async
    private static class InsertSignesVitauxAsyncTask extends AsyncTask<SignesVitaux, Void, Void> {
        private SignesVitauxDao signesVitauxDao;
        private InsertSignesVitauxAsyncTask(SignesVitauxDao signesVitauxDao) {
            this.signesVitauxDao = signesVitauxDao;
        }
        @Override
        protected Void doInBackground(SignesVitaux... signesVitauxes) {
            signesVitauxDao.insertSignesVitaux(signesVitauxes[0]);
            return null;
        }
    }

    // **
    // Insert Symptomes Patient
    public void insertSymptomesPatient(SymptomesPatient symptomesPatient){
        new InsertSymptomesPatientAsyncTask(symptomesPatientDao).execute(symptomesPatient);
    }

    // ***
    // Inset Symptomes Patient Async
    private static class InsertSymptomesPatientAsyncTask extends AsyncTask<SymptomesPatient, Void, Void> {
        private SymptomesPatientDao symptomesPatientDao;
        private InsertSymptomesPatientAsyncTask(SymptomesPatientDao symptomesPatientDao) {
            this.symptomesPatientDao = symptomesPatientDao;
        }
        @Override
        protected Void doInBackground(SymptomesPatient... symptomesPatients) {
            symptomesPatientDao.insertSymptomePatient(symptomesPatients[0]);
            return null;
        }
    }

    // ***
    // Insert Diagnostic patient
    public void insertDiagnosticPatient(Diagnostic diagnostic){
        new InsertDiagnosticPatientAsync(diagnosticDao).execute(diagnostic);
    }

    // ***
    // Inset Diagnostic Patient Async
    private static class InsertDiagnosticPatientAsync extends AsyncTask<Diagnostic, Void, Void> {
        private DiagnosticDao diagnosticDao;
        private InsertDiagnosticPatientAsync(DiagnosticDao diagnosticDao) {
            this.diagnosticDao = diagnosticDao;
        }
        @Override
        protected Void doInBackground(Diagnostic... diagnostics) {
            diagnosticDao.insertDiagnostic(diagnostics[0]);
            return null;
        }
    }

    // todo newly added

    // ***
    // Insert Vaccination patient
    public void insertVaccinationPatient(VaccinationPatient vaccinationPatient){
        new InsertVaccinationPatientAsync(vaccinationPatientDao).execute(vaccinationPatient);
    }

    // ***
    // Inset Vaccination Patient Async
    private static class InsertVaccinationPatientAsync extends AsyncTask<VaccinationPatient, Void, Void> {
        private VaccinationPatientDao vaccinationPatientDao;
        private InsertVaccinationPatientAsync(VaccinationPatientDao vaccinationPatientDao) {
            this.vaccinationPatientDao = vaccinationPatientDao;
        }
        @Override
        protected Void doInBackground(VaccinationPatient... vaccinationPatients) {
            vaccinationPatientDao.insertVaccinationPatient(vaccinationPatients[0]);
            return null;
        }
    }

    // ***
    // Get ALL signes vitaux Record
    public List<Maladie> getMaladies() {
        List<Maladie> maladies = null;
        try {
            maladies = new GetMaladiesAsyncTask(maladieDao).execute().get();
        } catch (Exception ignored){}
        return maladies;
    }

    // ***
    // Get ALL SV Record Async
    private static class GetMaladiesAsyncTask extends AsyncTask<String, Void, List<Maladie>> {
        private MaladieDao maladieDao;
        private GetMaladiesAsyncTask(MaladieDao maladieDao) {
            this.maladieDao = maladieDao;
        }
        @Override
        protected List<Maladie> doInBackground(String... data) {
            return maladieDao.getMaladies();
        }
    }

    // ***
    // Get ALL signes vitaux Record
    public List<TypeVaccination> getVaccines() {
        List<TypeVaccination> typeVaccinations = null;
        try {
            typeVaccinations = new GetTypeVaccinationAsyncTask(typeVaccinationDao).execute().get();
        } catch (Exception ignored){}
        return typeVaccinations;
    }

    // ***
    // Get ALL SV Record Async
    private static class GetTypeVaccinationAsyncTask extends AsyncTask<String, Void, List<TypeVaccination>> {
        private TypeVaccinationDao typeVaccinationDao;
        private GetTypeVaccinationAsyncTask(TypeVaccinationDao typeVaccinationDao) {
            this.typeVaccinationDao = typeVaccinationDao;
        }
        @Override
        protected List<TypeVaccination> doInBackground(String... data) {
            return typeVaccinationDao.typeVaccinationList();
        }
    }

    // ***
    // Insert Examens patient
    public void insertExamens(Examen examen){
        new InsertExamensPatientAsync(examensDao).execute(examen);
    }

    // ***
    // Inset Diagnostic Patient Async
    private static class InsertExamensPatientAsync extends AsyncTask<Examen, Void, Void> {
        private ExamensDao examensDao;
        private InsertExamensPatientAsync(ExamensDao examensDao) {
            this.examensDao = examensDao;
        }
        @Override
        protected Void doInBackground(Examen... examens) {
            examensDao.insertExamen(examens[0]);
            return null;
        }
    }

    // ***
    // Get all exams Record
    public List<TypeExamens> typeExamens() {
        List<TypeExamens> typeExamens = null;
        try {
            typeExamens = new GetTypeExamensAsyncTask(typeExamensDao).execute().get();
        } catch (Exception ignored){}
        return typeExamens;
    }

    // ***
    // Get all exams Async
    private static class GetTypeExamensAsyncTask extends AsyncTask<String, Void, List<TypeExamens>> {
        private TypeExamensDao typeExamensDao;
        private GetTypeExamensAsyncTask(TypeExamensDao typeExamensDao) {
            this.typeExamensDao = typeExamensDao;
        }
        @Override
        protected List<TypeExamens> doInBackground(String... data) {
            return typeExamensDao.typeExames();
        }
    }

    // ***
    // Insert Prescription patient
    public void insertPrescriptionPatient(Prescription prescription){
        new InsertPrescriptionPatientAsync(prescriptionDao).execute(prescription);
    }

    // ***
    // Inset Prescription Patient Async
    private static class InsertPrescriptionPatientAsync extends AsyncTask<Prescription, Void, Void> {
        private PrescriptionDao prescriptionDao;
        private InsertPrescriptionPatientAsync(PrescriptionDao prescriptionDao) {
            this.prescriptionDao = prescriptionDao;
        }
        @Override
        protected Void doInBackground(Prescription... prescriptions) {
            prescriptionDao.insertPrescription(prescriptions[0]);
            return null;
        }
    }

    // ***
    // Insert Conseil patient
    public void insertConseilPatient(Conseil conseil){
        new InsertConseilPatientAsync(conseilDao).execute(conseil);
    }

    // ***
    // Inset Conseil Patient Async
    private static class InsertConseilPatientAsync extends AsyncTask<Conseil, Void, Void> {
        private ConseilDao conseilDao;
        private InsertConseilPatientAsync(ConseilDao conseilDao) {
            this.conseilDao = conseilDao;
        }
        @Override
        protected Void doInBackground(Conseil... conseils) {
            conseilDao.insertConseil(conseils[0]);
            return null;
        }
    }

    // ***
    // Get consultation graph
    public List<DashProfileData> getConsultationGraph(String idMedecin,String period) {
        List<DashProfileData> consultations = null;
        try {
            consultations = consultationDao.getConsultationGraph(idMedecin,period);;
        } catch (Exception ignored){}
        return consultations;
    }


    // ***
    // Get consultation symptom
    public List<String> getConsultationSymptom(String idConsultation) {
        List<String> stringList = null;
        try {
            stringList = new GetConsultationSymptomAsync(symptomesPatientDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return stringList;
    }

    // ***
    // Get Consultation symptom  Async
    private static class GetConsultationSymptomAsync extends AsyncTask<String, Void, List<String>> {
        private SymptomesPatientDao symptomesPatientDao;
        private GetConsultationSymptomAsync(SymptomesPatientDao symptomesPatientDao) {
            this.symptomesPatientDao = symptomesPatientDao;
        }
        @Override
        protected List<String> doInBackground(String... data) {
            return symptomesPatientDao.symptomDescription(data[0]);
        }
    }

    // ***
    // Get consultation diagnostic
    public List<String> getConsultationDiagnostic(String idConsultation) {
        List<String> stringList = null;
        try {
            stringList = new GetConsultationDiagnosticAsync(diagnosticDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return stringList;
    }

    // ***
    // Get Consultation diagnostic  Async
    private static class GetConsultationDiagnosticAsync extends AsyncTask<String, Void, List<String>> {
        private DiagnosticDao diagnosticDao;
        private GetConsultationDiagnosticAsync(DiagnosticDao diagnosticDao) {
            this.diagnosticDao = diagnosticDao;
        }
        @Override
        protected List<String> doInBackground(String... data) {
            return diagnosticDao.diagnosticDescription(data[0]);
        }
    }

    // ***
    // Get consultation prescription
    public List<Prescription> getConsultationPrescription(String idConsultation) {
        List<Prescription> prescriptionList = null;
        try {
            prescriptionList = new GetConsultationPrescriptionAsync(prescriptionDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return prescriptionList;
    }

    // ***
    // Get Consultation prescription  Async
    private static class GetConsultationPrescriptionAsync extends AsyncTask<String, Void, List<Prescription>> {
        private PrescriptionDao prescriptionDao;
        private GetConsultationPrescriptionAsync(PrescriptionDao prescriptionDao) {
            this.prescriptionDao = prescriptionDao;
        }
        @Override
        protected List<Prescription> doInBackground(String... data) {
            return prescriptionDao.prescription(data[0]);
        }
    }
    // ***
    // Get consultation conseil
    public List<Conseil> getConsultationConseil(String idConsultation) {
        List<Conseil> conseilList = null;
        try {
            conseilList = new GetConsultationConseilAsync(conseilDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return conseilList;
    }

    // ***
    // Get Consultation conseil  Async
    private static class GetConsultationConseilAsync extends AsyncTask<String, Void, List<Conseil>> {
        private ConseilDao conseilDao;
        private GetConsultationConseilAsync(ConseilDao conseilDao) {
            this.conseilDao = conseilDao;
        }
        @Override
        protected List<Conseil> doInBackground(String... data) {
            return conseilDao.conseil(data[0]);
        }
    }

    // ***
    // Get consultation vaccination
    public List<VaccinationData> getConsultationVaccination(String idConsultation) {
        List<VaccinationData> vaccinationList = null;
        try {
            vaccinationList = new GetConsultationVaccinationAsync(vaccinationPatientDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return vaccinationList;
    }

    // ***
    // Get Consultation conseil  Async
    private static class GetConsultationVaccinationAsync extends AsyncTask<String, Void, List<VaccinationData>> {
        private VaccinationPatientDao vaccinationPatientDao;
        private GetConsultationVaccinationAsync(VaccinationPatientDao vaccinationPatientDao) {
            this.vaccinationPatientDao = vaccinationPatientDao;
        }
        @Override
        protected List<VaccinationData> doInBackground(String... data) {
            return vaccinationPatientDao.vaccineType(data[0]);
        }
    }

    // ***
    // Get consultation investigation
    public List<ExamenXTypeExamen> getConsultationInvestigation(String idConsultation) {
        List<ExamenXTypeExamen> e = null;
        try {
            e = new GetConsultationInvestigationAsync(examensDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return e;
    }

    // ***
    // Get Consultation investigation async
    private static class GetConsultationInvestigationAsync extends AsyncTask<String, Void, List<ExamenXTypeExamen>> {
        private ExamensDao examensDao;
        private GetConsultationInvestigationAsync(ExamensDao examensDao) {
            this.examensDao = examensDao;
        }
        @Override
        protected List<ExamenXTypeExamen> doInBackground(String... data) {
            return examensDao.investigationDescription(data[0]);
        }
    }
    // ***
    // Get consultation vitals
    public List<SignesVitauxXCatSignesVitaux> getConsultationVitals(String idConsultation) {
        List<SignesVitauxXCatSignesVitaux> s = null;
        try {
            s = new GetConsultationVitalsAsync(signesVitauxDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return s;
    }

    // ***
    // Get Consultation vitals async
    private static class GetConsultationVitalsAsync extends AsyncTask<String, Void, List<SignesVitauxXCatSignesVitaux>> {
        private SignesVitauxDao signesVitauxDao;
        private GetConsultationVitalsAsync(SignesVitauxDao signesVitauxDao) {
            this.signesVitauxDao = signesVitauxDao;
        }
        @Override
        protected List<SignesVitauxXCatSignesVitaux> doInBackground(String... data) {
            return signesVitauxDao.vitalsDescription(data[0]);
        }
    }

    // ***
    // Check Whether Consultation Record exist
    public String consultationExist(String idConsultation) {
        String result = null;
        try {
            result = new GetConsulationSizeAsyncTask(consultationDao).execute(idConsultation).get();
        } catch (Exception ignored){}
        return result;
    }

    // ***
    private static class GetConsulationSizeAsyncTask extends AsyncTask<String, Void, String> {
        private ConsultationDao consultationDao;
        private GetConsulationSizeAsyncTask(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected String doInBackground(String... data) {
            return consultationDao.consultationExist(data[0]);
        }
    }
    // ***
    // Check Whether Consultation Record exist
    public void deleteConsultation(String idConsultation) {
        new DeleteConsultationAsyncTask(consultationDao).execute(idConsultation);
    }

    // ***
    private static class DeleteConsultationAsyncTask extends AsyncTask<String, Void, Void> {
        private ConsultationDao consultationDao;
        private DeleteConsultationAsyncTask(ConsultationDao consultationDao) {
            this.consultationDao = consultationDao;
        }
        @Override
        protected Void doInBackground(String... data) {
            consultationDao.deleteConsultation(data[0]);
            return null;
        }
    }
}
