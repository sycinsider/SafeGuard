
package com.example.mobileguard.receiver;

import com.example.mobileguard.R;
import com.example.mobileguard.service.GPSService;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.LogUtils;
import com.example.mobileguard.utils.SpUtils;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;

/**
 * ClassName:SMSReceiver <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月10日 上午10:20:48 <br/>
 * 
 * @author dell
 * @version
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean state = SpUtils.getBoolean(context, Constantset.ANTI_THEFT_STATUS);
        if (!state) {
            LogUtils.e("没有开启手机防盗");
            return;
        }
        SmsMessage[] messages = getMessagesFromIntent(intent);
        LogUtils.e("开启手机防盗");
        for (SmsMessage smsMessage : messages) {
            String body = smsMessage.getMessageBody();
            String address = smsMessage.getOriginatingAddress();
            String safenumber = SpUtils.getString(context, Constantset.SAFE_NUMBER);
            if (address == safenumber) {
                if (TextUtils.isEmpty(body)) {
                    return;
                } else {
                    if (body.equals("#location#")) {
                        getLocation(context);
                        // 中断广播
                        abortBroadcast();
                    } else if (body.equals("#alarm#")) {
                        playAlarm(context);
                        abortBroadcast();
                    } else if (body.equals("#wipedata#")) {
                        wipeData(context);
                        abortBroadcast();
                    } else if (body.equals("#lockscreen#")) {
                        lockScreen(context);
                        abortBroadcast();
                    }
                }

            }
        }
    }

    private void lockScreen(Context context) {
        DevicePolicyManager manager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        manager.resetPassword("123", DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        manager.lockNow();
    }

    private void wipeData(Context context) {
        DevicePolicyManager manager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        manager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);

    }

    private void playAlarm(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
        player.setVolume(1.0f, 1.0f);
        player.setLooping(true);
        player.start();
    }

    private void getLocation(Context context) {
        Intent service = new Intent(context, GPSService.class);
        context.startService(service);
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        String format = intent.getStringExtra("format");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
