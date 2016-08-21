package com.example.mobileguard.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.AppBean;
import com.example.mobileguard.view.ProgressView;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class APPManagerActivity extends Activity
        implements OnScrollListener, OnItemClickListener, OnClickListener {
    private ArrayList<AppBean> datas = new ArrayList<AppBean>();
    private ArrayList<AppBean> userApp = new ArrayList<AppBean>();
    private ArrayList<AppBean> systemApp = new ArrayList<AppBean>();
    private ProgressView rom;
    private ProgressView sd;
    private ListView lv;
    private TextView title;
    private AppManagerAdapter adapter;
    private PackageManager pm;
    private List<ApplicationInfo> applicationInfo;
    private LinearLayout loading;
    private AppBean item;
    private PopupWindow popwindow;
    private TextView info;
    private TextView open;
    private TextView uninstall;
    private TextView share;
    private Intent intentOpen;
    private IntentFilter filter;
    private UninstallReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initView();
        initRom();
        initSD();
        initData();
        initEvent();
    }

    private void initEvent() {
        lv.setOnScrollListener(this);
        lv.setOnItemClickListener(this);
    }

    private void initData() {
        title.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        pm = getPackageManager();
        applicationInfo = pm.getInstalledApplications(0);
        new Thread(new Runnable() {
            public void run() {
                SystemClock.sleep(3000);
                initInfo();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        title.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        adapter = new AppManagerAdapter();
                        lv.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private void initInfo() {

        for (ApplicationInfo info : applicationInfo) {
            AppBean bean = new AppBean();
            // 获取应用的名称
            CharSequence label = pm.getApplicationLabel(info);
            bean.appName = label.toString();
            // 应用的报名
            bean.pkgName = info.packageName;
            // 获取应用的图标
            Drawable applicationIcon = pm.getApplicationIcon(info);
            bean.icon = applicationIcon;
            // 获取应用的安装路径
            String sourceDir = info.sourceDir;
            File file = new File(sourceDir);
            if (sourceDir.startsWith("/mnt/asec")) {
                bean.isOnSD = true;
            } else {
                bean.isOnSD = false;
            }
            int flag = info.flags;
            if ((flag
                    & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == android.content.pm.ApplicationInfo.FLAG_SYSTEM) {
                bean.isSystem = true;
            } else {
                bean.isSystem = false;
            }
            // 获取应用的大小
            long size = file.length();
            String fileSize = Formatter.formatFileSize(this, size);
            bean.appSize = fileSize;
            datas.add(bean);
        }
        for (AppBean bean : datas) {
            if (bean.isSystem) {
                systemApp.add(bean);
            } else {
                userApp.add(bean);
            }
        }
        datas.clear();
        datas.addAll(userApp);
        datas.addAll(systemApp);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        receiver = new UninstallReceiver();
        filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas == null ? 2 : datas.size() + 2;
        }

        @Override
        public AppBean getItem(int position) {
            if (position == 0) {
                return null;
            }
            if (position == userApp.size() + 1) {
                return null;
            }
            if (position <= userApp.size()) {
                return datas.get(position - 1);
            }

            return datas.get(position - 2);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView title = (TextView) View.inflate(APPManagerActivity.this,
                        R.layout.app_manager_item_title, null);
                title.setText("用户程序:" + userApp.size() + "个");
                return title;
            } else if (position == userApp.size() + 1) {
                TextView title = (TextView) View.inflate(APPManagerActivity.this,
                        R.layout.app_manager_item_title, null);
                title.setText("系统程序:" + systemApp.size() + "个");
                return title;
            } else {
                ViewHolder holder;
                if (convertView == null || convertView instanceof TextView) {
                    convertView = View.inflate(APPManagerActivity.this,
                            R.layout.activity_app_manager_item, null);
                    holder = new ViewHolder();
                    holder.icon = (ImageView) convertView.findViewById(R.id.app_manager_item_iv);
                    holder.name =
                            (TextView) convertView.findViewById(R.id.app_manager_item_tv_name);
                    holder.path =
                            (TextView) convertView.findViewById(R.id.app_manager_item_tv_path);
                    holder.size =
                            (TextView) convertView.findViewById(R.id.app_manager_item_tv_size);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                AppBean bean = getItem(position);
                holder.icon.setImageDrawable(bean.icon);
                holder.name.setText(bean.appName);
                holder.size.setText(bean.appSize);
                if (bean.isOnSD) {
                    holder.path.setText("SD卡安装");
                } else {
                    holder.path.setText("内存安装");
                }
                return convertView;
            }
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView path;
            TextView size;
        }

    }

    private void initView() {
        loading = (LinearLayout) findViewById(R.id.loading_now);
        rom = (ProgressView) findViewById(R.id.storage_status_view_rom);
        sd = (ProgressView) findViewById(R.id.storage_status_view_sd);
        lv = (ListView) findViewById(R.id.storage_status_view_lv);
        title = (TextView) findViewById(R.id.storage_status_view_lv_title);
    }

    private void initSD() {
        File sdFile = Environment.getExternalStorageDirectory();
        long totalSpace = sdFile.getTotalSpace();
        long freeSpace = sdFile.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        sd.setLefttv(Formatter.formatFileSize(this, usedSpace));
        sd.setRighttv(Formatter.formatFileSize(this, freeSpace));
        sd.setProgress((int) (usedSpace * 100f / totalSpace));
    }

    private void initRom() {
        File romFile = Environment.getDataDirectory();
        long totalSpace = romFile.getTotalSpace();
        long freeSpace = romFile.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        rom.setLefttv(Formatter.formatFileSize(this, usedSpace));
        rom.setRighttv(Formatter.formatFileSize(this, freeSpace));
        rom.setProgress((int) (usedSpace * 100f / totalSpace));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        if (firstVisibleItem <= userApp.size()) {
            title.setText("用户程序:" + userApp.size() + "个");
        } else {
            title.setText("系统程序:" + systemApp.size() + "个");
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0 || position == userApp.size() + 1) {
            return;
        }
        item = adapter.getItem(position);
        View inflate = View.inflate(this, R.layout.popupwindow, null);
        popwindow = new PopupWindow(inflate, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popwindow.setFocusable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setBackgroundDrawable(new ColorDrawable());
        popwindow.setAnimationStyle(R.style.PopupAnimationDialog);
        popwindow.showAsDropDown(view, 100, -view.getHeight());
        info = (TextView) inflate.findViewById(R.id.popupwindow_info);
        open = (TextView) inflate.findViewById(R.id.popupwindow_open);
        uninstall = (TextView) inflate.findViewById(R.id.popupwindow_uninstall);
        share = (TextView) inflate.findViewById(R.id.popupwindow_share);
        if (item.isSystem) {
            uninstall.setVisibility(View.GONE);
        }
        intentOpen = pm.getLaunchIntentForPackage(item.pkgName);
        if (intentOpen == null) {
            open.setVisibility(View.GONE);
        }
        info.setOnClickListener(this);
        open.setOnClickListener(this);
        uninstall.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    class UninstallReceiver extends BroadcastReceiver {

        private String pgk;

        @Override
        public void onReceive(Context context, Intent intent) {
            String string = intent.getDataString();
            pgk = string.replace("package:", "");
            Iterator<AppBean> iterator = datas.iterator();
            while (iterator.hasNext()) {
                AppBean bean = iterator.next();
                if (pgk.equals(bean.pkgName)) {
                    iterator.remove();
                }
            }
            Iterator<AppBean> iterator2 = userApp.iterator();
            while (iterator2.hasNext()) {
                AppBean bean = iterator2.next();
                if (pgk.equals(bean.pkgName)) {
                    iterator2.remove();
                }
            }
            adapter.notifyDataSetChanged();
            title.setText("用户程序:" + userApp.size() + "个");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popupwindow_info:
                Intent intentInfo = new Intent();
                intentInfo.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intentInfo.addCategory("android.intent.category.DEFAULT");
                intentInfo.setData(Uri.parse("package:" + item.pkgName));
                startActivity(intentInfo);
                popwindow.dismiss();
                break;
            case R.id.popupwindow_open:
                startActivity(intentOpen);
                popwindow.dismiss();
                break;
            case R.id.popupwindow_uninstall:

                Intent intentUn =
                        new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + item.pkgName));
                startActivity(intentUn);

                popwindow.dismiss();
                break;
            case R.id.popupwindow_share:
                Intent intentShare = new Intent("android.intent.action.SEND");
                intentShare.addCategory("android.intent.category.DEFAULT");
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, "This is my Share:" + item.appName);
                startActivity(Intent.createChooser(intentShare, "分享到"));
                popwindow.dismiss();
                break;

        }

    }

}
