package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Addresse;

@Dao
public interface AddresseDao {
    @Insert
    void insert(Addresse addresse);

    @Query("SELECT * FROM addresse WHERE addresse.idAddresse LIKE :idAddresse")
    Addresse getAddresse(String idAddresse);
}
