package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.drawable;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.receiver.UserDeviceAdminReceiver;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothClass.Device;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AntiTheftGuide4Activity extends AntiTheftBaseActivity implements OnClickListener {

    private static final int ROMTE_DEVICE_ADMIN = 100;
    private DevicePolicyManager dpm;
    private ComponentName admin;
    private Button remote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_theft_guide4);
        remote = (Button) findViewById(R.id.anti_theft_remote);
        remote.setOnClickListener(this);
        dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        admin = new ComponentName(this, UserDeviceAdminReceiver.class);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        boolean adminActive = dpm.isAdminActive(admin);
        if (adminActive) {
            Drawable drawable = getResources().getDrawable(R.drawable.admin_activated);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            remote.setCompoundDrawables(null, null, drawable, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.admin_inactivated);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            remote.setCompoundDrawables(null, null, drawable, null);
        }

    }

    @Override
    protected boolean setNext() {
        boolean adminActive = dpm.isAdminActive(admin);
        if (adminActive) {
            Intent intent = new Intent(this, AntiTheftGuide5Activity.class);
            startActivity(intent);
            return true;
        } else {
            Toast.makeText(this, "请激活管理员权限", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected boolean setPre() {
        Intent intent = new Intent(this, AntiTheftGuide3Activity.class);
        startActivity(intent);
        return true;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anti_theft_remote:
                boolean adminActive = dpm.isAdminActive(admin);
                if (adminActive) {
                    dpm.removeActiveAdmin(admin);
                    Drawable drawable = getResources().getDrawable(R.drawable.admin_inactivated);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight());
                    remote.setCompoundDrawables(null, null, drawable, null);
                } else {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "黑马手机卫士");
                    startActivityForResult(intent, ROMTE_DEVICE_ADMIN);
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case ROMTE_DEVICE_ADMIN:
                if (resultCode == Activity.RESULT_OK) {
                    Drawable drawable = getResources().getDrawable(R.drawable.admin_activated);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                            drawable.getMinimumHeight());
                    remote.setCompoundDrawables(null, null, drawable, null);
                }
                break;
        }
    }
}
