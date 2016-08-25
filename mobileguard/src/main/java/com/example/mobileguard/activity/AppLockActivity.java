package com.example.mobileguard.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.AppLockBean;
import com.example.mobileguard.view.SegmentView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AppLockActivity extends Activity {
    private ArrayList<AppLockBean> lockApps = new ArrayList<AppLockBean>();
    private ArrayList<AppLockBean> unLockApps = new ArrayList<AppLockBean>();
    private SegmentView activityAppLockSv;
    private TextView activityAppLockTitle;
    private ListView activityAppLockUnlocklv;
    private ListView activityAppLockLocklv;
    private LinearLayout activityAppLockLoading;
    private LockTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        initView();
        initData();
    }

    private void initData() {
        task = new LockTask();
        task.execute();
    }

    class LockTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            activityAppLockLoading.setVisibility(View.VISIBLE);
            activityAppLockTitle.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void...params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
        }
    }

    private void initView() {
        activityAppLockSv = (SegmentView) findViewById(R.id.activity_app_lock_sv);
        activityAppLockTitle = (TextView) findViewById(R.id.activity_app_lock_title);
        activityAppLockUnlocklv = (ListView) findViewById(R.id.activity_app_lock_unlocklv);
        activityAppLockLocklv = (ListView) findViewById(R.id.activity_app_lock_locklv);
        activityAppLockLoading = (LinearLayout) findViewById(R.id.loading_now);
    }

    private boolean isShowUnLock = true;

    public class AppLockItemAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public AppLockItemAdapter(Context context) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (isShowUnLock) {
                return unLockApps == null ? 0 : unLockApps.size();
            } else {
                return lockApps == null ? 0 : lockApps.size();
            }
        }

        @Override
        public AppLockBean getItem(int position) {
            if (isShowUnLock) {
                if (unLockApps != null) {

                    return unLockApps.get(position);
                }
            } else {
                if (lockApps != null) {

                    return lockApps.get(position);
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.app_lock_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            initializeViews((AppLockBean) getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(AppLockBean bean, ViewHolder holder) {
            holder.itemLockIcon.setImageDrawable(bean.icon);
            holder.itemLockName.setText(bean.name);
            if (isShowUnLock) {
                holder.itemLockLock.setImageResource(R.drawable.lock_selector);
            } else {
                holder.itemLockLock.setImageResource(R.drawable.unlock_selector);
            }
        }

        protected class ViewHolder {
            private ImageView itemLockIcon;
            private ImageView itemLockLock;
            private TextView itemLockName;

            public ViewHolder(View view) {
                itemLockIcon = (ImageView) view.findViewById(R.id.item_lock_icon);
                itemLockLock = (ImageView) view.findViewById(R.id.item_lock_lock);
                itemLockName = (TextView) view.findViewById(R.id.item_lock_name);
            }
        }
    }

}
