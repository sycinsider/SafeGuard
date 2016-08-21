
package com.example.mobileguard.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * ClassName:AutoCleanService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月18日 下午4:42:25 <br/>
 * 
 * @author dell
 * @version
 */
public class AutoCleanService extends Service {

    private LockScreenReceiver receiver;
    private IntentFilter filter;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new LockScreenReceiver();
        filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class LockScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager am =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> info = am.getRunningAppProcesses();
            for (RunningAppProcessInfo runningInfo : info) {
                am.killBackgroundProcesses(runningInfo.processName);
            }
        }

    }
}
