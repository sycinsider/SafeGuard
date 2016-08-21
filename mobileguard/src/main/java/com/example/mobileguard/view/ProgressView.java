
package com.example.mobileguard.view;

import com.example.mobileguard.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * ClassName:StorageStatusView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月15日 上午11:09:06 <br/>
 * 
 * @author dell
 * @version
 */
public class ProgressView extends LinearLayout {

    private TextView left_tv;
    private TextView right_tv;
    private ProgressBar pbBar;

    public ProgressView(Context context) {
        this(context, null);

    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.storage_status_view, this);
        TypedArray attributes =
                context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        String ssvtext = attributes.getString(R.styleable.ProgressView_ssvtext);
        TextView title = (TextView) findViewById(R.id.storage_status_view_title);
        left_tv = (TextView) findViewById(R.id.storage_status_view_left_tv);
        right_tv = (TextView) findViewById(R.id.storage_status_view_right_tv);
        pbBar = (ProgressBar) findViewById(R.id.storage_status_view_pb);
        title.setText(ssvtext);
        attributes.recycle();
    }
    public void setLefttv(String ltv){
        left_tv.setText(ltv);
    }
    public void setRighttv(String rtv){
        right_tv.setText(rtv);
    }
    public void setPBmax(int max){
        pbBar.setMax(max);
    }
    public void setProgress(int progress){
        pbBar.setProgress(progress);
    }
}
