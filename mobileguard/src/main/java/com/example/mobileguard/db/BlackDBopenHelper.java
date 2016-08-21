
package com.example.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ClassName:BlackDBopenHELPER <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:15:29 <br/>
 * 
 * @author dell
 * @version
 */
public class BlackDBopenHelper extends SQLiteOpenHelper {

    public BlackDBopenHelper(Context context) {
        super(context, BlackDB.NAME, null, BlackDB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BlackDB.Black_table.SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
