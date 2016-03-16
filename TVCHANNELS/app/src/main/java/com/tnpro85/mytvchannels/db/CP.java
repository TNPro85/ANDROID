package com.tnpro85.mytvchannels.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tnpro85.mytvchannels.BuildConfig;

import java.util.ArrayList;

public class CP extends ContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    private static final int CODE_DEVICE = 1;
    private static final int CODE_CHANNEL = 2;
    private static final String PATH_DEVICE = "devices";
    private static final String PATH_CHANNEL = "channels";

    public static final Uri CONTENT_URI_DEVICES = Uri.parse("content://" + AUTHORITY + "/" + PATH_DEVICE);
    public static final Uri CONTENT_URI_CHANNELS = Uri.parse("content://" + AUTHORITY + "/" + PATH_CHANNEL);

    private static final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mURIMatcher.addURI(AUTHORITY, PATH_DEVICE, CODE_DEVICE);
        mURIMatcher.addURI(AUTHORITY, PATH_CHANNEL, CODE_CHANNEL);
    }

    @Override
    public boolean onCreate() { return false; }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case CODE_DEVICE:
                queryBuilder.setTables(DBConst.TABLE.TBL_DEVICE_NAME);
                break;
            case CODE_CHANNEL:
                queryBuilder.setTables(DBConst.TABLE.TBL_CHANNEL_NAME);
                break;
        }

        Cursor cursor = queryBuilder.query(DBHelper.getInstance().getDB(), projection, selection, selectionArgs, null, null, sortOrder);
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
        SQLiteDatabase db = DBHelper.getInstance().getDB();
        long id = 0;
        int uriType = mURIMatcher.match(uri);
        switch (uriType) {
            case CODE_DEVICE:
                id = db.insertWithOnConflict(DBConst.TABLE.TBL_DEVICE_NAME, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);
                break;
            case CODE_CHANNEL:
                id = db.insertWithOnConflict(DBConst.TABLE.TBL_CHANNEL_NAME, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(uri + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getInstance().getDB();
        int uriType = mURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType) {
            case CODE_DEVICE:
                rowsDeleted = db.delete(DBConst.TABLE.TBL_DEVICE_NAME, selection, selectionArgs);
                break;
            case CODE_CHANNEL:
                rowsDeleted = db.delete(DBConst.TABLE.TBL_CHANNEL_NAME, selection, selectionArgs);
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DBHelper.getInstance().getDB();
        int uriType = mURIMatcher.match(uri);
        int rowsUpdated = 0;
        switch (uriType) {
            case CODE_DEVICE:
                rowsUpdated = db.update(DBConst.TABLE.TBL_DEVICE_NAME, values, selection, selectionArgs);
                break;
        }

        if(getContext() != null && getContext().getContentResolver() != null)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = DBHelper.getInstance().getDB();
        ContentProviderResult[] result = new ContentProviderResult[operations.size()];
        int i = 0;
        db.beginTransaction();
        try {
            for (ContentProviderOperation operation : operations)
                result[i++] = operation.apply(this, result, i);
            db.setTransactionSuccessful();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return result;
    }
}
