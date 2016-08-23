package com.example.mobileguard.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import com.example.mobileguard.R;
import com.example.mobileguard.view.SegmentView;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

public class AppLockActivity extends Activity  {

    private SegmentView activityAppLockSv;
    private TextView activityAppLockTitle;
    private ListView activityAppLockUnlocklv;
    private ListView activityAppLockLocklv;
    private LinearLayout activityAppLockLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        initView();
        initData();
    }

    private void initData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        }.execute();
    }

    private void initView() {
        activityAppLockSv = (SegmentView) findViewById(R.id.activity_app_lock_sv);
        activityAppLockTitle = (TextView) findViewById(R.id.activity_app_lock_title);
        activityAppLockUnlocklv = (ListView) findViewById(R.id.activity_app_lock_unlocklv);
        activityAppLockLocklv = (ListView) findViewById(R.id.activity_app_lock_locklv);
        activityAppLockLoading = (LinearLayout) findViewById(R.id.loading_now);
    }

}
