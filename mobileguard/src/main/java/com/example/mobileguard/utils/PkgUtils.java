
package com.example.mobileguard.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * ClassName:PkgUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月5日 下午1:19:38 <br/>
 * 
 * @author dell
 * @version
 */
public class PkgUtils {

    public static String getVersionName(Context context) {
        String versionName = "未知";
        // 获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        // 获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //
        try {
            PackageInfo info = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
