package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Permission;
import com.example.tulipsante.models.PermissionXMedecin;

import java.util.List;

@Dao
public interface PermissionDao {

    @Insert
    void insertPermission(Permission permission);

    @Query("update permission set dateExpiration = :dateExpiration, type = :type where idPermission like :idPermission")
    void updatePermission(String dateExpiration, String type, String idPermission);

    @Query("SELECT * FROM permission WHERE permission.idPatient LIKE :idPatient AND permission.idMedecin LIKE :idMedecin order by dateExpiration desc")
    Permission getPermissionPatient(String idPatient, String idMedecin);

    @Query("SELECT * FROM permission WHERE permission.idMedecin LIKE :idMedecin AND permission.type LIKE :typePer order by dateExpiration desc")
    List<Permission> getPermission(String idMedecin,String typePer);

    @Query("SELECT medecin.nomMedecin, medecin.prenomMedecin,* FROM permission inner join medecin on permission.idMedecin like medecin.idMedecin WHERE permission.idMedecin LIKE :idMedecin AND permission.idPatient LIKE :idPatient order by dateExpiration desc")
    List<PermissionXMedecin> getPerDocPatient(String idMedecin, String idPatient);

}
