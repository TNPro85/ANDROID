package com.tnpro85.mytvchannels.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tnpro85.mytvchannels.BuildConfig;
import com.tnpro85.mytvchannels.models.Device;

public class CP extends ContentProvider {
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    private static final int CODE_DEVICE = 1;
    private static final int CODE_DEVICE_ID = 2;
    private static final int CODE_CHANNEL = 3;
    private static final int CODE_CHANNEL_ID = 4;
    private static final String PATH_DEVICE = "devices";
    private static final String PATH_DEVICE_ID = "devices/#";
    private static final String PATH_CHANNEL = "channels";
    private static final String PATH_CHANNEL_ID = "channels/#";

    public static final Uri CONTENT_URI_DEVICES = Uri.parse("content://" + AUTHORITY + "/" + PATH_DEVICE);
    public static final Uri CONTENT_URI_CHANNELS = Uri.parse("content://" + AUTHORITY + "/" + PATH_CHANNEL);

    private static final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mURIMatcher.addURI(AUTHORITY, PATH_DEVICE, CODE_DEVICE);
        mURIMatcher.addURI(AUTHORITY, PATH_DEVICE_ID, CODE_DEVICE_ID);
        mURIMatcher.addURI(AUTHORITY, PATH_CHANNEL, CODE_CHANNEL);
        mURIMatcher.addURI(AUTHORITY, PATH_CHANNEL_ID, CODE_CHANNEL_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case CODE_DEVICE:
                queryBuilder.setTables(DBConst.TABLE.TBL_DEVICE_NAME);
                break;
            case CODE_DEVICE_ID:
                queryBuilder.appendWhere(DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=" + uri.getLastPathSegment());
                break;
            case CODE_CHANNEL:
                queryBuilder.setTables(DBConst.TABLE.TBL_CHANNEL_NAME);
                break;
            case CODE_CHANNEL_ID:
                queryBuilder.appendWhere(DBConst.TABLE.TBL_CHANNEL_COL.COLUMN_NAME_CNUM + "=" + uri.getLastPathSegment());
                break;
        }

        Cursor cursor = queryBuilder.query(DBHelper.getInstance().getWritableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        long id = 0;
        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case CODE_DEVICE:
                id = db.insertWithOnConflict(DBConst.TABLE.TBL_DEVICE_NAME, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(uri + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        int uriType = mURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType) {
            case CODE_DEVICE:
                rowsDeleted = db.delete(DBConst.TABLE.TBL_DEVICE_NAME, selection, selectionArgs);
                break;
            case CODE_DEVICE_ID:
                String deviceId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(DBConst.TABLE.TBL_DEVICE_NAME, DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=" + deviceId, null);
                } else {
                    rowsDeleted = db.delete(DBConst.TABLE.TBL_DEVICE_NAME, DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=" + deviceId + " and "
                            + selection, selectionArgs);
                }
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        int uriType = mURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case CODE_DEVICE:
                rowsUpdated = db.update(DBConst.TABLE.TBL_DEVICE_NAME, values, selection, selectionArgs);
                break;
            case CODE_DEVICE_ID:
                String deviceId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(DBConst.TABLE.TBL_DEVICE_NAME,
                            values, DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=" + deviceId, null);
                } else {
                    rowsUpdated = db.update(DBConst.TABLE.TBL_DEVICE_NAME,
                            values, DBConst.TABLE.TBL_DEVICE_COL.COLUMN_NAME_NAME + "=" + deviceId + " and "
                            + selection, selectionArgs);
                }
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
