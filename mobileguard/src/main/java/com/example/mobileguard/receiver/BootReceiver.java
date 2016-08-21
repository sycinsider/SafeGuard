
package com.example.mobileguard.receiver;

import com.example.mobileguard.service.ProtectedService;
import com.example.mobileguard.utils.Constantset;

import com.example.mobileguard.utils.LogUtils;
import com.example.mobileguard.utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.SmsManager;
import android.text.TextUtils;

/**
 * ClassName:BootReceiver <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月9日 下午6:02:14 <br/>
 * 
 * @author dell
 * @version
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("开启了1");
        context.startService(new Intent(context, ProtectedService.class));
        boolean state = SpUtils.getBoolean(context, Constantset.ANTI_THEFT_STATUS);
        if (!state) {
            LogUtils.e("没有开启手机防盗");
            return;
        } else {
            System.out.println("开启了1");
            String sim_number = SpUtils.getString(context, Constantset.SIM_NUMBER);
            if (TextUtils.isEmpty(sim_number)) {
                LogUtils.e("没有开启手机防盗");
                return;
            } else {
                System.out.println("开启了2");
                TelephonyManager manager =
                        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String currentNumber = manager.getSimSerialNumber();
                if (TextUtils.isEmpty(currentNumber)) {
                    LogUtils.e("没有手机卡");
                    return;
                } else {
                    System.out.println("开启了3");
                    String safe_number = SpUtils.getString(context, Constantset.SAFE_NUMBER);
                    if (TextUtils.isEmpty(safe_number)) {
                        LogUtils.e("没有设置安全号码");
                        return;
                    }
                    System.out.println("开启了1");
                    if (sim_number == currentNumber) {
                    } else {
                        SmsManager.getDefault().sendTextMessage(safe_number, null, "手机丢了", null,
                                null);
                    }
                }
            }
        }
    }

}
