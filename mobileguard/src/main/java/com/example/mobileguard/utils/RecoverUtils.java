package com.example.mobileguard.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;

import com.example.mobileguard.bean.SmsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by dell on 2016/8/21.
 */
public class RecoverUtils {

    public static void smsRecover(final Context context,final OnSmsRecoverListner listener) {
        new AsyncTask<Void, Integer, Exception>() {

            private ArrayList<SmsBean> list;
            private ContentResolver cr;

            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onRPre();
                }
            }

            @Override
            protected Exception doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    File file = new File(Environment.getExternalStorageDirectory(), "smsback.xml");
                    bufferedReader = new BufferedReader(new FileReader(file));
                    String json = bufferedReader.readLine();
                    Gson gson = new Gson();
                    list = gson.fromJson(json, new TypeToken<ArrayList<SmsBean>>(){}.getType());
                    cr = context.getContentResolver();
                    Uri uri = Uri.parse("content://sms");
                    int progress =0;
                    for (int i = 0; i < list.size(); i++) {
                        SystemClock.sleep(500);
                        SmsBean bean = list.get(i);
                        ContentValues values = new ContentValues();
                        values.put("address", bean.address);
                        values.put("type", bean.type);
                        values.put("date", bean.date);
                        values.put("body", bean.body);
                        values.put("read", bean.read);
                        cr.insert(uri, values);
                        publishProgress(++progress, list.size());

                    }
                    return null;
                } catch (Exception e) {
                    return e;
                } finally {
                    StreamUtils.close(bufferedReader);
                }


            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                if (listener != null) {
                    listener.onRUpdate(values);
                }
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (listener != null) {
                    if (result == null) {
                        listener.onRSuccess();
                    } else {
                        listener.onRFail(result);
                    }
                }
            }
        }.execute();

    }
    public interface OnSmsRecoverListner{
        void onRPre();
        void onRUpdate(Integer... values);
        void onRSuccess();
        void onRFail(Exception result);
    }
}
