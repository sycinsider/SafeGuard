
package com.example.mobileguard.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.example.mobileguard.bean.SmsBean;
import com.google.gson.Gson;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;

/**
 * ClassName:BackupUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月19日 上午9:06:23 <br/>
 * 
 * @author dell
 * @version
 */
public class BackupUtils {
    public static void smsBackup(final Context context, final OnBackupListener listener) {
        new AsyncTask<Void, Integer, Exception>() {
            private ContentResolver resolver;
            private ArrayList<SmsBean> datas;

            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onPre();
                }
            }

            @Override
            protected Exception doInBackground(Void... params) {
                FileOutputStream fos = null;
                try {
                    resolver = context.getContentResolver();
                    Uri uri = Uri.parse("content://sms");
                    String[] projection = new String[] {"address", "date", "read", "type", "body"};
                    Cursor cursor = resolver.query(uri, projection, null, null, null);
                    int count = 0;
                    datas = new ArrayList<SmsBean>();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            SmsBean bean = new SmsBean();
                            String address = cursor.getString(0);
                            String date = cursor.getString(1);
                            String read = cursor.getString(2);
                            String type = cursor.getString(3);
                            String body = cursor.getString(4);
                            bean.address = address;
                            bean.date = date;
                            bean.read = read;
                            bean.type = type;
                            bean.body = body;
                            datas.add(bean);
                            publishProgress(++count, cursor.getCount());
                            System.out.println(count);
                            SystemClock.sleep(100);
                        }
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(datas);
                    File file = new File(Environment.getExternalStorageDirectory(), "smsback.xml");
                    fos = new FileOutputStream(file);
                    fos.write(json.getBytes());
                    return null;
                } catch (Exception e) {

                    return e;
                } finally {
                    StreamUtils.close(fos);
                }

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (listener != null) {
                    listener.onUpdate(values);
                }
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (listener != null) {
                    if (result == null) {
                        listener.onSuccess();
                    } else {
                        listener.onFail(result);
                    }
                }
            }
        }.execute();
    }

    public interface OnBackupListener {
        void onPre();

        void onUpdate(Integer... values);

        void onSuccess();

        void onFail(Exception result);
    }
}
