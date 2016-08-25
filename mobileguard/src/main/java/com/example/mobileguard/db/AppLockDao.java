
package com.example.mobileguard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;

import com.example.mobileguard.bean.BlackBean;

import java.util.ArrayList;

/**
 * ClassName:BlackDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:29:18 <br/>
 * 
 * @author dell
 * @version
 */
public class AppLockDao {
    public static boolean add(Context context, String pkg) {
        AppLockDBopenHelper helper = new AppLockDBopenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppLockDB.AppLock_Table.COLUMN_NAME,pkg);
        long insert = db.insert(AppLockDB.AppLock_Table.TABLE_NAME, null, values);
        db.close();
        return insert != -1;
    }

    public static boolean del(Context context, String pkg) {
        AppLockDBopenHelper helper = new AppLockDBopenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = AppLockDB.AppLock_Table.COLUMN_NAME + "= ?";
        String[] args = new String[]{pkg};
        int delete = db.delete(AppLockDB.AppLock_Table.TABLE_NAME, where, args);
        db.close();
        return delete>0;
    }

    public static boolean isLockApp(Context context, String pkg) {
        boolean flag = false;
        AppLockDBopenHelper helper = new AppLockDBopenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String table = AppLockDB.AppLock_Table.TABLE_NAME;
        String where = AppLockDB.AppLock_Table.COLUMN_NAME + "= ?";
        String[] args = new String[]{pkg};
        String[] columns = new String[]{AppLockDB.AppLock_Table.COLUMN_NAME};
        Cursor cursor = db.query(table, columns, where, args, null, null, null);
        if (cursor!=null) {
           if (cursor.moveToFirst()) {
                flag = true;
           }
            cursor.close();
        }
        db.close();
        return flag;
    }

    public static ArrayList<String> getAll(Context context) {
        AppLockDBopenHelper helper = new AppLockDBopenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String table = AppLockDB.AppLock_Table.TABLE_NAME;
        String[] columns = new String[]{AppLockDB.AppLock_Table.COLUMN_NAME};
        Cursor cursor = db.query(table, columns, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<>();
        if (cursor!=null) {
            while(cursor.moveToNext()){
                list.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return list;
    }


}
