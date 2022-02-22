package com.example.tulipsante.models.uIModels;

import com.google.gson.annotations.SerializedName;

public class SliderListModel {
    @SerializedName("id")
    public int id;

    @SerializedName("image_url")
    public String image_url;


    public SliderListModel(int id, String image_url) {
        this.id = id;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }
}
