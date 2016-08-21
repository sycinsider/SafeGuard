
package com.example.mobileguard.service;

import java.util.List;

import com.example.mobileguard.business.ProcessProvider;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.Toast;

/**
 * ClassName:KillProcessService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月18日 下午8:13:50 <br/>
 * 
 * @author dell
 * @version
 */
public class KillProcessService extends Service {

    private ActivityManager am;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcess = ProcessProvider.getRunningProcess(this);
        long[] memSize1 = ProcessProvider.getMemSize(this);
        long freeSize1 = memSize1[0];
        int startCount = runningProcess.size();
        for (RunningAppProcessInfo info : runningProcess) {
            am.killBackgroundProcesses(info.processName);
        }
        int endCount = ProcessProvider.getRunning(this);
        long[] memSize2 = ProcessProvider.getMemSize(this);
        long freeSize2 = memSize2[0];
        int cleanedNums = startCount - endCount;
        long cleanedRam = Math.abs(freeSize2 - freeSize1);
        if (cleanedNums > 0) {
            Toast.makeText(this, "清理了" + cleanedNums + "个进程,释放了"
                    + Formatter.formatFileSize(this, cleanedRam) + "内存", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "您的手机很干净", Toast.LENGTH_SHORT).show();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
