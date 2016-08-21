
package com.example.mobileguard.service;

import com.example.mobileguard.R;

import com.example.mobileguard.activity.LogoActivity;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * ClassName:ProtectedService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月18日 下午6:21:59 <br/>
 * 
 * @author dell
 * @version
 */
public class ProtectedService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, LogoActivity.class);
        PendingIntent intent2 =
                PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(this).setContentTitle("黑马手机卫士")
                .setContentText("正在保护您的手机").setTicker("黑马手机卫士").setWhen(System.currentTimeMillis())
                .setContentIntent(intent2).setSmallIcon(R.drawable.ic_launcher).build();

        startForeground(1, noti);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, ProtectedService.class));
    }
}
