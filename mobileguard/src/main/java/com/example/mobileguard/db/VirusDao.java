package com.example.mobileguard.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.StaticLayout;

import java.io.File;

/**
 * Created by dell on 2016/8/25.
 */
public class VirusDao {
    public static boolean isVirus(Context context, String md5) {
        boolean flag = false;
        File file = new File(context.getFilesDir(), "antivirus.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null,
                SQLiteDatabase.OPEN_READONLY);
        String sql = "select md5 from datable where md5 = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{md5});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                flag = true;
            }
            cursor.close();
        }
        db.close();
        return flag;
    }

}
