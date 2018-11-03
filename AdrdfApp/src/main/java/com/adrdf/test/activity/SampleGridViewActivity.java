package com.adrdf.test.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.adrdf.test.R;
import com.adrdf.test.adapter.SampleTextAdapter;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.app.model.RdfSampleItem;
import com.adrdf.base.util.RdfToastUtil;
import com.adrdf.base.view.sample.RdfSampleGridView;

import java.util.ArrayList;
/**
 * Copyright © CapRobin
 *
 * Name：SampleGridViewActivity
 * Describe：
 * Date：2018-02-06 11:59:08
 * Author: CapRobin@yeah.net
 *
 */
public class SampleGridViewActivity extends RdfBaseActivity {

    ArrayList<RdfSampleItem> paramList = new ArrayList<RdfSampleItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_gird_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_sample_grid_view);
        //toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(int i=0;i< 250 ;i++){
            paramList.add(new RdfSampleItem("item"+i,"ITEM "+i,null,String.valueOf(i)));
        }

        RdfSampleGridView gridView = (RdfSampleGridView)findViewById(R.id.grid_view);
        gridView.setPadding(10,10);
        gridView.setColumn(4);
        SampleTextAdapter sampleTextAdapter = new SampleTextAdapter(this,paramList);
        gridView.setAdapter(sampleTextAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RdfSampleItem item =  paramList.get(position);
                RdfToastUtil.showToast(SampleGridViewActivity.this,item.getText()+":"+item.getValue());
            }
        });


    }


}
