
package com.example.mobileguard.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobileguard.R;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.GzipUtils;
import com.example.mobileguard.utils.PkgUtils;
import com.example.mobileguard.utils.SpUtils;
import com.example.mobileguard.utils.StreamUtils;

import android.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName:LogoActivity <br/>
 * Function: 载入画面 <br/>
 * Date: 2016年8月5日 下午12:54:06 <br/>
 * 
 * @author dell
 * @version
 */
public class LogoActivity extends Activity {
    public static final int MSG_ERR = 300;
    private String server = "http://192.168.24.24:8080/safeguard/version.json";
    private String desc;
    private String downloadpath;
    public static final int DIALOG_REQUEST = 200;
    public static final int LOGO_UPDATE_DIALOG = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case LOGO_UPDATE_DIALOG:
                    updateDialog();
                    break;
                case MSG_ERR:
                    Toast.makeText(LogoActivity.this, "网络异常,请检查网络链接", Toast.LENGTH_SHORT).show();
                    gotoMain();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        TextView tv_version = (TextView) findViewById(R.id.logo_activity_tv_version);
        tv_version.setText("版本号:" + PkgUtils.getVersionName(this));
        boolean autoupdate = SpUtils.getBoolean(this, Constantset.SET_UPDATE, true);
        if (autoupdate) {
            new Thread(new CheckRunnable()).start();
        } else {
            gotoMain();
        }
        uncompressData();
        uncompressComNumData();
        uncompressVirusData();
    }

    private void uncompressComNumData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    InputStream in = getAssets().open("commonnum.zip");
                    File file = new File(getFilesDir(), "commonnum.db");
                    if (!file.exists()) {
                        GzipUtils.uncompress(in, new FileOutputStream(file));
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void uncompressVirusData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    InputStream in = getAssets().open("antivirus.zip");
                    File file = new File(getFilesDir(), "antivirus.db");
                    if (!file.exists()) {
                        GzipUtils.uncompress(in, new FileOutputStream(file));
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void uncompressData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    InputStream in = getAssets().open("address.zip");
                    File file = new File(getFilesDir(), "address.db");
                    if (!file.exists()) {
                        GzipUtils.uncompress(in, new FileOutputStream(file));
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    class CheckRunnable implements Runnable {

        private InputStream is = null;
        private BufferedReader bufferedReader = null;

        @Override
        public void run() {
            try {
                URL url = new URL(server);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.connect();
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String json = "";
                    String temp = "";
                    while ((temp = bufferedReader.readLine()) != null) {
                        json += temp;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    int returnVersion = jsonObject.getInt("versioncode");
                    int localVersion = PkgUtils.getVersionCode(LogoActivity.this);
                    if (returnVersion > localVersion) {
                        desc = jsonObject.getString("desc");
                        downloadpath = jsonObject.getString("url");
                        handler.sendEmptyMessage(LOGO_UPDATE_DIALOG);
                        System.out.println(downloadpath);
                    } else {
                        gotoMain();
                    }
                } else {
                    handler.sendEmptyMessage(MSG_ERR);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                handler.sendEmptyMessage(MSG_ERR);
                e.printStackTrace();
            } finally {
                StreamUtils.close(bufferedReader);
                StreamUtils.close(is);
            }
        }

    }
    class DownloadRunnable implements Runnable {

        private ProgressDialog pd;
        private int progress;

        public DownloadRunnable(ProgressDialog pd) {
            this.pd = pd;
        }

        @Override
        public void run() {
            InputStream is = null;
            FileOutputStream fos = null;
            try {

                URL url = new URL(downloadpath);
                System.out.println(downloadpath);
                System.out.println("down" + downloadpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.connect();
                int length = connection.getContentLength();
                pd.setMax(length);
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                    File file = new File(Environment.getExternalStorageDirectory(), "new.apk");
                    fos = new FileOutputStream(file);
                    int len = 0;
                    byte[] b = new byte[1024];
                    while ((len = is.read(b)) != -1) {
                        fos.write(b, 0, len);
                        progress += len;
                        pd.setProgress(progress);
                        SystemClock.sleep(1);
                    }
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setDataAndType(Uri.parse("file:" + file.getAbsolutePath()),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, DIALOG_REQUEST);
                } else {
                    handler.sendEmptyMessage(MSG_ERR);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                handler.sendEmptyMessage(MSG_ERR);
            } finally {
                StreamUtils.close(fos);
                StreamUtils.close(is);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case DIALOG_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    gotoMain();
                }
                break;
        }

    }

    private void updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新的版本");
        builder.setMessage(desc);
        builder.setPositiveButton("开始更新", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressDialog pd = new ProgressDialog(LogoActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.show();
                new Thread(new DownloadRunnable(pd)).start();
            }
        });
        builder.setNegativeButton("稍后再说", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                gotoMain();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void gotoMain() {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 1500);

    }
}
