package com.example.tulipsante.utils;

public interface IPersist {
    public static final String FILE_NAME = "persist_data";

    boolean clearData(String paramString);

    boolean clearDatas();

    Object readData(String paramString, Object paramObject);

    boolean writeData(String paramString, Object paramObject);
}
