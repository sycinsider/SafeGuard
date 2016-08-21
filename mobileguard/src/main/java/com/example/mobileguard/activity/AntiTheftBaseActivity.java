package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

abstract class AntiTheftBaseActivity extends Activity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {

                float startX = e1.getRawX();
                float startY = e1.getRawY();
                float endX = e2.getRawX();
                float endY = e2.getRawY();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if (distanceX > distanceY) {
                    if (endX > startX) {
                        startPre();
                    } else if (endX < startX) {
                        startNext();
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void startNext() {
        boolean flag = setNext();

        if (flag) {
            overridePendingTransition(R.anim.next_enter, R.anim.next_exit);
            finish();
        }
    }

    private void startPre() {
        boolean flag = setPre();
        if (flag) {
            overridePendingTransition(R.anim.pre_enter, R.anim.pre_exit);
            finish();
        }
    }

    public void goNext(View v) {
        startNext();
    }

    public void goPre(View v) {
        startPre();
    }

    protected abstract boolean setNext();

    protected abstract boolean setPre();
}
