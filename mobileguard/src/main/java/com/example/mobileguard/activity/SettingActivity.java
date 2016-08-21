package com.example.mobileguard.activity;

import com.example.mobileguard.R;

import com.example.mobileguard.R.layout;
import com.example.mobileguard.service.BlackNumberService;
import com.example.mobileguard.service.LocationService;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.PkgUtils;
import com.example.mobileguard.utils.ServiceUtils;
import com.example.mobileguard.utils.SpUtils;
import com.example.mobileguard.view.LocationDialogView;
import com.example.mobileguard.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity implements OnClickListener {

    private SettingItemView update;
    private SettingItemView guard;
    private SettingItemView location;
    private SettingItemView themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initEvent();
    }

    private void initView() {
        update = (SettingItemView) findViewById(R.id.set_update);
        guard = (SettingItemView) findViewById(R.id.set_guard);
        location = (SettingItemView) findViewById(R.id.set_location);
        themes = (SettingItemView) findViewById(R.id.set_location_themes);

    }

    private void initEvent() {
        guard.setOnClickListener(this);
        update.setOnClickListener(this);
        location.setOnClickListener(this);
        themes.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        boolean update_state = SpUtils.getBoolean(this, Constantset.SET_UPDATE, true);
        update.setOpenSwitch(update_state);
        boolean guard_state = ServiceUtils.isServiceRunning(this, BlackNumberService.class);
        guard.setOpenSwitch(guard_state);
        boolean location_state = ServiceUtils.isServiceRunning(this, LocationService.class);
        location.setOpenSwitch(location_state);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.set_update:
                boolean update_state = SpUtils.getBoolean(this, Constantset.SET_UPDATE, true);
                if (update_state) {
                    update.setOpenSwitch(false);
                    SpUtils.putBoolean(this, Constantset.SET_UPDATE, false);
                } else {
                    update.setOpenSwitch(true);
                    SpUtils.putBoolean(this, Constantset.SET_UPDATE, true);
                }
                break;
            case R.id.set_guard:
                boolean guard_state = ServiceUtils.isServiceRunning(this, BlackNumberService.class);
                if (guard_state) {
                    Intent intent = new Intent(this, BlackNumberService.class);
                    guard.setOpenSwitch(false);
                    stopService(intent);
                } else {
                    Intent intent = new Intent(this, BlackNumberService.class);
                    guard.setOpenSwitch(true);
                    startService(intent);
                }
                break;
            case R.id.set_location:
                boolean location_state = ServiceUtils.isServiceRunning(this, LocationService.class);
                if (location_state) {
                    Intent intent = new Intent(this, LocationService.class);
                    location.setOpenSwitch(false);
                    stopService(intent);
                } else {
                    Intent intent = new Intent(this, LocationService.class);
                    location.setOpenSwitch(true);
                    startService(intent);
                }
                break;
            case R.id.set_location_themes:
                   LocationDialogView dialogView = new LocationDialogView(this);
                   dialogView.show();
                break;
        }
    }
}
