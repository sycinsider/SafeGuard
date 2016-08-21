
package com.example.mobileguard.business;

import java.io.BufferedReader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;

import com.example.mobileguard.utils.StreamUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.MemoryFile;

/**
 * ClassName:ProcessProvider <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月17日 下午8:26:08 <br/>
 * 
 * @author dell
 * @version
 */
public class ProcessProvider {
    public static ActivityManager am;

    public static int getProcesses(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        HashSet<String> set = new HashSet<String>();
        for (PackageInfo info : packages) {
            String processName = info.applicationInfo.processName;
            set.add(processName);
            ActivityInfo[] activities = info.activities;
            if (activities != null) {
                for (ActivityInfo activityInfo : activities) {
                    String processName2 = activityInfo.processName;
                    set.add(processName2);
                }
            }
            ServiceInfo[] services = info.services;
            if (services != null) {
                for (ServiceInfo serviceInfo : services) {
                    String processName3 = serviceInfo.processName;
                    set.add(processName3);
                }
            }
            ActivityInfo[] receivers = info.receivers;
            if (receivers != null) {
                for (ActivityInfo receiverInfo : receivers) {
                    String processName4 = receiverInfo.processName;
                    set.add(processName4);
                }

            }
            ProviderInfo[] providers = info.providers;
            if (providers != null) {
                for (ProviderInfo providerInfo : providers) {
                    String processName5 = providerInfo.processName;
                    set.add(processName5);
                }

            }
        }

        return set.size();
    }

    public static int getRunning(Context context) {
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    public static List<RunningAppProcessInfo> getRunningProcess(Context context) {
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        return runningAppProcesses;
    }

    @SuppressLint("NewApi")
    public static long[] getMemSize(Context context) {
        MemoryInfo outInfo = new MemoryInfo();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        long totalMem = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            totalMem = outInfo.totalMem;
        } else {
            BufferedReader br = null;
            File file = new File("/proc/meminfo");
            try {
                br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                line = line.replace("MemTotal", "");
                line = line.replace("kB", "");
                line = line.trim();
                totalMem = Long.valueOf(line) * 1024;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                StreamUtils.close(br);
            }
        }
        long usedMem = totalMem - availMem;
        return new long[] {availMem, usedMem, totalMem};
    }

}
