package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AntiTheftGuide2Activity extends AntiTheftBaseActivity implements OnClickListener {

    private Button bind_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_guide2);
        initView();
        initEvent();
    }

    private void initView() {
        bind_sim = (Button) findViewById(R.id.guide2_bind_sim);

    }

    private void initEvent() {
        bind_sim.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        String savesim = SpUtils.getString(this, Constantset.SIM_NUMBER);
        if (TextUtils.isEmpty(savesim)) {
            Drawable drawable = getResources().getDrawable(R.drawable.unlock_selector);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            bind_sim.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.lock_selector);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            bind_sim.setCompoundDrawables(null, null, drawable, null);

        }
    }

    @Override
    protected boolean setNext() {
        String savesim = SpUtils.getString(this, Constantset.SIM_NUMBER);

        if (!TextUtils.isEmpty(savesim)) {
            Intent intent = new Intent(this, AntiTheftGuide3Activity.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(this, "请绑定sim卡", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    protected boolean setPre() {
        Intent intent = new Intent(this, AntiTheftGuide1Activity.class);
        startActivity(intent);

        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide2_bind_sim:
                String savesim = SpUtils.getString(this, Constantset.SIM_NUMBER);
                if (TextUtils.isEmpty(savesim)) {
                    TelephonyManager manager =
                            (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    if (simSerialNumber != null) {
                        SpUtils.putString(this, Constantset.SIM_NUMBER, simSerialNumber);
                        Drawable drawable = getResources().getDrawable(R.drawable.lock_selector);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        bind_sim.setCompoundDrawables(null, null, drawable, null);
                    } else {
                        Toast.makeText(this, "请确认插入sim卡", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    SpUtils.putString(this, Constantset.SIM_NUMBER, null);
                    Drawable drawable = getResources().getDrawable(R.drawable.unlock_selector);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight());
                    bind_sim.setCompoundDrawables(null, null, drawable, null);
                }
                break;

        }

    }

}
