package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.db.QueryLocationDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhoneLoctionActivity extends Activity implements OnClickListener, TextWatcher {

    private EditText et;
    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_loction);
        initView();
        initEvent();
    }

    private void initView() {
        et = (EditText) findViewById(R.id.phone_loction_et);
        btn = (Button) findViewById(R.id.phone_loction_query);
        tv = (TextView) findViewById(R.id.phone_loction_tv);
    }

    private void initEvent() {
        btn.setOnClickListener(this);
        et.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_loction_query:
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                et.startAnimation(shake);
                btn.startAnimation(shake);
                tv.startAnimation(shake);
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String location = QueryLocationDao.getLocation(PhoneLoctionActivity.this, s.toString());
        tv.setText(location);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }
}
