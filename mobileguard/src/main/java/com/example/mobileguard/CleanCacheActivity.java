package com.example.mobileguard;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobileguard.bean.CacheBean;

import java.util.ArrayList;
import java.util.List;


public class CleanCacheActivity extends Activity implements View.OnClickListener {

    private RelativeLayout cleanCacheRunning;
    private ImageView cleanCacheRunningIcon;
    private ImageView cleanCacheRunningLine;
    private ProgressBar cleanCacheRunningPb;
    private TextView cleanCacheRunningName;
    private TextView cleanCacheRunningSize;
    private RelativeLayout cleanCacheFinish;
    private TextView cleanCacheFinishTv;
    private ListView cleanCacheLv;
    private ArrayList<CacheBean> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);

        initView();
        initData();
        initCache();
    }

    private void initCache() {

    }

    private void initData() {
        datas= new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(Void... params) {
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> list = pm.getInstalledApplications(0);
                for (ApplicationInfo info :
                        list) {
                    CacheBean bean = new CacheBean();
                    bean.icon = pm.getApplicationIcon(info);
                    bean.name = pm.getApplicationLabel(info).toString();

                }
                int max = list.size();
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }

    private void initView() {
        cleanCacheRunning = (RelativeLayout) findViewById(R.id.clean_cache_running);
        cleanCacheRunningIcon = (ImageView) findViewById(R.id.clean_cache_running_icon);
        cleanCacheRunningLine = (ImageView) findViewById(R.id.clean_cache_running_line);
        cleanCacheRunningPb = (ProgressBar) findViewById(R.id.clean_cache_running_pb);
        cleanCacheRunningName = (TextView) findViewById(R.id.clean_cache_running_name);
        cleanCacheRunningSize = (TextView) findViewById(R.id.clean_cache_running_size);
        cleanCacheFinish = (RelativeLayout) findViewById(R.id.clean_cache_finish);
        findViewById(R.id.clean_cache_finish_btn).setOnClickListener(this);
        cleanCacheFinishTv = (TextView) findViewById(R.id.clean_cache_finish_tv);
        cleanCacheLv = (ListView) findViewById(R.id.clean_cache_lv);
        findViewById(R.id.clean_cache_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clean_cache_finish_btn:

                break;
            case R.id.clean_cache_btn:

                break;
        }
    }
    public class CleanCacheItemAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public CleanCacheItemAdapter(Context context) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public CacheBean getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.clean_cache_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            initializeViews((CacheBean)getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(CacheBean bean, ViewHolder holder) {

        }

        protected class ViewHolder {
            private ImageView itemCacheIcon;
            private ImageView itemCacheCleaner;
            private TextView itemCacheName;
            private TextView itemCacheSize;

            public ViewHolder(View view) {
                itemCacheIcon = (ImageView) view.findViewById(R.id.item_cache_icon);
                itemCacheCleaner = (ImageView) view.findViewById(R.id.item_cache_cleaner);
                itemCacheName = (TextView) view.findViewById(R.id.item_cache_name);
                itemCacheSize = (TextView) view.findViewById(R.id.item_cache_size);
            }
        }
    }
}

