package com.example.tulipsante.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tulipsante.models.HistoriquePresence;
import com.example.tulipsante.models.uIModels.HistoriquePresenceMedecin;

import java.util.List;

@Dao
public interface HistoriquePresenceDao {
    @Insert
    void insert(HistoriquePresence historiquePresence);

    @Query("SELECT count(*) FROM historiquePresence WHERE historiquePresence.idMedecin LIKE :idMedecin")
    String numberOfRecord(String idMedecin);

    @Query("SELECT *,nomMedecin,prenomMedecin from historiquePresence inner join medecin on historiquePresence.idMedecin LIKE medecin.idMedecin AND historiquePresence.idMedecin LIKE :idMedecin ORDER BY dateHistorique Desc limit :index,15")
    LiveData<List<HistoriquePresenceMedecin>> getPresenceHistory(String idMedecin,String index);

    @Query("SELECT *,nomMedecin,prenomMedecin from historiquePresence inner join medecin on historiquePresence.idMedecin LIKE medecin.idMedecin AND historiquePresence.idMedecin LIKE :idMedecin ORDER BY dateHistorique DESC limit :index,15")
    List<HistoriquePresenceMedecin> getHistoriquePresenceNewest(String idMedecin, String index);

    @Query("SELECT *,nomMedecin,prenomMedecin from historiquePresence inner join medecin on historiquePresence.idMedecin LIKE medecin.idMedecin AND historiquePresence.idMedecin LIKE :idMedecin ORDER BY dateHistorique ASC limit :index,15")
    List<HistoriquePresenceMedecin> getHistoriquePresenceOldest(String idMedecin,String index);

    @Query("SELECT *,nomMedecin,prenomMedecin from historiquePresence inner join medecin on historiquePresence.idMedecin LIKE medecin.idMedecin AND historiquePresence.idMedecin LIKE :idMedecin AND historiquePresence.dateHistorique LIKE :dateHistorique ORDER BY dateHistorique ASC  ")
    LiveData<List<HistoriquePresenceMedecin>> getHistoriquePresenceSearch(String idMedecin,String dateHistorique);
}
