package com.example.mobileguard.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.Trafficbean;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficActivity extends Activity {

    private ListView lv;
    private ArrayList<Trafficbean> datas = new ArrayList<Trafficbean>();
    private PackageManager pm;
    private Trafficbean bean;
    private ListViewAdapter adapter;
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        lv = (ListView) findViewById(R.id.activity_traffic_lv);
        loading = (LinearLayout) findViewById(R.id.loading_now);
        init();

    }

    private void init() {
        loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                initData();
                runOnUiThread(new Runnable() {
                    public void run() {
                        loading.setVisibility(View.GONE);
                        adapter = new ListViewAdapter();
                        lv.setAdapter(adapter);

                    }
                });
            }
        }).start();
    }

    private void initData() {
        pm = getPackageManager();
        List<ApplicationInfo> applications = pm.getInstalledApplications(0);
        for (ApplicationInfo info : applications) {
            bean = new Trafficbean();
            Drawable icon = pm.getApplicationIcon(info);
            String name = pm.getApplicationLabel(info).toString();
            int uid = info.uid;

            // long rxBytes = TrafficStats.getUidRxBytes(uid);
            // long txBytes = TrafficStats.getUidTxBytes(uid);
            long rxBytes = getReceiver(uid);
            long txBytes = getSend(uid);
            if (rxBytes == 0 && txBytes == 0) {
                continue;
            }

            bean.icon = icon;
            bean.name = name;
            bean.receive = rxBytes;
            bean.send = txBytes;
            datas.add(bean);
        }
    }

    private long getSend(int uid) {
        BufferedReader br = null;
        File file = new File("/proc/uid_stat/" + uid + "/tcp_snd");
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            return Long.valueOf(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getReceiver(int uid) {
        BufferedReader br = null;
        File file = new File("/proc/uid_stat/" + uid + "/tcp_rcv");
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            return Long.valueOf(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return datas == null ? 0 : datas.size();
        }

        @Override
        public Trafficbean getItem(int position) {
            if (datas != null) {
                return datas.get(position);
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
                holder = new ViewHolder();
                convertView =
                        View.inflate(TrafficActivity.this, R.layout.activity_traffic_item, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.activity_traffic_item_iv);
                holder.name =
                        (TextView) convertView.findViewById(R.id.activity_traffic_item_tv_name);
                holder.send =
                        (TextView) convertView.findViewById(R.id.activity_traffic_item_tv_send);
                holder.receiver =
                        (TextView) convertView.findViewById(R.id.activity_traffic_item_tv_receiver);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Trafficbean bean = datas.get(position);
            holder.iv.setImageDrawable(bean.icon);
            holder.name.setText(bean.name);
            holder.send.setText("发送:" + Formatter.formatFileSize(TrafficActivity.this, bean.send));
            holder.receiver
                    .setText("接收:" + Formatter.formatFileSize(TrafficActivity.this, bean.receive));
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView name;
            TextView send;
            TextView receiver;
        }

    }
}
