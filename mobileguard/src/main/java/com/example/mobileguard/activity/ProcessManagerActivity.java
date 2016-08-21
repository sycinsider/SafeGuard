package com.example.mobileguard.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.ProcessBean;
import com.example.mobileguard.business.ProcessProvider;
import com.example.mobileguard.service.AutoCleanService;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.ServiceUtils;
import com.example.mobileguard.utils.SpUtils;
import com.example.mobileguard.view.ProgressView;
import com.example.mobileguard.view.SettingItemView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ProcessManagerActivity extends Activity implements OnClickListener {
    private ArrayList<ProcessBean> datas = new ArrayList<ProcessBean>();
    private ArrayList<ProcessBean> userTask = new ArrayList<ProcessBean>();
    private ArrayList<ProcessBean> sysTask = new ArrayList<ProcessBean>();

    private ProgressView num;
    private ProgressView ram;
    private ImageView iv1;
    private ImageView iv2;
    private StickyListHeadersListView lv;
    private ActivityManager am;
    private PackageManager pm;
    private ProcessBaseAdapter adapter;
    private SlidingDrawer drawer;
    private SettingItemView showSys;
    private SettingItemView auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);
        initView();
        initProgress();
        playAnim();
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        pm = getPackageManager();
        initData();
        adapter = new ProcessBaseAdapter();
        lv.setAdapter(adapter);
        showSys.setOnClickListener(this);
        auto.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        boolean isShowSys = SpUtils.getBoolean(this, Constantset.SHOW_SYS_PRO);
        showSys.setOpenSwitch(isShowSys);
        boolean isAuto = ServiceUtils.isServiceRunning(this, AutoCleanService.class);
        auto.setOpenSwitch(isAuto);
    }

    @SuppressWarnings("deprecation")
    private void playAnim() {
        drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {

            @Override
            public void onDrawerOpened() {
                // TODO Auto-generated method stub
                iv1.setImageResource(R.drawable.drawer_arrow_down);
                iv2.setImageResource(R.drawable.drawer_arrow_down);
            }
        });
        drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

            @Override
            public void onDrawerClosed() {
                // TODO Auto-generated method stub
                iv1.setImageResource(R.drawable.drawer_arrow_up);
                iv2.setImageResource(R.drawable.drawer_arrow_up);
            }
        });
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv1, "alpha", 0.2f, 1.0f);
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        animator1.setRepeatMode(ObjectAnimator.RESTART);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(iv2, "alpha", 1.0f, 0.2f);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);
        animator2.setRepeatMode(ObjectAnimator.RESTART);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2);
        set.setDuration(750);
        set.start();
    }

    private void initData() {
        datas.clear();
        userTask.clear();
        sysTask.clear();
        List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : processes) {
            ProcessBean bean = new ProcessBean();
            String processName = info.processName;
            bean.pkgName = processName;
            bean.proName = processName;
            try {
                bean.icon = pm.getApplicationIcon(processName);
            } catch (Exception e) {
                bean.icon = getResources().getDrawable(R.drawable.ic_default);
                e.printStackTrace();
            }
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(processName, 0);
                bean.name = pm.getApplicationLabel(applicationInfo).toString();
                int flag = applicationInfo.flags;
                if ((flag & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    bean.isSystem = true;
                } else {
                    bean.isSystem = false;
                }
            } catch (Exception e) {
                bean.name = processName;
                bean.isSystem = true;
                e.printStackTrace();
            }
            int pid = info.pid;
            MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[] {pid});
            int totalPss = processMemoryInfo[0].getTotalPss();
            bean.size = totalPss * 1024;
            datas.add(bean);
        }
        for (ProcessBean bean : datas) {
            if (bean.isSystem == true) {
                sysTask.add(bean);
            } else {
                userTask.add(bean);
            }
        }
        datas.clear();
        datas.addAll(userTask);
        datas.addAll(sysTask);
    }

    private void initProgress() {
        long[] memSize = ProcessProvider.getMemSize(this);
        String usedMem = Formatter.formatFileSize(this, memSize[1]);
        String availMem = Formatter.formatFileSize(this, memSize[0]);
        int totals = ProcessProvider.getProcesses(this);
        int running = ProcessProvider.getRunning(this);
        num.setLefttv("正在运行:" + running + "个");
        num.setRighttv("可以运行:" + totals + "个");
        num.setPBmax(totals);
        num.setProgress(running);
        ram.setLefttv("已用内存:" + usedMem);
        ram.setRighttv("可用内存:" + availMem);
        ram.setProgress((int) (memSize[1] * 100f / memSize[2] + 0.5f));
    }

    private void initView() {
        drawer = (SlidingDrawer) findViewById(R.id.drawer);
        num = (ProgressView) findViewById(R.id.progress_manager_view_num);
        ram = (ProgressView) findViewById(R.id.progress_manager_view_ram);
        iv1 = (ImageView) findViewById(R.id.progress_manager_view_iv1);
        iv2 = (ImageView) findViewById(R.id.progress_manager_view_iv2);
        lv = (StickyListHeadersListView) findViewById(R.id.progress_manager_view_lv);
        showSys = (SettingItemView) findViewById(R.id.progress_manager_view_showsys);
        auto = (SettingItemView) findViewById(R.id.progress_manager_view_auto);
    }

    class ProcessBaseAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        @Override
        public int getCount() {
            if (datas != null) {
                boolean isShowSys =
                        SpUtils.getBoolean(ProcessManagerActivity.this, Constantset.SHOW_SYS_PRO);
                if (isShowSys) {
                    return datas.size();
                } else {
                    return userTask.size();
                }
            }
            return 0;
        }

        @Override
        public ProcessBean getItem(int position) {
            if (datas != null) {
                boolean isShowSys =
                        SpUtils.getBoolean(ProcessManagerActivity.this, Constantset.SHOW_SYS_PRO);
                if (isShowSys) {
                    return datas.get(position);

                } else {
                    return userTask.get(position);
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(ProcessManagerActivity.this,
                        R.layout.progress_manager_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.progress_manager_item_iv);
                holder.name =
                        (TextView) convertView.findViewById(R.id.progress_manager_item_tv_name);
                holder.size =
                        (TextView) convertView.findViewById(R.id.progress_manager_item_tv_size);
                holder.cb = (CheckBox) convertView.findViewById(R.id.progress_manager_item_cb);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ProcessBean item = getItem(position);
            holder.icon.setImageDrawable(item.icon);;
            holder.name.setText(item.name);
            holder.size.setText(Formatter.formatFileSize(ProcessManagerActivity.this, item.size));
            if (item.pkgName.equals(ProcessManagerActivity.this.getPackageName())) {
                holder.cb.setVisibility(View.GONE);
            }
            holder.cb.setChecked(item.isChecked);

            holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isChecked = isChecked;
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView size;
            CheckBox cb;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = View.inflate(ProcessManagerActivity.this,
                        R.layout.app_manager_item_title, null);
                holder.title = (TextView) convertView.findViewById(R.id.item_head_title);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            ProcessBean item = getItem(position);
            if (item.isSystem) {
                holder.title.setText("系统进程:" + sysTask.size() + "个");
            } else {
                holder.title.setText("用户进程:" + userTask.size() + "个");
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            ProcessBean item = getItem(position);
            return item.isSystem ? 0 : 1;
        }

        class HeaderViewHolder {
            TextView title;
        }
    }

    public void cleanProcess(View v) {
        boolean isShowSys = SpUtils.getBoolean(this, Constantset.SHOW_SYS_PRO);
        if (isShowSys) {
            for (int i = 0; i < datas.size(); i++) {
                ProcessBean bean = datas.get(i);
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                if (bean.isChecked) {
                    am.killBackgroundProcesses(bean.pkgName);
                    datas.remove(bean);
                    userTask.remove(bean);
                    sysTask.remove(bean);
                    i--;
                }
            }
            for (ProcessBean bean : datas) {
                bean.isChecked = false;
            }
        } else {
            for (int i = 0; i < userTask.size(); i++) {
                ProcessBean bean = userTask.get(i);
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                if (bean.isChecked) {
                    am.killBackgroundProcesses(bean.pkgName);
                    datas.remove(bean);
                    userTask.remove(bean);
                    sysTask.remove(bean);
                    i--;
                }
            }
        }
        adapter.notifyDataSetChanged();
        initData();
        initProgress();
    }

    public void checkAll(View v) {
        boolean isShowSys = SpUtils.getBoolean(this, Constantset.SHOW_SYS_PRO);
        if (isShowSys) {
            for (ProcessBean bean : datas) {
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                bean.isChecked = true;
            }
        } else {
            for (ProcessBean bean : userTask) {
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                bean.isChecked = true;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void reverse(View v) {
        boolean isShowSys = SpUtils.getBoolean(this, Constantset.SHOW_SYS_PRO);
        if (isShowSys) {
            for (ProcessBean bean : datas) {
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                boolean flag = bean.isChecked;
                bean.isChecked = !flag;
            }
        } else {
            for (ProcessBean bean : userTask) {
                if (bean.pkgName.equals(getPackageName())) {
                    continue;
                }
                boolean flag = bean.isChecked;
                bean.isChecked = !flag;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_manager_view_showsys:
                boolean isShowSys = SpUtils.getBoolean(this, Constantset.SHOW_SYS_PRO);
                SpUtils.putBoolean(this, Constantset.SHOW_SYS_PRO, !isShowSys);
                showSys.setOpenSwitch(!isShowSys);
                adapter.notifyDataSetChanged();
                break;
            case R.id.progress_manager_view_auto:
                boolean isAuto = ServiceUtils.isServiceRunning(this, AutoCleanService.class);
                Intent intent = new Intent(this, AutoCleanService.class);
                if (isAuto) {
                    stopService(intent);
                    auto.setOpenSwitch(!isAuto);
                } else {
                    startService(intent);
                    auto.setOpenSwitch(!isAuto);
                }

                break;

        }
    }
}
