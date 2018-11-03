package com.adrdf.test.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.adrdf.test.R;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.util.RdfDialogUtil;
/**
 * Copyright © CapRobin
 *
 * Name：ButtonActivity
 * Describe：
 * Date：2018-02-14 16:55:00
 * Author: CapRobin@yeah.net
 *
 */
public class ButtonActivity extends RdfBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_button);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button orangeBtn = (Button)this.findViewById(R.id.orange_btn);
        orangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RdfDialogUtil.showProgressDialog(ButtonActivity.this,-1,"加载中...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RdfDialogUtil.removeDialog(ButtonActivity.this);
                    }
                },1000);
            }
        });

    }


}

