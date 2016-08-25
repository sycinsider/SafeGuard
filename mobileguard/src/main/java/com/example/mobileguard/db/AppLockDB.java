
package com.example.mobileguard.db;

/**
 * ClassName:BlackDB <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:17:39 <br/>
 * 
 * @author dell
 * @version
 */
interface AppLockDB {
    String NAME = "applock.db";
    int VERSION = 1;

    interface AppLock_Table {
        String TABLE_NAME = "applock";
        String COLUMN_ID = "_id";
        String COLUMN_NAME = "pkgName";
        String SQL =
                "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement,"
                        + COLUMN_NAME + " varchar)";
    }
}
