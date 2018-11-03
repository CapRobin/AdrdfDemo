package com.adrdf.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.adrdf.test.R;
import com.adrdf.base.app.base.RdfBaseActivity;
/**
 * Copyright © CapRobin
 *
 * Name：MainActivity
 * Describe：
 * Date：2018-01-12 18:58:00
 * Author: CapRobin@yeah.net
 *
 */
public class MainActivity extends RdfBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.app_name);
        toolbar.setContentInsetsRelative(0,0);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        Button startBtn = (Button) findViewById(R.id.start_btn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FuncListActivity.class);
                startActivity(intent);
            }
        });


    }
}
