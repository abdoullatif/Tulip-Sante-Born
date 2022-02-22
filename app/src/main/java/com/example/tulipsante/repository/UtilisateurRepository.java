package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.tulipsante.dao.UtilisateurDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Utilisateur;

public class UtilisateurRepository {
    private UtilisateurDao utilisateurDao;

    public UtilisateurRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        utilisateurDao = tulipSanteDatabase.utilisateurDao();
    }

    public String getIdMedecin(String username) {
        String idMedecin = null;
        try {
            idMedecin = new IdMedecinAsyncTask(utilisateurDao).execute(username).get();
        } catch (Exception e){
            System.out.println(e);
        }
        return idMedecin;
    }

    public String getPassword(String username)
    {
        String res = null;
        try {
            res = new CheckUserNameAsync(utilisateurDao).execute(username).get();
        } catch (Exception e){
            System.out.println(e);
        }
        return res;
    }

    private static class CheckUserNameAsync extends AsyncTask<String, Void, String> {
        private UtilisateurDao utilisateurDao;
        private String result;
        private CheckUserNameAsync(UtilisateurDao utilisateurDao) {
            this.utilisateurDao = utilisateurDao;
        }
        @Override
        protected String doInBackground(String... data) {
            result = utilisateurDao.getPassword(data[0]);
            return result;
        }
    }

    private static class IdMedecinAsyncTask extends AsyncTask<String, Void, String> {
        private UtilisateurDao utilisateurDao;
        private String result;
        private IdMedecinAsyncTask(UtilisateurDao utilisateurDao) {
            this.utilisateurDao = utilisateurDao;
        }
        @Override
        protected String doInBackground(String... data) {
            Utilisateur utilisateur = utilisateurDao.getAccount(data[0]);
            if(utilisateur != null) {
                result = utilisateur.getIdMedecin();
            }
            return result;
        }
    }

}
