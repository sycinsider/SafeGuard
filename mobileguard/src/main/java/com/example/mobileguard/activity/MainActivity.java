package com.example.mobileguard.activity;

import com.example.mobileguard.CleanCacheActivity;
import com.example.mobileguard.R;
import com.example.mobileguard.adapter.GridAdapter;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.animation.ObjectAnimator;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {

    private ImageView iv_logo;
    private GridView main_grid;
    private ImageView ivsetting;
    private EditText set_pwd_et1;
    private EditText set_pwd_et2;
    private AlertDialog set_pwd_dialog;
    private EditText enter_pwd_et;
    private AlertDialog enter_pwd_dialog;
    private String userpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        playAnim();
        initEvent();
    }

    private void playAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_logo, "rotationY", 0, 30, 60, 90, 120,
                150, 180, 210, 240, 270, 300, 330, 360);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    private void initView() {
        iv_logo = (ImageView) findViewById(R.id.main_activity_iv_logo);
        main_grid = (GridView) findViewById(R.id.main_activity_grid);
        ivsetting = (ImageView) findViewById(R.id.main_activity_iv_setting);
    }

    private void initEvent() {
        ivsetting.setOnClickListener(this);
        GridAdapter gridAdapter = new GridAdapter(this);
        main_grid.setAdapter(gridAdapter);
        main_grid.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.main_activity_iv_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.set_pwd_cancel_btn:
                set_pwd_dialog.dismiss();
                break;

            case R.id.enter_pwd_cancel_btn:
                enter_pwd_dialog.dismiss();
                break;
            case R.id.enter_pwd_enter_btn:
                String input_pwd = enter_pwd_et.getText().toString().trim();
                if (TextUtils.isEmpty(input_pwd)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userpwd.equals(input_pwd)) {
                    boolean state = SpUtils.getBoolean(this, Constantset.ANTI_THEFT_STATUS);
                    if (state) {
                        Intent intent2 = new Intent(this, AntiTheftActivity.class);
                        startActivity(intent2);
                    } else {

                        Intent intent2 = new Intent(this, AntiTheftGuide1Activity.class);
                        startActivity(intent2);
                    }
                    enter_pwd_dialog.dismiss();
                } else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            case R.id.set_pwd_enter_btn:
                String pwd1 = set_pwd_et1.getText().toString().trim();
                String pwd2 = set_pwd_et2.getText().toString().trim();
                if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd1.equals(pwd2)) {
                    SpUtils.putString(this, Constantset.ANTI_THEFT_PWD, pwd1);
                    Intent intent2 = new Intent(this, AntiTheftGuide1Activity.class);
                    startActivity(intent2);
                    set_pwd_dialog.dismiss();
                } else {
                    Toast.makeText(this, "两次输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                antiTheft();

                break;
            case 4:
                startActivity(new Intent(this, TrafficActivity.class));

                break;
            case 3:
                startActivity(new Intent(this, ProcessManagerActivity.class));

                break;
            case 2:;
                startActivity(new Intent(this, APPManagerActivity.class));
                break;
            case 1:
                Intent intent = new Intent(this, BlackListActivity.class);
                startActivity(intent);
                break;
            case 7:
                startActivity(new Intent(this, ToolsActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, CleanCacheActivity.class));
                break;

        }

    }

    private void antiTheft() {
        userpwd = SpUtils.getString(this, Constantset.ANTI_THEFT_PWD);
        if (userpwd == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogview = View.inflate(this, R.layout.dialog_set_pwd, null);
            builder.setView(dialogview);
            set_pwd_et1 = (EditText) dialogview.findViewById(R.id.dialog_set_pwd_et1);
            set_pwd_et2 = (EditText) dialogview.findViewById(R.id.dialog_set_pwd_et2);
            Button set_pwd_enter_btn = (Button) dialogview.findViewById(R.id.set_pwd_enter_btn);
            Button set_pwd_cancel_btn = (Button) dialogview.findViewById(R.id.set_pwd_cancel_btn);
            set_pwd_enter_btn.setOnClickListener(this);
            set_pwd_cancel_btn.setOnClickListener(this);
            set_pwd_dialog = builder.show();
            set_pwd_dialog.setCanceledOnTouchOutside(false);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogview = View.inflate(this, R.layout.dialog_enter_pwd, null);
            builder.setView(dialogview);
            enter_pwd_et = (EditText) dialogview.findViewById(R.id.dialog_enter_pwd_et);
            Button enter_pwd_enter_btn = (Button) dialogview.findViewById(R.id.enter_pwd_enter_btn);
            Button enter_pwd_cancel_btn =
                    (Button) dialogview.findViewById(R.id.enter_pwd_cancel_btn);
            enter_pwd_cancel_btn.setOnClickListener(this);
            enter_pwd_enter_btn.setOnClickListener(this);
            enter_pwd_dialog = builder.show();
            enter_pwd_dialog.setCanceledOnTouchOutside(false);
        }
    }

}
