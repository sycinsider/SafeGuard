
package com.example.mobileguard.view;

import com.example.mobileguard.R;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.annotation.SuppressLint;
import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.view.WindowManager;

/**
 * ClassName:LocationToast <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月13日 下午4:17:53 <br/>
 * 
 * @author dell
 * @version
 */
public class LocationToast implements OnTouchListener {
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private WindowManager mWM;
    private Context context;
    View mView;
    private float startX;
    private float startY;

    public LocationToast(Context context) {
        this.context = context;
        final WindowManager.LayoutParams params = mParams;
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    public void show(String msg) {
        if (mView == null) {
            mView = View.inflate(context, R.layout.location_item, null);
        }
        mView.setOnTouchListener(this);
        TextView tv = (TextView) mView.findViewById(R.id.location_item_tv);
        int icon = SpUtils.getInt(context, Constantset.LOCATION_THEME);
        mView.setBackgroundResource(icon);
        tv.setText(msg);
        mWM.addView(mView, mParams);
    }

    public void hide() {
        if (mView != null) {
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getRawX();
                float currentY = event.getRawY();
                float distanceX = currentX - startX;
                float distanceY = currentY - startY;
                mParams.x += distanceX;
                mParams.y += distanceY;
                mWM.updateViewLayout(mView, mParams);
                startX = currentX;
                startY = currentY;
                break;

        }
        return true;
    }
}
