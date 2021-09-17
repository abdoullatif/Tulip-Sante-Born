package com.example.tulipsante.pulse.oximeter.repository.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;


import com.example.tulipsante.pulse.oximeter.repository.bean.SpoRecord;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "SPO_RECORD".
*/
public class SpoRecordDao extends AbstractDao<SpoRecord, Long> {

    public static final String TABLENAME = "SPO_RECORD";

    /**
     * Properties of entity SpoRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uid = new Property(1, String.class, "uid", false, "UID");
        public final static Property DeviceId = new Property(2, String.class, "deviceId", false, "DEVICE_ID");
        public final static Property Time = new Property(3, Long.class, "time", false, "TIME");
        public final static Property Spo2 = new Property(4, Integer.class, "spo2", false, "SPO2");
        public final static Property Pr = new Property(5, Integer.class, "pr", false, "PR");
        public final static Property Pi = new Property(6, Float.class, "pi", false, "PI");
    }


    public SpoRecordDao(DaoConfig config) {
        super(config);
    }
    
    public SpoRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SPO_RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"UID\" TEXT," + // 1: uid
                "\"DEVICE_ID\" TEXT," + // 2: deviceId
                "\"TIME\" INTEGER," + // 3: time
                "\"SPO2\" INTEGER," + // 4: spo2
                "\"PR\" INTEGER," + // 5: pr
                "\"PI\" REAL);"); // 6: pi
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SPO_RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SpoRecord entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
 
        String deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindString(3, deviceId);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(4, time);
        }
 
        Integer spo2 = entity.getSpo2();
        if (spo2 != null) {
            stmt.bindLong(5, spo2);
        }
 
        Integer pr = entity.getPr();
        if (pr != null) {
            stmt.bindLong(6, pr);
        }
 
        Float pi = entity.getPi();
        if (pi != null) {
            stmt.bindDouble(7, pi);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SpoRecord entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
 
        String deviceId = entity.getDeviceId();
        if (deviceId != null) {
            stmt.bindString(3, deviceId);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(4, time);
        }
 
        Integer spo2 = entity.getSpo2();
        if (spo2 != null) {
            stmt.bindLong(5, spo2);
        }
 
        Integer pr = entity.getPr();
        if (pr != null) {
            stmt.bindLong(6, pr);
        }
 
        Float pi = entity.getPi();
        if (pi != null) {
            stmt.bindDouble(7, pi);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SpoRecord readEntity(Cursor cursor, int offset) {
        SpoRecord entity = new SpoRecord( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // uid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // deviceId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // time
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // spo2
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // pr
            cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6) // pi
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SpoRecord entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDeviceId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTime(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setSpo2(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setPr(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setPi(cursor.isNull(offset + 6) ? null : cursor.getFloat(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SpoRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SpoRecord entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SpoRecord entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
