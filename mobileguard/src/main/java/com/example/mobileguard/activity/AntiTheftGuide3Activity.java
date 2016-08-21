package com.example.mobileguard.activity;

import java.sql.Date;

import com.example.mobileguard.R;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AntiTheftGuide3Activity extends AntiTheftBaseActivity {
    private EditText guide3_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_guide3);
        guide3_et = (EditText) findViewById(R.id.guide3_et);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String number = SpUtils.getString(this, Constantset.SAFE_NUMBER);
        if (!TextUtils.isEmpty(number)) {
            guide3_et.setText(number);
            guide3_et.setSelection(number.length());
        }
    }

    @Override
    protected boolean setNext() {
        String curentnumber = guide3_et.getText().toString().trim();
        if (!TextUtils.isEmpty(curentnumber)) {
            SpUtils.putString(this, Constantset.SAFE_NUMBER, curentnumber);
            Intent intent = new Intent(this, AntiTheftGuide4Activity.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected boolean setPre() {
        Intent intent = new Intent(this, AntiTheftGuide2Activity.class);
        startActivity(intent);
        return true;

    }

    public void add(View v) {

        Intent intent = new Intent(this, ContactListActivity.class);
        startActivityForResult(intent, Constantset.RQT_CONTACT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constantset.RQT_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    String number = data.getStringExtra(Constantset.SELECTED_NUM);
                    System.out.println(number);
                    guide3_et.setText(number);
                    SpUtils.putString(this, Constantset.SAFE_NUMBER, number);
                    guide3_et.setSelection(number.length());
                }
                break;

        }
    }
}
