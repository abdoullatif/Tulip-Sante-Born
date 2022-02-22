package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.ReferenceDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Reference;
import com.example.tulipsante.models.uIModels.PatientXRefXDoc;

import java.util.List;

public class ReferenceRepository {
    private ReferenceDao referenceDao;

    public ReferenceRepository(Application application) {
        TulipSanteDatabase database = TulipSanteDatabase.getInstance(application);

        referenceDao = database.referenceDao();
    }

    // **
    // Insert
    public void insertReference(Reference reference) {
        new InsertAsyncTask(referenceDao).execute(reference);
    }

    // **
    // Insert Async
    private static class InsertAsyncTask extends AsyncTask<Reference, Void, Void> {
        private ReferenceDao referenceDao;
        private InsertAsyncTask(ReferenceDao referenceDao) {
            this.referenceDao = referenceDao;
        }
        @Override
        protected Void doInBackground(Reference... references) {
            referenceDao.insertReference(references[0]);
            return null;
        }
    }

    public LiveData<List<PatientXRefXDoc>> patientReferredByOthers(String data) {
        LiveData<List<PatientXRefXDoc>> patientList = null;
        patientList = referenceDao.patientReferredByOthers(data);
        return patientList;
    }

    public LiveData<List<PatientXRefXDoc>> patientReferredByMe(String data) {
        LiveData<List<PatientXRefXDoc>> patientList = null;
        patientList = referenceDao.patientReferredByMe(data);
        return patientList;
    }
}
