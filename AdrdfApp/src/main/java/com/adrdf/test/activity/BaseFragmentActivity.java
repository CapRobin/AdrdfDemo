package com.adrdf.test.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.adrdf.test.R;
import com.adrdf.base.app.base.RdfBaseActivity;
/**
 * Copyright © CapRobin
 *
 * Name：BaseFragmentActivity
 * Describe：
 * Date：2018-02-23 11:54:37
 * Author: CapRobin@yeah.net
 *
 */
public class BaseFragmentActivity extends RdfBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_base);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
