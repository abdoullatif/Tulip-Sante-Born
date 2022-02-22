package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.tulipsante.models.Utilisateur;


@Dao
public interface UtilisateurDao {
    @Insert
    void insert(Utilisateur utilisateur);

    @Query("SELECT password FROM utilisateur WHERE utilisateur.username = :username")
    String getPassword (String username);

    @Query("SELECT * FROM utilisateur WHERE utilisateur.username = :username")
    Utilisateur getAccount(String username);
}
