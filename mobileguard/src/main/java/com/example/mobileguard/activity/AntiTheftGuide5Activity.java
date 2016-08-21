package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AntiTheftGuide5Activity extends AntiTheftBaseActivity
        implements OnCheckedChangeListener {

    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_guide5);
        checkBox = (CheckBox) findViewById(R.id.anti_theft_guide5_cb);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        boolean isChecked = SpUtils.getBoolean(this, Constantset.ANTI_THEFT_STATUS);
        checkBox.setChecked(isChecked);
    }

    @Override
    protected boolean setNext() {
        boolean isChecked = SpUtils.getBoolean(this, Constantset.ANTI_THEFT_STATUS);
        if (isChecked) {
            Intent intent = new Intent(this, AntiTheftActivity.class);
            startActivity(intent);
            return true;
        }else {
            Toast.makeText(this, "需要启用手机防盗", Toast.LENGTH_SHORT).show();
            return false;
        }
        

    }

    @Override
    protected boolean setPre() {
        Intent intent = new Intent(this, AntiTheftGuide4Activity.class);
        startActivity(intent);

        return true;

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SpUtils.putBoolean(this, Constantset.ANTI_THEFT_STATUS, isChecked);

    }

}
