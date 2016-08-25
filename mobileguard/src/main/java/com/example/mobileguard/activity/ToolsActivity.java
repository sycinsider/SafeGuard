package com.example.mobileguard.activity;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import com.example.mobileguard.R;
import com.example.mobileguard.activity.CommonNumActivity;
import com.example.mobileguard.activity.PhoneLoctionActivity;
import com.example.mobileguard.utils.BackupUtils;
import com.example.mobileguard.utils.BackupUtils.OnBackupListener;
import com.example.mobileguard.utils.LogUtils;
import com.example.mobileguard.utils.RecoverUtils;
import com.example.mobileguard.utils.StreamUtils;
import com.example.mobileguard.view.SettingItemView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ToolsActivity extends Activity implements OnClickListener, OnBackupListener, RecoverUtils.OnSmsRecoverListner {

    private SettingItemView tools_location;
    private SettingItemView tools_common;
    private SettingItemView tools_backup;
    private SettingItemView tools_recover;
    private SettingItemView tools_lockmanager;
    private SettingItemView tools_lock1;
    private SettingItemView tools_lock2;
    private ProgressDialog dialog;
    private FileOutputStream fos;
    private XmlSerializer serializer;
    private ContentResolver resolver;
    private ProgressDialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        initView();
        initEvent();
    }

    private void initView() {
        tools_location = (SettingItemView) findViewById(R.id.tools_activity_location);
        tools_common = (SettingItemView) findViewById(R.id.tools_activity_common);
        tools_backup = (SettingItemView) findViewById(R.id.tools_activity_backup);
        tools_recover = (SettingItemView) findViewById(R.id.tools_activity_recover);
        tools_lockmanager = (SettingItemView) findViewById(R.id.tools_activity_lockmanager);
        tools_lock1 = (SettingItemView) findViewById(R.id.tools_activity_lock1);
        tools_lock2 = (SettingItemView) findViewById(R.id.tools_activity_lock2);

    }

    private void initEvent() {
        tools_location.setOnClickListener(this);
        tools_common.setOnClickListener(this);
        tools_backup.setOnClickListener(this);
        tools_recover.setOnClickListener(this);
        tools_lockmanager.setOnClickListener(this);
        tools_lock1.setOnClickListener(this);
        tools_lock2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tools_activity_location:
                Intent intent = new Intent(this, PhoneLoctionActivity.class);
                startActivity(intent);
                break;
            case R.id.tools_activity_common:
                Intent intent2 = new Intent(this, CommonNumActivity.class);
                startActivity(intent2);
                break;
            case R.id.tools_activity_backup:
                /*
                 * dialog = new ProgressDialog(this);
                 * dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                 * dialog.setMessage("短信正在备份中..."); dialog.show(); new Thread(new Runnable() {
                 * 
                 * private boolean result;
                 * 
                 * @Override public void run() { result = backupSMS(); runOnUiThread(new Runnable()
                 * {
                 * 
                 * @Override public void run() { if (result) { Toast.makeText(ToolsActivity.this,
                 * "备份成功", Toast.LENGTH_SHORT) .show(); } else {
                 * 
                 * Toast.makeText(ToolsActivity.this, "备份失败", Toast.LENGTH_SHORT) .show(); }
                 * dialog.dismiss(); } }); } }).start();
                 */
                BackupUtils.smsBackup(this, this);
                break;
            case R.id.tools_activity_recover:
                RecoverUtils.smsRecover(this,this);
                break;
            case R.id.tools_activity_lockmanager:
                startActivity(new Intent(this,AppLockActivity.class));
                break;

        }

    }

    public boolean backupSMS() {
        // if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)) {
        serializer = Xml.newSerializer();
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        try {
            fos = new FileOutputStream(file);
            resolver = getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            String[] projection = new String[] {"address", "date", "type", "body"};
            Cursor cursor = resolver.query(uri, projection, null, null, null);
            serializer.setOutput(fos, "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
            int count = cursor.getCount();
            dialog.setMax(count);
            int progress = 0;
            while (cursor.moveToNext()) {
                String address = cursor.getString(0);
                String date = cursor.getString(1);
                String type = cursor.getString(2);
                String body = cursor.getString(3);
                serializer.startTag(null, "sms");
                serializer.startTag(null, "address");
                serializer.text(address);
                serializer.endTag(null, "address");
                serializer.startTag(null, "date");
                serializer.text(date);
                serializer.endTag(null, "date");
                serializer.startTag(null, "type");
                serializer.text(type);
                serializer.endTag(null, "type");
                serializer.startTag(null, "body");
                serializer.text(body);
                serializer.endTag(null, "body");
                serializer.endTag(null, "sms");
                System.out.println(progress);
                progress++;
                dialog.setProgress(progress);
                System.out.println(progress);
                SystemClock.sleep(50);
            }
            serializer.endTag(null, "smss");
            serializer.endDocument();
            cursor.close();
            return true;
        } catch (Exception e) {
            LogUtils.e("");
            return false;
        } finally {
            StreamUtils.close(fos);
        }
        // }
        // return false;
    }

    @Override
    public void onPre() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("短信正在备份中...");
        dialog.show();
    }

    @Override
    public void onUpdate(Integer... values) {
        dialog.setProgress(values[0]);
        dialog.setMax(values[1]);
    }

    @Override
    public void onSuccess() {
        dialog.dismiss();
        Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFail(Exception result) {
        dialog.dismiss();
        Toast.makeText(this, "备份失败" + result, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRPre() {
        dialog1 = new ProgressDialog(this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog1.setMessage("短信正在恢复...");
        dialog1.show();
    }

    @Override
    public void onRUpdate(Integer... values) {
        dialog1.setProgress(values[0]);
        dialog1.setMax(values[1]);
    }

    @Override
    public void onRSuccess() {
        dialog1.dismiss();
        Toast.makeText(this, "恢复成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRFail(Exception result) {
        dialog1.dismiss();
        Toast.makeText(this, "恢复失败" + result, Toast.LENGTH_SHORT).show();
    }
}
