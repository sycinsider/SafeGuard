
package com.example.mobileguard.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ClassName:MarQueenTextview <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月5日 下午10:26:16 <br/>
 * 
 * @author dell
 * @version
 */
public class MarQueenTextview extends TextView {

    public MarQueenTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSingleLine();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEllipsize(TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    public MarQueenTextview(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    @Override
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
       if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        
    }
    }
}
