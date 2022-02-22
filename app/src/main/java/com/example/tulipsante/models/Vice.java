package com.example.tulipsante.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vice")
public class Vice {
    @NonNull
    @PrimaryKey
    private String idVice;
    private String description;
    private String flagTransmis;

    public Vice(@NonNull String idVice, String description, String flagTransmis) {
        this.idVice = idVice;
        this.description = description;
        this.flagTransmis = flagTransmis;
    }

    @NonNull
    public String getIdVice() {
        return idVice;
    }

    public void setIdVice(@NonNull String idVice) {
        this.idVice = idVice;
    }

    public String getDescription() {
        return description;
    }

    public String getFlagTransmis() {
        return flagTransmis;
    }
}
