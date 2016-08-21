 package com.example.mobileguard.view;

import com.example.mobileguard.R;
import com.example.mobileguard.R.drawable;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.R.styleable;
import com.example.mobileguard.utils.Constantset;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

    private ImageView set_item_iv;

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.activity_setting_item_view, this);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String sivtext = attributes.getString(R.styleable.SettingItemView_sivtext);
        boolean sivswitch = attributes.getBoolean(R.styleable.SettingItemView_sivswitch, true);
        int sivbg = attributes.getInt(R.styleable.SettingItemView_sivbg, Constantset.BG_TOP);

        set_item_iv = (ImageView) findViewById(R.id.set_item_iv);
        set_item_iv.setVisibility(sivswitch ? View.VISIBLE : View.GONE);
        TextView textView = (TextView) findViewById(R.id.set_item_tv);
        textView.setText(sivtext);
        switch (sivbg) {
            case Constantset.BG_TOP:
                setBackgroundResource(R.drawable.set_item_top_selector);
                break;
            case Constantset.BG_MIDDLE:
                setBackgroundResource(R.drawable.set_item_mid_selector);
                break;
            case Constantset.BG_BOTTOM:
                setBackgroundResource(R.drawable.set_item_bot_selector);
                break;

        }
        attributes.recycle();
    }

    public SettingItemView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public void setOpenSwitch(boolean update_state) {
        // TODO Auto-generated method stub
        if (update_state) {
           set_item_iv.setImageResource(R.drawable.on);
        }else{
            set_item_iv.setImageResource(R.drawable.off);
        }
    }

}
