package com.tnpro85.mytvchannels.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tnpro.core.db.DBUtils;
import com.tnpro85.mytvchannels.application.MainApp;
import com.tnpro85.mytvchannels.models.Device;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Database instance
    private static volatile DBHelper dbHelper;
    private SQLiteDatabase db;

    public static DBHelper getInstance() {
        synchronized (DBHelper.class) {
            if(dbHelper == null)
                dbHelper = new DBHelper(MainApp.getContext());
        }

        // Check exist and create tables here
        dbHelper.createTableDevices();

        return dbHelper;
    }

    public DBHelper(Context context) {
        super(context, DBConst.DB_NAME, null, DBConst.DB_VERSION);

        // 20150918: Create or open app database
        // For this simple app, I don't have intention to handle data corruption, so I just use default openOrCreateDatabase method
        // to do the DB things.
        if(context != null)
            db = context.openOrCreateDatabase(DBConst.DB_NAME, Context.MODE_PRIVATE, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // [CREATE TABLES]
    private void createTableDevices() {
        if(!DBUtils.isTableExist(db, DBConst.TABLE.TBL_DEVICE_NAME)) {
            db.execSQL(String.format(
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                    DBConst.TABLE.TBL_DEVICE_NAME,
                    DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_ID,
                    DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME,
                    DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_DESC
            ));
        }
    }

    public void addDevice(Device device) {
        if(device != null) {
            ContentValues values = new ContentValues();
            values.put(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME, device.dName);
            values.put(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_DESC, device.dDesc);
            MainApp.getContext().getContentResolver().insert(CP.CONTENT_URI_DEVICES, values);
        }
    }

    public ArrayList<Device> getAllDevices() {
        ArrayList<Device> result = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = MainApp.getContext().getContentResolver().query(CP.CONTENT_URI_DEVICES, null, null, null, null);
            if(cursor != null) {
                while(cursor.moveToNext()) {
                    Device device = new Device();
                    device.dName = cursor.getString(cursor.getColumnIndex(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME));
                    device.dDesc = cursor.getString(cursor.getColumnIndex(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_DESC));
                    result.add(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return result;
    }

//    public ArrayList<Device> getDeviceList() {
//        ArrayList<Device> result = null;
//        Cursor cs = null;
//
//        try {
//            cs = db.rawQuery(DeviceEntry.SQL_QUERY_ALL, null);
//            if(cs != null && cs.getCount() > 0) {
//                result = new ArrayList<>();
//
//                int colId_deviceId = cs.getColumnIndex(DeviceEntry.COLUMN_NAME_ID);
//                int colId_deviceName = cs.getColumnIndex(DeviceEntry.COLUMN_NAME_NAME);
//                int colId_deviceDesc = cs.getColumnIndex(DeviceEntry.COLUMN_NAME_DESC);
//
//                cs.moveToFirst();
//                do {
//                    Device item = new Device();
//                    item.dId = cs.getString(colId_deviceId);
//                    item.dName = cs.getString(colId_deviceName);
//                    item.dDesc = cs.getString(colId_deviceDesc);
//                    result.add(item);
//                }
//                while(cs.moveToNext());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if(cs != null)
//                cs.close();
//        }
//
//        return result;
//    }
}
