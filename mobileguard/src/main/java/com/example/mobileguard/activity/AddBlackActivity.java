package com.example.mobileguard.activity;

import com.example.mobileguard.R;
import com.example.mobileguard.R.id;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.bean.BlackBean;
import com.example.mobileguard.db.BlackDao;
import com.example.mobileguard.utils.Constantset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class AddBlackActivity extends Activity implements OnClickListener {

    private EditText et;
    private RadioGroup rg;
    private Button save;
    private Button cancel;
    private TextView title;
    private String action;
    private int position;
    private RadioButton rb_sms;
    private RadioButton rb_phone;
    private RadioButton rb_all;
    private int usedtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        action = intent.getStringExtra(BlackListActivity.ACTION);
        if (action.equals(BlackListActivity.UPDATE_BLACK)) {
            String number = intent.getStringExtra(BlackListActivity.UPDATE_BLACK_NUM);
            usedtype = intent.getIntExtra(BlackListActivity.UPDATE_BLACK_TYPE, -1);
            position = intent.getIntExtra(BlackListActivity.UPDATE_BLACK_POSITION, 0);
            title.setText("更新黑名单");
            save.setText("更新");
            et.setText(number);
            et.setEnabled(false);
            switch (usedtype) {
                case BlackBean.TYPE_SMS:
                    rb_sms.setChecked(true);
                    break;
                case BlackBean.TYPE_PHONE:
                    rb_phone.setChecked(true);
                    break;
                case BlackBean.TYPE_ALL:
                    rb_all.setChecked(true);
                    break;
            }

        }
    }

    private void initView() {
        et = (EditText) findViewById(R.id.add_black_et);
        rg = (RadioGroup) findViewById(R.id.add_black_rg);
        save = (Button) findViewById(R.id.add_black_save);
        cancel = (Button) findViewById(R.id.add_black_cancel);
        title = (TextView) findViewById(R.id.add_black_title);
        rb_sms = (RadioButton) findViewById(R.id.add_black_type_sms);
        rb_phone = (RadioButton) findViewById(R.id.add_black_type_phone);
        rb_all = (RadioButton) findViewById(R.id.add_black_type_all);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_black_cancel:
                finish();
                break;
            case R.id.add_black_save:
                String number = et.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(this, "号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int radioButtonId = rg.getCheckedRadioButtonId();
                    if (radioButtonId == -1) {
                        Toast.makeText(this, "类型不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int type = -1;
                    switch (radioButtonId) {
                        case R.id.add_black_type_sms:
                            type = BlackBean.TYPE_SMS;
                            break;
                        case R.id.add_black_type_phone:
                            type = BlackBean.TYPE_PHONE;
                            break;
                        case R.id.add_black_type_all:
                            type = BlackBean.TYPE_ALL;
                            break;
                    }
                    if (action.equals(BlackListActivity.UPDATE_BLACK)) {
                        update(number, type);
                    } else if (action.equals(BlackListActivity.ADD_BLACK)) {
                        add(number, type);

                    }
                }
                break;

        }

    }

    private void update(String number, int type) {
        if (usedtype == type) {
            Toast.makeText(this, "请修改类型", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean update = BlackDao.update(this, number, type);
        if (update) {
            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(BlackListActivity.UPDATE_BLACK_NUM, number);
            intent.putExtra(BlackListActivity.UPDATE_BLACK_TYPE, type);
            intent.putExtra(BlackListActivity.UPDATE_BLACK_POSITION, position);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void add(String number, int type) {
        boolean add = BlackDao.add(this, number, type);
        if (add) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(BlackListActivity.ADD_BLACK_NUM, number);
            intent.putExtra(BlackListActivity.ADD_BLACK_TYPE, type);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
