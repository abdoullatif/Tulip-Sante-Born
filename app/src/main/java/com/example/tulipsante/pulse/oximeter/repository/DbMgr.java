package com.example.tulipsante.pulse.oximeter.repository;

import android.content.Context;
import android.util.Log;


import com.example.tulipsante.pulse.oximeter.Config;
import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;
import com.spark.oximeter.repository.greendao.DaoMaster;
import com.spark.oximeter.repository.greendao.DaoSession;
import com.spark.oximeter.repository.greendao.SpoRecordDao;

import java.util.List;

public class DbMgr {
    private final String TAG = "DbMgr";
    private final String dbName = "record.db";
    private Context mContext;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SpoRecordDao mSpoRecordDao;

    private static DbMgr mInstance;

    private DbMgr(Context context) {
        mContext = context.getApplicationContext();
        devOpenHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        mSpoRecordDao = daoSession.getSpoRecordDao();
    }

    public static DbMgr getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DbMgr.class) {
                if (mInstance == null) {
                    mInstance = new DbMgr(context);
//                    if (mInstance.getSpoRecords().size() == 0) {
//                        mInstance.initData();
//                    }
                }
            }
        }

        return mInstance;
    }

    public long insertSpoRecord(SpoRecord record) {
        try {
            return mSpoRecordDao.insert(record);
        } catch (Exception e) {
            Log.e(TAG, "insert spoRecord exception = " + e.getMessage());
        }
        return 0;
    }

    public List<SpoRecord> getSpoRecords() {
        return mSpoRecordDao.queryBuilder().orderDesc(SpoRecordDao.Properties.Time).list();
    }

    public void deleteSpoRecord(Long id) {
        try {
            mSpoRecordDao.deleteByKey(id);
        } catch (Exception e) {
            Log.e(TAG, "delete spoRecord exception = " + e.getMessage());
        }
    }

    private void initData() {
        long ms = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            SpoRecord record = new SpoRecord();
            record.setTime(ms + i * 300 * 1000l);
            record.setSpo2(getRandom(Config.cSpo2Min, Config.cSpo2Max));
            record.setPr(getRandom(Config.cPrMin, Config.cPrMax));
            record.setPi((float) getRandom((int) Config.cPiMin, (int) Config.cPiMax));
            insertSpoRecord(record);
        }
    }

    private int getRandom(int min, int max) {
        return min + (int) ((Math.random() * 9 + 1) * (max - min) / 10);
    }
}
