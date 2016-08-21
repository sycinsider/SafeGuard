
package com.example.mobileguard.db;

import java.io.File;
import java.util.ArrayList;

import com.example.mobileguard.bean.ComNumChildBean;
import com.example.mobileguard.bean.ComNumGroupBean;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;

/**
 * ClassName:ComNumDBDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月14日 下午7:35:59 <br/>
 * 
 * @author dell
 * @version
 */
public class ComNumDBDao {
    private static ArrayList<ComNumGroupBean> datas = new ArrayList<ComNumGroupBean>();

    public static ArrayList<ComNumGroupBean> getData(Context context) {
        File file = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null,
                SQLiteDatabase.OPEN_READONLY);
        String group = "select name,idx from classlist";
        Cursor groupCursor = db.rawQuery(group, null);
        if (groupCursor != null) {
            while (groupCursor.moveToNext()) {
                ComNumGroupBean groupBean = new ComNumGroupBean();
                String groupName = groupCursor.getString(0);
                int idx = groupCursor.getInt(1);
                groupBean.groupName = groupName;
                String child = "select name,number from table" + idx;
                Cursor childCursor = db.rawQuery(child, null);
                ArrayList<ComNumChildBean> childrens = new ArrayList<ComNumChildBean>();
                if (childCursor != null) {
                    while (childCursor.moveToNext()) {
                        ComNumChildBean childBean = new ComNumChildBean();
                        childBean.childName = childCursor.getString(0);
                        childBean.num = childCursor.getString(1);
                        childrens.add(childBean);
                    }
                    childCursor.close();
                }
                groupBean.childrens = childrens;
                datas.add(groupBean);
            }
            groupCursor.close();
        }
        db.close();
        return datas;
    }
}
