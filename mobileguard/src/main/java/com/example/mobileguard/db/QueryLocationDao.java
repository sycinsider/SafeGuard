
package com.example.mobileguard.db;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.ContactsContract.Contacts.Data;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;

/**
 * ClassName:QueryLocation <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月12日 下午7:37:17 <br/>
 * 
 * @author dell
 * @version
 */
public class QueryLocationDao {

    public static String getLocation(Context context, String num) {
        String reg = "^1[3|4|5|8][0-9]\\d{8}$";
        String location = "未知";
        File file = new File(context.getFilesDir(), "address.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null,
                SQLiteDatabase.OPEN_READONLY);
        if (num.matches(reg)) {
            String sql = "select city from info where mobileprefix =" + num.substring(0, 7);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    location = cursor.getString(0);
                }
                cursor.close();
            }
        } else {
            switch (num.length()) {
                case 3:
                    location = "警报号码";
                    break;
                case 4:
                case 5:
                case 6:
                    location = "服务号码";
                    break;
                case 11:
                case 12:
                    String temp = num.substring(0, 3);
                    Cursor cursor = db.rawQuery("select city from info where area = ?",
                            new String[] {temp});
                    if (cursor != null) {
                        if (cursor.moveToNext()) {
                            location = cursor.getString(0);
                        } else {
                            String tem = num.substring(0, 4);
                            Cursor cs = db.rawQuery("select city from info where area = ?",
                                    new String[] {tem});
                            if (cs != null) {
                                if (cs.moveToNext()) {
                                    location = cs.getString(0);
                                }
                                cs.close();
                            }
                        }
                        cursor.close();
                    }

                    break;

            }

        }
        db.close();
        return location;
    }
}
