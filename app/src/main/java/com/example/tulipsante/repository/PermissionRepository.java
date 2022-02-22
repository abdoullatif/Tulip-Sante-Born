package com.example.tulipsante.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.tulipsante.dao.PermissionDao;
import com.example.tulipsante.database.TulipSanteDatabase;
import com.example.tulipsante.models.Permission;
import com.example.tulipsante.models.PermissionXMedecin;
import com.example.tulipsante.utils.GeneralPurposeFunctions;

import java.util.List;

public class PermissionRepository {
    private PermissionDao permissionDao;

    public PermissionRepository(Application application) {
        TulipSanteDatabase tulipSanteDatabase = TulipSanteDatabase.getInstance(application);
        permissionDao = tulipSanteDatabase.permissionDao();
    }

    // **
    // Insert permissions
    public void insertPermission(Permission permission) {
        new InsertPermissionAsyncTask(permissionDao).execute(permission);
    }

    // **
    // Get permissions
    public List<Permission> getPermissions(String idMedecin) {
        List<Permission> permission = null;
        try {
            permission = new GetPermissionAsyncTask(permissionDao).execute(idMedecin).get();
        } catch (Exception ignored){}
        return permission;
    }

    // **
    // Insert Permissions Async
    private static class InsertPermissionAsyncTask extends AsyncTask<Permission, Void, Void> {
        private PermissionDao permissionDao;
        private InsertPermissionAsyncTask(PermissionDao permissionDao) {
            this.permissionDao = permissionDao;
        }
        @Override
        protected Void doInBackground(Permission... permissions) {
            permissionDao.insertPermission(permissions[0]);
            return null;
        }
    }

    // **
    // Get permissions Async
    private static class GetPermissionAsyncTask extends AsyncTask<String, Void, List<Permission>> {
        private PermissionDao permissionDao;
        private GetPermissionAsyncTask(PermissionDao permissionDao) {
            this.permissionDao = permissionDao;
        }
        @Override
        protected List<Permission> doInBackground(String... data) {
            return permissionDao.getPermission(data[0],"public");
        }
    }

    // **
    // Check permission
    public boolean hasPermission(String idMedecin, String idPatient) {
        boolean result = false;
        try {
            result = new CheckPermissionAsyncTask(permissionDao).execute(idPatient, idMedecin).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // **
    // Check permission Async
    private static class CheckPermissionAsyncTask extends AsyncTask<String, Void, Boolean> {
        private PermissionDao permissionDao;
        private Permission permission;
        private boolean isNotValid = true;
        private CheckPermissionAsyncTask(PermissionDao permissionDao) {
            this.permissionDao = permissionDao;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            permission = permissionDao.getPermissionPatient(strings[0],strings[1]);
            if(permission != null) {
                isNotValid = GeneralPurposeFunctions.hasExpired(permission.getDateExpiration());
            }
            return isNotValid;
        }
    }

    // **
    // Get permissions
    public List<PermissionXMedecin> getPermissions(String idMedecin, String idPatient) {
        List<PermissionXMedecin> permission = null;
        try {
            permission = permissionDao.getPerDocPatient(idMedecin, idPatient);
        } catch (Exception ignored){}
        return permission;
    }

    // **
    // Cancel permission
    public void cancelPermission(String date, String type,String param) {
        new CancelPermissionAsyncTask(permissionDao).execute(date,type,param);
    }

    // **
    // Canel permission Async
    private static class CancelPermissionAsyncTask extends AsyncTask<String, Void, Void> {
        private PermissionDao permissionDao;
        private CancelPermissionAsyncTask(PermissionDao permissionDao) {
            this.permissionDao = permissionDao;
        }
        @Override
        protected Void doInBackground(String... strings) {
            permissionDao.updatePermission(strings[0],strings[1],strings[2]);
            return null;
        }
    }

}
