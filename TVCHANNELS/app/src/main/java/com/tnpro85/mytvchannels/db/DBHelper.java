package com.tnpro85.mytvchannels.db;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.RemoteException;

import com.tnpro.core.db.DBUtils;
import com.tnpro85.mytvchannels.application.MainApp;
import com.tnpro85.mytvchannels.models.Channel;
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
        dbHelper.createTableChannels();

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

    public SQLiteDatabase getDB() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // [CREATE TABLES]
    private void createTableDevices() {
        if(!DBUtils.isTableExist(db, DBConst.TABLE.TBL_DEVICE_NAME)) {
            db.execSQL(String.format(
                    "CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT)",
                    DBConst.TABLE.TBL_DEVICE_NAME,
                    DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME,
                    DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_DESC
            ));
        }
    }

    private void createTableChannels() {
        if(!DBUtils.isTableExist(db, DBConst.TABLE.TBL_CHANNEL_NAME)) {
            db.execSQL(String.format(
                    "CREATE TABLE %s (%s TEXT, %s INTEGER, %s TEXT, %s TEXT, PRIMARY KEY (%s, %s))",
                    DBConst.TABLE.TBL_CHANNEL_NAME,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNAME,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDESC,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM
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

    public boolean deleteDevice(Device device) {
        boolean result = false;
        if(device != null) {
            try {
                ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                ContentProviderOperation deleteDeviceChannelOp = ContentProviderOperation.newDelete(CP.CONTENT_URI_CHANNELS)
                        .withSelection(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE + "=?",
                                new String[]{device.dName}).build();
                ContentProviderOperation deleteDeviceOp = ContentProviderOperation.newDelete(CP.CONTENT_URI_DEVICES)
                        .withSelection(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=?",
                                new String[]{device.dName}).build();

                ops.add(deleteDeviceChannelOp);
                ops.add(deleteDeviceOp);
                MainApp.getContext().getContentResolver().applyBatch(CP.AUTHORITY, ops);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
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

    public ArrayList<Channel> getAllChannel(Device device) {
        ArrayList<Channel> result = new ArrayList<>();
        Cursor cursor = null;

        if(device != null) {
            try {
                cursor = MainApp.getContext().getContentResolver().query(
                        CP.CONTENT_URI_CHANNELS,
                        null,
                        DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE + "=?",
                        new String[] {device.dName},
                        DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM + " ASC");
                if(cursor != null) {
                    while(cursor.moveToNext()) {
                        Channel channel = new Channel();
                        channel.cDevice = cursor.getString(cursor.getColumnIndex(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE));
                        channel.cNum = cursor.getInt(cursor.getColumnIndex(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM));
                        channel.cName = cursor.getString(cursor.getColumnIndex(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNAME));
                        channel.cDesc = cursor.getString(cursor.getColumnIndex(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDESC));
                        result.add(channel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(cursor != null)
                    cursor.close();
            }
        }

        return result;
    }

    public void addChannel(Channel channel) {
        if(channel != null) {
            ContentValues values = new ContentValues();
            values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE, channel.cDevice);
            values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM, channel.cNum);
            values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNAME, channel.cName);
            values.put(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDESC, channel.cDesc);
            MainApp.getContext().getContentResolver().insert(CP.CONTENT_URI_CHANNELS, values);
        }
    }

    public void deleteAllChannels() {
        MainApp.getContext().getContentResolver().delete(CP.CONTENT_URI_CHANNELS, null, null);
    }

    public void deleteAllChannelsFromDevice(Device device) {
        if(device != null) {
            MainApp.getContext().getContentResolver().delete(CP.CONTENT_URI_CHANNELS,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE + "=?",
                    new String[]{device.dName});
        }
    }

    public void deleteChannel(Channel channel) {
        if(channel != null) {
            MainApp.getContext().getContentResolver().delete(CP.CONTENT_URI_CHANNELS,
                    DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CDEVICE + "=? AND " + DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM + "=?",
                    new String[]{channel.cDevice, channel.cNum + ""});
        }
    }
}
