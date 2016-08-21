
package com.example.mobileguard.db;

/**
 * ClassName:BlackDB <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 下午5:17:39 <br/>
 * 
 * @author dell
 * @version
 */
interface BlackDB {
    String NAME = "black.db";
    int VERSION = 1;

    public interface Black_table {
        String TABLE_NAME = "black";
        String COLUMN_ID = "_id";
        String COLUMN_NUMBER = "number";
        String COLUMN_TYPE = "type";
        String SQL =
                "create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement,"
                        + COLUMN_NUMBER + " varchar unique," + COLUMN_TYPE + " varchar)";
    }
}
