package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.ContactUrgence;

@Dao
public interface ContactUrgenceDao {

    @Insert
    void insertContactUrgence(ContactUrgence contactUrgence);

    @Query("SELECT * FROM contactUrgence WHERE contactUrgence.idPersonne LIKE :idPersonne")
    ContactUrgence getContactUrgence(String idPersonne);

}
