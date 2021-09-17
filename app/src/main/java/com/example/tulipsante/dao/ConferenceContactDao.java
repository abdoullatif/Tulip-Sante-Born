package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.ConferenceContact;

import java.util.List;

@Dao
public interface ConferenceContactDao {
    @Insert
    void insert(ConferenceContact conferenceContact);

    @Delete
    void delete(ConferenceContact conferenceContact);

    @Query("select * from conferenceContact where nomComplet like :nomComplet")
    List<ConferenceContact> getConfByName(String nomComplet);

    @Query("select * from conferenceContact where idMedecin = :idMedecin")
    List<ConferenceContact> getConferenceContact(String idMedecin);
}
