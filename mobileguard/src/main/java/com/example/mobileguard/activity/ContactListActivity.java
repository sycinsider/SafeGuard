package com.example.mobileguard.activity;

import java.util.ArrayList;

import com.example.mobileguard.R;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.adapter.ContactListAdapter;
import com.example.mobileguard.bean.Contactbean;
import com.example.mobileguard.db.ContactDao;
import com.example.mobileguard.utils.Constantset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DateSorter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ContactListActivity extends Activity implements OnItemClickListener {

    private ListView contact_lv;
    private ArrayList<Contactbean> dates;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        contact_lv = (ListView) findViewById(R.id.activity_contact_lv);
        pb = (ProgressBar) findViewById(R.id.contact_list_pb);
        initData();
        initEvent();
    }

    private void initEvent() {
        contact_lv.setOnItemClickListener(this);
    }

    private void initData() {
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            public void run() {
                SystemClock.sleep(1000);
                dates = ContactDao.getAllContact(ContactListActivity.this);
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setVisibility(View.GONE);
                        ContactListAdapter adapter =
                                new ContactListAdapter(ContactListActivity.this, dates);
                        contact_lv.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contactbean bean = dates.get(position);
        Intent intent = new Intent();
        intent.putExtra(Constantset.SELECTED_NUM, bean.number);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
