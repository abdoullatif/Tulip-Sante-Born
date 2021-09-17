package com.example.tulipsante.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.Relation;
import com.example.tulipsante.models.uIModels.RelationXPatient;

import java.util.List;

@Dao
public interface RelationDao {
    @Insert
    void insertRelation(Relation relation);

    @Query("Delete from relation where idRelation like :idRelation")
    void deleteRelation(String idRelation);

    @Query("select count(*) from relation where idPatient like :idPatient and idRelationPatient like :idRelationPatient")
    String checkRelation(String idPatient, String idRelationPatient);

    @Query("select idRelation,typeRelation, * from relation inner join patient on patient.idPatient like relation.idRelationPatient where relation.idPatient like :idPatient")
    List<RelationXPatient> getRelations(String idPatient);
}
