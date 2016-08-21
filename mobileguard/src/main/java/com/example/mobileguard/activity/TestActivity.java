package com.example.mobileguard.activity;

import java.util.Random;

import com.example.mobileguard.R;
import com.example.mobileguard.R.layout;
import com.example.mobileguard.db.BlackDao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        for (int number = 0; number < 100; number++) {
            int type = new Random().nextInt(3);
            BlackDao.add(this, "10000" + number, type);
        }
    }

}
