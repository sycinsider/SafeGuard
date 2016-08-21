
package com.example.mobileguard.service;

import java.lang.reflect.Method;


import com.android.internal.telephony.ITelephony;
import com.example.mobileguard.bean.BlackBean;
import com.example.mobileguard.db.BlackDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

/**
 * ClassName:BlackNumberService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月11日 下午10:47:23 <br/>
 * 
 * @author dell
 * @version
 */
public class BlackNumberService extends Service {

    private InterCeptSMSReceiver receiver;
    private TelephonyManager manager;
    private InterceptCallListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new InterCeptSMSReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, intentFilter);
        manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        listener = new InterceptCallListener();
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    class InterceptCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                int type = BlackDao.getType(BlackNumberService.this, incomingNumber);
                if (type == BlackBean.TYPE_ALL || type == BlackBean.TYPE_PHONE) {
                    try {
                        Class<?> clazz = Class.forName("android.os.ServiceManager");
                        Method method = clazz.getMethod("getService", String.class);
                        IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                        ITelephony telephony = ITelephony.Stub.asInterface(iBinder);
                        telephony.endCall();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    class InterCeptSMSReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SmsMessage[] messages = getMessagesFromIntent(intent);
            for (SmsMessage smsMessage : messages) {
                String number = smsMessage.getOriginatingAddress();

                int type = BlackDao.getType(context, number);
                if (type == BlackBean.TYPE_ALL || type == BlackBean.TYPE_SMS) {
                    System.out.println("短信拦截成功");
                    abortBroadcast();

                }
            }
        }

        public SmsMessage[] getMessagesFromIntent(Intent intent) {
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
}
