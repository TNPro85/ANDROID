package com.tnpro85.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tnpro.core.db.DBUtils;
import com.tnpro85.android.application.MainApp;
import com.tnpro85.android.models.Device.DeviceEntry;

/**
 * Created by CPU10819-local on 18/09/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "TvChannels.db";
    private final String DB_TABLE = "tb_version";

    // Database instance
    private static volatile DBHelper dbHelper;
    private SQLiteDatabase db;

    public static DBHelper getInstance() {
        synchronized (DBHelper.class) {
            if(dbHelper == null)
                dbHelper = new DBHelper(MainApp.getContext());
        }

        return dbHelper;
    }

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        // 20150918: Create or open app database
        // For this simple app, I don't have intention to handle data corruption, so I just use default openOrCreateDatabase method
        // to do the DB things.
        if(context != null) {
            db = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        }

        if(db != null && dbHelper != null) {
            // Create table version
            dbHelper.createTableVersion();

            // Create table devices
            dbHelper.createTableDevices();

            // TODO: Create table channels
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // [CREATE TABLES]
    private void createTableVersion() {
        if(!DBUtils.isTableExist(db, DB_TABLE)) {
        }
    }

    private void createTableDevices() {
        if(!DBUtils.isTableExist(db, DeviceEntry.TABLE_NAME)) {
            db.execSQL(DeviceEntry.SQL_CREATE_TABLE);
        }
    }
}
