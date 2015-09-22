package com.tnpro.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils {
    public static boolean isTableExist(SQLiteDatabase db, String tableName) {
        boolean exist = true;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM SQLITE_MASTER WHERE NAME = '" + tableName + "'", null);
            if (cursor != null && cursor.getCount() == 0) exist = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }

        return exist;
    }
}
