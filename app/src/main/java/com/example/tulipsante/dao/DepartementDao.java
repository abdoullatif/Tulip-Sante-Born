package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Departement;

@Dao
public interface DepartementDao {

   @Insert
   void insert(Departement departement);

   @Query("SELECT * FROM departement WHERE departement.idDepartement LIKE :idDepartement")
    Departement getDepartement(String idDepartement);

}
