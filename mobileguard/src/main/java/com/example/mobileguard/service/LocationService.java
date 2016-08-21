
package com.example.mobileguard.service;

import com.example.mobileguard.db.QueryLocationDao;
import com.example.mobileguard.view.LocationToast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * ClassName:LocationService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月13日 下午3:47:08 <br/>
 * 
 * @author dell
 * @version
 */
public class LocationService extends Service {

    private CalloutListener outlistener;
    private IntentFilter filter;
    private TelephonyManager tm;
    private CallInListener inListener;
    private LocationToast toast;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        outlistener = new CalloutListener();
        filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(outlistener, filter);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        inListener = new CallInListener();
        tm.listen(inListener, PhoneStateListener.LISTEN_CALL_STATE);
        toast = new LocationToast(this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        toast.hide();
        tm.listen(inListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(outlistener);
    }

    class CalloutListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String num = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String location = QueryLocationDao.getLocation(LocationService.this, num);

            toast.show(location);
        }

    }
    class CallInListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                String location =
                        QueryLocationDao.getLocation(LocationService.this, incomingNumber);
                toast.show(location);
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                toast.hide();
            }
        }
    }
}
