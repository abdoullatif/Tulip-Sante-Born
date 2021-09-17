package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.TypeExamens;

import java.util.List;

@Dao
public interface TypeExamensDao {
    @Insert
    void insertTypeExamens(TypeExamens typeExamens);

    @Query("SELECT * FROM typeExamens ORDER BY description ASC")
    List<TypeExamens> typeExames();
}
