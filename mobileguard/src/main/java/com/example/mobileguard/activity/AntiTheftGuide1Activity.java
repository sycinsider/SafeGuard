package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AntiTheftGuide1Activity extends AntiTheftBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_guide1);
    }

    @Override
    protected boolean setNext() {
        Intent intent = new Intent(this, AntiTheftGuide2Activity.class);
        startActivity(intent);
        return true;
    }

    @Override
    protected boolean setPre() {
        return false;
        // TODO Auto-generated method stub

    }

}
