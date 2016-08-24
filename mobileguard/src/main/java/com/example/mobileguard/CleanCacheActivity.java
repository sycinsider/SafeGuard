package com.example.mobileguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobileguard.bean.CacheBean;

import java.lang.reflect.Method;
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
    private PackageManager pm;
    final IPackageStatsObserver.Stub mStatsObserver =
            new IPackageStatsObserver.Stub() {
                int i;
                public void onGetStatsCompleted(PackageStats stats,
                                                boolean succeeded) {
                    long cacheSize = stats.cacheSize;
                    System.out.println(cacheSize+"======="+i++);
                    ApplicationInfo info;
                    try {
                        info = pm.getApplicationInfo(stats.packageName, 0);
                        CacheBean bean = new CacheBean();
                        bean.icon = pm.getApplicationIcon(info);
                        bean.name = pm.getApplicationLabel(info).toString();
                        bean.pkgName = info.packageName;
                        bean.cacheSize = cacheSize;
                        task.upDataProgress(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
    private CacheAsyncTask task;
    private CleanCacheItemAdapter adapter;
    private int cacheNum;
    private int totalSize;
    private Button oneKeyClean;
    private boolean isFocused = false;
    private ClearCacheObserver mClearCacheObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        pm = getPackageManager();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFocused = true;
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFocused = false;
    }

    private void getCache(ApplicationInfo info) {
        try {
            Method method =
                    pm.getClass().getDeclaredMethod("getPackageSizeInfo",
                            String.class, IPackageStatsObserver.class);
            method.invoke(pm, info.packageName, mStatsObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        datas = new ArrayList<>();
        task = new CacheAsyncTask();
        task.execute();
    }

    class CacheAsyncTask extends AsyncTask<Void, CacheBean, Void> {
        private boolean isScanFinished;
        private int max;
        private int progress;
        private TranslateAnimation animation;
        @Override
        protected void onPreExecute() {
            isScanFinished = false;
            oneKeyClean.setEnabled(false);
            cleanCacheRunning.setVisibility(View.VISIBLE);
            cleanCacheFinish.setVisibility(View.GONE);
            adapter = new CleanCacheItemAdapter(CleanCacheActivity.this);
            cleanCacheLv.setAdapter(adapter);
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1);
            animation.setDuration(500);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            cleanCacheRunningLine.setAnimation(animation);
            animation.start();
            cacheNum = 0;
            totalSize = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<ApplicationInfo> list = pm.getInstalledApplications(0);
            max = list.size();
            for (ApplicationInfo info :
                    list) {
                if (!isFocused) {
                   break;
                }
                SystemClock.sleep(60);
                getCache(info);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(CacheBean... values) {
            progress++;
            CacheBean bean = values[0];
            if (bean.cacheSize > 0) {
                datas.add(0, bean);
                cacheNum++;
                totalSize+=bean.cacheSize;
            } else {
                datas.add(bean);
            }
            cleanCacheRunningIcon.setImageDrawable(bean.icon);
            cleanCacheRunningName.setText(bean.name);
            cleanCacheRunningSize.setText("缓存大小是:" + Formatter.formatFileSize(CleanCacheActivity.this, bean.cacheSize));
            cleanCacheRunningPb.setProgress(progress);
            cleanCacheRunningPb.setMax(max);
            adapter.notifyDataSetChanged();
            cleanCacheLv.setSelection(adapter.getCount()-1);
            if (isScanFinished) {
               onPostExecute(null);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            isScanFinished = true;
            animation.cancel();
            cleanCacheLv.smoothScrollToPosition(0);
            cleanCacheRunning.setVisibility(View.GONE);
            cleanCacheFinish.setVisibility(View.VISIBLE);
            if (totalSize>0) {
                cleanCacheFinishTv.setText("总共扫描到" +
                        cacheNum+"个,"+"缓存总大小是:"+Formatter.formatFileSize(CleanCacheActivity.this, totalSize));
            } else {
                cleanCacheFinishTv.setText("没有缓存可以清理");
            }
            oneKeyClean.setEnabled(true);
        }

        public void upDataProgress(CacheBean... values) {
            publishProgress(values);
        }
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
        oneKeyClean = (Button) findViewById(R.id.clean_cache_btn);
        oneKeyClean.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clean_cache_finish_btn:
                initData();
                break;
            case R.id.clean_cache_btn:
                if (mClearCacheObserver == null) {
                    mClearCacheObserver = new ClearCacheObserver();
                }
                try {
                    // 清除缓存
                    Method method = pm.getClass().getMethod("freeStorageAndNotify",
                            long.class, IPackageDataObserver.class);
                    method.invoke(pm, Long.MAX_VALUE, mClearCacheObserver);
                    // 重新获取一次数据
                    initData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    class ClearCacheObserver extends IPackageDataObserver.Stub {
        public void onRemoveCompleted(final String packageName,
                                      final boolean succeeded) {

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
            initializeViews((CacheBean) getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(final CacheBean bean, ViewHolder holder) {
            holder.itemCacheIcon.setImageDrawable(bean.icon);
            holder.itemCacheName.setText(bean.name);
            holder.itemCacheSize.setText(Formatter.formatFileSize(CleanCacheActivity.this, bean.cacheSize));
            if (bean.cacheSize > 0) {
                holder.itemCacheCleaner.setVisibility(View.VISIBLE);
            } else {
                holder.itemCacheCleaner.setVisibility(View.GONE);
            }
            holder.itemCacheCleaner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(
                            "android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + bean.pkgName));
                    startActivity(intent);
                }
            });
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

