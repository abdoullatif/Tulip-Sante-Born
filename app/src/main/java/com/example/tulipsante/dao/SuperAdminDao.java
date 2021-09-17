package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.SuperAdmin;

@Dao
public interface SuperAdminDao {
    @Insert
    void insert(SuperAdmin superAdmin);

    @Query("SELECT password FROM superAdmin WHERE superAdmin.username LIKE :username")
    String getPassword (String username);

    @Query("SELECT * FROM superAdmin WHERE superAdmin.username LIKE :username")
    SuperAdmin getAccount(String username);
}
