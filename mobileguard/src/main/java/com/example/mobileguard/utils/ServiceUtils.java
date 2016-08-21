
package com.example.mobileguard.utils;

import java.util.List;



import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

/**
 * ClassName:ServiceUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月13日 上午10:49:49 <br/>
 * 
 * @author dell
 * @version
 */
public class ServiceUtils {

    public static boolean isServiceRunning(Context context, Class<? extends Service> clazz) {

        boolean flag = false;
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo info : services) {
            String className = info.service.getClassName();
            if (className.equals(clazz.getName())) {
                flag = true;
            }
        }
        return flag;
    }
}
