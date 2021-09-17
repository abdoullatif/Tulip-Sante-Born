package com.example.tulipsante.pulse.oximeter.util.persist.impl;

import android.content.Context;

import com.example.tulipsante.pulse.oximeter.util.persist.IPersist;


public class PersistFactory {

    private static IPersist persist;

    public static IPersist getInstance(Context context) {
        if (persist == null) {
            synchronized (PersistFactory.class) {
                if (persist == null) {
                    persist = new SharedPersist(context.getApplicationContext());
                }
            }
        }
        return persist;
    }

    public static IPersist newInstance(Context context) {
        return new SharedPersist(context.getApplicationContext());
    }

}
