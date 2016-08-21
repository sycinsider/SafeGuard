
package com.example.mobileguard.service;

import com.example.mobileguard.R;
import com.example.mobileguard.activity.WindowWidgetProvider;
import com.example.mobileguard.business.ProcessProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.widget.RemoteViews;

/**
 * ClassName:OneKeyCleanService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月18日 下午7:35:14 <br/>
 * 
 * @author dell
 * @version
 */
public class OneKeyCleanService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    ComponentName componentName =
                            new ComponentName(OneKeyCleanService.this, WindowWidgetProvider.class);
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                    int nums = ProcessProvider.getRunning(OneKeyCleanService.this);
                    long[] memSize = ProcessProvider.getMemSize(OneKeyCleanService.this);
                    views.setTextViewText(R.id.process_count, "正在运行进程:" + nums + "个");
                    views.setTextViewText(R.id.process_memory, "可用内存:" + Formatter.formatFileSize(OneKeyCleanService.this, memSize[0] )+ "个");
                    
                    Intent intent =new Intent(OneKeyCleanService.this, KillProcessService.class) ;
                    PendingIntent service = PendingIntent.getService(OneKeyCleanService.this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                      views.setOnClickPendingIntent(R.id.btn_clear, service);
                    AppWidgetManager.getInstance(OneKeyCleanService.this).updateAppWidget(componentName,
                            views);
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
