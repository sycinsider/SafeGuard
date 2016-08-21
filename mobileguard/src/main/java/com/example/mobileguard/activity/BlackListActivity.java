package com.example.mobileguard.activity;

import java.util.ArrayList;

import com.example.mobileguard.R;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.bean.BlackBean;
import com.example.mobileguard.db.BlackDao;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DateSorter;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlackListActivity extends Activity implements OnItemClickListener, OnScrollListener {

    public static final int RQU_ADD_BLACK = 100;
    public static final String UPDATE_BLACK_NUM = "update_black_num";
    public static final String UPDATE_BLACK_TYPE = "update_black_type";
    public static final int RQU_UPDATE_BLACK = 200;
    public static final String ACTION = "action";
    public static final String UPDATE_BLACK_POSITION = "update_black_position";
    public static final String ADD_BLACK = "add_black";
    public static final String UPDATE_BLACK = "update_black";
    public static final String ADD_BLACK_NUM = "add_black_num";
    public static final String ADD_BLACK_TYPE = "add_black_type";
    private static final int SIZE = 20;
    private ListView lv;
    private ArrayList<BlackBean> datas;
    private BlackItemAdapter adapter;
    private boolean addNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        initView();
        initData();
        System.out.println("oncreate");
        initEvent();
    }

    private void initEvent() {
        lv.setOnItemClickListener(this);
        lv.setOnScrollListener(this);
    }

    private void initData() {
        loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            public void run() {
                SystemClock.sleep(10000);
                datas = BlackDao.getPageData(BlackListActivity.this, SIZE, 0);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loading.setVisibility(View.GONE);
                        adapter = new BlackItemAdapter();
                        lv.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    private void initView() {
        lv = (ListView) findViewById(R.id.black_list_lv);
        loading = (LinearLayout) findViewById(R.id.loading_now);
    }

    public void addBlack(View v) {
        Intent intent = new Intent(this, AddBlackActivity.class);
        intent.putExtra(ACTION, ADD_BLACK);
        startActivityForResult(intent, RQU_ADD_BLACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RQU_ADD_BLACK:
                if (resultCode == Activity.RESULT_OK) {
                    String number = data.getStringExtra(ADD_BLACK_NUM);
                    int type = data.getIntExtra(ADD_BLACK_TYPE, 0);
                    BlackBean bean = new BlackBean();
                    bean.number = number;
                    bean.type = type;
                    datas.add(bean);
                    addNew = true;
                    adapter.notifyDataSetChanged();
                    lv.setSelection(datas.size() - 1);
                }
                break;
            case RQU_UPDATE_BLACK:
                if (resultCode == Activity.RESULT_OK) {
                    String number = data.getStringExtra(UPDATE_BLACK_NUM);
                    int type = data.getIntExtra(UPDATE_BLACK_TYPE, 0);
                    int position = data.getIntExtra(UPDATE_BLACK_POSITION, 0);
                    BlackBean bean = datas.get(position);
                    bean.type = type;
                    adapter.notifyDataSetChanged();
                    lv.setSelection(position);
                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BlackBean bean = datas.get(position);
        Intent intent = new Intent(this, AddBlackActivity.class);
        intent.putExtra(ACTION, UPDATE_BLACK);
        intent.putExtra(UPDATE_BLACK_NUM, bean.number);
        intent.putExtra(UPDATE_BLACK_TYPE, bean.type);
        intent.putExtra(UPDATE_BLACK_POSITION, position);
        startActivityForResult(intent, RQU_UPDATE_BLACK);
    }

    class BlackItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (datas != null) {

                return datas.size();
            }
            return 0;
        }

        @Override
        public BlackBean getItem(int position) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(BlackListActivity.this, R.layout.black_list_item, null);
                holder = new ViewHolder();
                holder.number = (TextView) convertView.findViewById(R.id.black_list_item_number);
                holder.type = (TextView) convertView.findViewById(R.id.balck_list_item_type);
                holder.iv = (ImageView) convertView.findViewById(R.id.black_list_item_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final BlackBean bean = datas.get(position);
            if (bean != null) {
                holder.number.setText(bean.number);
                switch (bean.type) {
                    case BlackBean.TYPE_PHONE:
                        holder.type.setText("电话拦截");
                        break;
                    case BlackBean.TYPE_SMS:
                        holder.type.setText("短信拦截");
                        break;
                    case BlackBean.TYPE_ALL:
                        holder.type.setText("全部拦截");
                        break;

                }
                holder.iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean flag = BlackDao.del(BlackListActivity.this, bean.number);
                        if (flag) {
                            datas.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(BlackListActivity.this, "删除成功", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(BlackListActivity.this, "删除失败", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            TextView number;
            TextView type;
            ImageView iv;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private boolean isLoading = false;
    private boolean newDataHere = true;
    private LinearLayout loading;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        if (datas == null) {
            return;
        }
        if (isLoading) {
            return;
        }
        if (!newDataHere) {
            return;
        }
        if ((firstVisibleItem + visibleItemCount) == datas.size()) {
            new Thread(new Runnable() {
                public void run() {
                    SystemClock.sleep(1500);
                    isLoading = true;
                    ArrayList<BlackBean> newdatas =
                            BlackDao.getPageData(BlackListActivity.this, SIZE, datas.size());
                    if (newdatas.size() < SIZE) {
                        newDataHere = false;
                    }
                    datas.addAll(newdatas);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                        }
                    });
                }
            }).start();
        }

    }

}
