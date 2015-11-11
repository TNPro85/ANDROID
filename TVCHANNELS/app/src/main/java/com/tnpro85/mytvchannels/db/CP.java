package com.tnpro85.mytvchannels.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class CP extends ContentProvider {
    private static final String AUTHORITY = "com.tnpro.tvchannels";

    private static final int CODE_DEVICE = 1;
    private static final int CODE_DEVICE_ID = 2;
    private static final String PATH_DEVICE = "devices";
    private static final String PATH_DEVICE_ID = "devices/#";

    private static final Uri CONTENT_URI_DEVICES = Uri.parse("content://" + AUTHORITY + "/" + PATH_DEVICE);

    private static final UriMatcher mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mURIMatcher.addURI(AUTHORITY, PATH_DEVICE, CODE_DEVICE);
        mURIMatcher.addURI(AUTHORITY, PATH_DEVICE_ID, CODE_DEVICE_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
