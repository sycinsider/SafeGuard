
package com.example.mobileguard.activity;

import com.example.mobileguard.service.OneKeyCleanService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * ClassName:WindowWidgetProvider <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月18日 下午7:28:53 <br/>
 * 
 * @author dell
 * @version
 */
public class WindowWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent = new Intent(context, OneKeyCleanService.class);
        context.startService(intent);
    }
}
