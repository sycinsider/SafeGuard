
package com.example.mobileguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ClassName:BlackDBopenHELPER <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:15:29 <br/>
 * 
 * @author dell
 * @version
 */
public class AppLockDBopenHelper extends SQLiteOpenHelper {

    public AppLockDBopenHelper(Context context) {
        super(context, AppLockDB.NAME, null, AppLockDB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppLockDB.AppLock_Table.SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
