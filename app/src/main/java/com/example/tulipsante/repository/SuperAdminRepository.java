package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tulipsante.dao.ParametreDao;
import com.example.tulipsante.dao.SuperAdminDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Parametre;
import com.example.tulipsante.models.SuperAdmin;

import java.util.List;

public class SuperAdminRepository {
    private ParametreDao parametreDao;
    private SuperAdminDao superAdminDao;

    private LiveData<List<Parametre>> parametreList;

    public SuperAdminRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        parametreDao = tulipSanteDatabase.parametreDao();
        superAdminDao = tulipSanteDatabase.superAdminDao();

        parametreList = parametreDao.parametreList();
    }

    public String getId(String username) {
        String idMedecin = null;
        try {
            idMedecin = new IdAsyncTask(superAdminDao).execute(username).get();
        } catch (Exception e){
            System.out.println(e);
        }
        return idMedecin;
    }

    public String getPassword(String username)
    {
        String res = null;
        try {
            res = new CheckUserNameAsync(superAdminDao).execute(username).get();
        } catch (Exception e){
            System.out.println(e);
        }
        return res;
    }

    private static class CheckUserNameAsync extends AsyncTask<String, Void, String> {
        private SuperAdminDao superAdminDao;
        private String result;
        private CheckUserNameAsync(SuperAdminDao superAdminDao) {
            this.superAdminDao = superAdminDao;
        }
        @Override
        protected String doInBackground(String... data) {
            result = superAdminDao.getPassword(data[0]);
            return result;
        }
    }

    private static class IdAsyncTask extends AsyncTask<String, Void, String> {
        private SuperAdminDao superAdminDao;
        private String result;
        private IdAsyncTask(SuperAdminDao superAdminDaoo) {
            this.superAdminDao = superAdminDaoo;
        }
        @Override
        protected String doInBackground(String... data) {
            SuperAdmin superAdmin = superAdminDao.getAccount(data[0]);
            if(superAdmin != null) {
                result = superAdmin.getIdSuperAdmin();
            }
            return result;
        }
    }

    // **
    // Insert parametre
    public void insertParametre(Parametre parametre) {
        new InsertParametreAsyncTask(parametreDao).execute(parametre);
    }

    // **
    // Insert parametre Async
    private static class InsertParametreAsyncTask extends AsyncTask<Parametre, Void, Void> {
        private ParametreDao parametreDao;
        private InsertParametreAsyncTask(ParametreDao parametreDao) {
            this.parametreDao = parametreDao;
        }
        @Override
        protected Void doInBackground(Parametre... parametres) {
            parametreDao.insertParametre(parametres[0]);
            return null;
        }
    }

    // **
    // Update parametre
    public void updateParametre(Parametre parametre) {
        new UpdateParametreAsyncTask(parametreDao).execute(parametre);
    }

    // **
    // Update parametre Async
    private static class UpdateParametreAsyncTask extends AsyncTask<Parametre, Void, Void> {
        private ParametreDao parametreDao;
        private UpdateParametreAsyncTask(ParametreDao parametreDao) {
            this.parametreDao = parametreDao;
        }
        @Override
        protected Void doInBackground(Parametre... parametres) {
            parametreDao.updateParametre(parametres[0]);
            return null;
        }
    }

    public LiveData<List<Parametre>> getParametreList() {
        return parametreList;
    }
}
