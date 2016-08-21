
package com.example.mobileguard.db;

import java.util.ArrayList;

import com.example.mobileguard.bean.BlackBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;

/**
 * ClassName:BlackDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:29:18 <br/>
 * 
 * @author dell
 * @version
 */
public class BlackDao {
    public static boolean add(Context context, String number, int type) {
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BlackDB.Black_table.COLUMN_NUMBER, number);
        values.put(BlackDB.Black_table.COLUMN_TYPE, type);
        long insert = db.insert(BlackDB.Black_table.TABLE_NAME, null, values);
        db.close();
        return insert != -1;

    }

    public static boolean del(Context context, String number) {
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String whereClause = BlackDB.Black_table.COLUMN_NUMBER + "=?";
        String[] whereArgs = new String[] {number};
        int delete = db.delete(BlackDB.Black_table.TABLE_NAME, whereClause, whereArgs);
        db.close();
        return delete > 0;
    }

    public static boolean update(Context context, String number, int type) {
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BlackDB.Black_table.COLUMN_TYPE, type);
        String whereClause = BlackDB.Black_table.COLUMN_NUMBER + "=?";
        String[] whereArgs = new String[] {number};
        int update = db.update(BlackDB.Black_table.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        return update > 0;
    }

    public static ArrayList<BlackBean> getAll(Context context) {
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns =
                new String[] {BlackDB.Black_table.COLUMN_NUMBER, BlackDB.Black_table.COLUMN_TYPE};
        Cursor cursor =
                db.query(BlackDB.Black_table.TABLE_NAME, columns, null, null, null, null, null);
        ArrayList<BlackBean> datas = new ArrayList<BlackBean>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                BlackBean bean = new BlackBean();
                String number = cursor.getString(0);
                int type = cursor.getInt(1);
                bean.number = number;
                bean.type = type;
                datas.add(bean);
            }
            cursor.close();
        }
        db.close();
        return datas;

    }

    public static ArrayList<BlackBean> getPageData(Context context, int size, int offset) {
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select " + BlackDB.Black_table.COLUMN_NUMBER + ","
                + BlackDB.Black_table.COLUMN_TYPE + " from " + BlackDB.Black_table.TABLE_NAME
                + " limit " + size + " offset " + offset;
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<BlackBean> datas = new ArrayList<BlackBean>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                BlackBean bean = new BlackBean();
                String number = cursor.getString(0);
                int type = cursor.getInt(1);
                bean.number = number;
                bean.type = type;
                datas.add(bean);
            }
            cursor.close();
        }
        db.close();
        return datas;

    }

    public static int getType(Context context, String number) {
        int type = -1;
        BlackDBopenHelper helper = new BlackDBopenHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = new String[] {BlackDB.Black_table.COLUMN_TYPE};
        String selection = BlackDB.Black_table.COLUMN_NUMBER + "=?";
        String[] selectionArgs = new String[] {number};
        Cursor cursor = db.query(BlackDB.Black_table.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                type = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return type;

    }
}
