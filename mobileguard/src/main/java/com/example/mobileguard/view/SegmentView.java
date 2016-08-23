package com.example.mobileguard.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobileguard.R;

/**
 * Created by dell on 2016/8/21.
 */
public class SegmentView extends RelativeLayout implements View.OnClickListener {

    public static final int SELECT_RIGHT = 1;
    public static final int SELECT_LEFT = 0;
    private TextView viewSegementLeft;
    private TextView viewSegmentRight;
    private OnSegmentListener listener;

    public SegmentView(Context context) {
        this(context, null);
    }

    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_segment, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentView);
        String leftString = ta.getString(R.styleable.SegmentView_svLeftText);
        String rightString = ta.getString(R.styleable.SegmentView_svRightText);
        int isSelected = ta.getInt(R.styleable.SegmentView_svSelected,SELECT_LEFT);
        viewSegementLeft = (TextView) findViewById(R.id.view_segment_left);
        viewSegmentRight = (TextView) findViewById(R.id.view_segment_right);
        viewSegementLeft.setText(leftString);
        viewSegmentRight.setText(rightString);
        switch (isSelected) {
            case SELECT_LEFT:
                viewSegementLeft.setSelected(true);
                break;
            case SELECT_RIGHT:
                viewSegmentRight.setSelected(true);
                break;
        }
        // 设置监听
        viewSegementLeft.setOnClickListener(this);
        viewSegmentRight.setOnClickListener(this);
        // 释放资源
        ta.recycle();
    }

    private boolean isLeftSelected = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_segment_left:
                if (!isLeftSelected) {
                    viewSegementLeft.setSelected(true);
                    viewSegmentRight.setSelected(false);
                    isLeftSelected = true;
                    if (listener != null)
                        listener.onLeftSelected();
                }
                break;
            case R.id.view_segment_right:
                if (isLeftSelected) {
                    viewSegementLeft.setSelected(false);
                    viewSegmentRight.setSelected(true);
                    isLeftSelected = false;
                    if (listener != null)
                        listener.onRightSelected();
                }
                break;
        }
    }
    public void setOnSegmentSelectedListener(OnSegmentListener listener){
        this.listener =  listener;
    }
    public interface OnSegmentListener{
        void onLeftSelected();
        void onRightSelected();
    }
}