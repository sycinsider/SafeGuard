package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.drawable;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.R.raw;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AntiTheftActivity extends Activity implements OnClickListener {

    private TextView tv_number;
    private ImageView iv_state;
    private RelativeLayout rl_auto;
    private RelativeLayout rl_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft);
        initView();
        initEvent();
    }

    private void initEvent() {

        rl_auto.setOnClickListener(this);
        rl_reset.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub

        super.onStart();
        String safe_number = SpUtils.getString(this, Constantset.SAFE_NUMBER);
        tv_number.setText(safe_number);
        boolean status = SpUtils.getBoolean(this, Constantset.ANTI_THEFT_STATUS);
        if (status) {
            iv_state.setImageResource(R.drawable.lock);
        } else {
            iv_state.setImageResource(R.drawable.unlock);
        }
    }

    private void initView() {
        tv_number = (TextView) findViewById(R.id.anti_theft_tv_number);
        iv_state = (ImageView) findViewById(R.id.anti_theft_iv_state);
        rl_auto = (RelativeLayout) findViewById(R.id.anti_theft_rl_auto);
        rl_reset = (RelativeLayout) findViewById(R.id.anti_theft_rl_reset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anti_theft_rl_auto:
                boolean status = SpUtils.getBoolean(this, Constantset.ANTI_THEFT_STATUS);
                if (status) {
                    SpUtils.putBoolean(this, Constantset.ANTI_THEFT_STATUS, false);
                    iv_state.setImageResource(R.drawable.unlock);
                } else {
                    SpUtils.putBoolean(this, Constantset.ANTI_THEFT_STATUS, true);
                    iv_state.setImageResource(R.drawable.lock);
                }
                break;
            case R.id.anti_theft_rl_reset:
                Intent intent = new Intent(this, AntiTheftGuide1Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
