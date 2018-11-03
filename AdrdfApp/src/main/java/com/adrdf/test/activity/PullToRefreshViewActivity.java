package com.adrdf.test.activity;

import java.util.Random;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.asynctask.RdfTask;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskListener;
import com.adrdf.base.util.RdfDialogUtil;
import com.adrdf.base.view.refresh.RdfPullToRefreshView;

/**
 * Copyright © CapRobin
 *
 * Name：PullToRefreshViewActivity
 * Describe：
 * Date：2018-02-27 15:58:42
 * Author: CapRobin@yeah.net
 *
 */
public class PullToRefreshViewActivity extends RdfBaseActivity implements RdfPullToRefreshView.OnHeaderRefreshListener, RdfPullToRefreshView.OnFooterLoadListener {
	
	private MyApplication application;
	private RdfPullToRefreshView mAbPullToRefreshView = null;
	private TextView mTextView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_to_refresh_view);
        application = (MyApplication)this.getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_pull_to_refresh);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
	    //获取ListView对象
        mAbPullToRefreshView = (RdfPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mTextView = (TextView)this.findViewById(R.id.mTextView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));

        refreshTask();
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	@Override
    public void onFooterLoad(RdfPullToRefreshView view) {
	    loadMoreTask();
    }
	
    @Override
    public void onHeaderRefresh(RdfPullToRefreshView view) {
        refreshTask();
    }
	
	
	public void refreshTask(){
		RdfTask mAbTask = RdfTask.newInstance();
        final RdfTaskItem item = new RdfTaskItem();
        item.setListener(new RdfTaskListener() {

            @Override
            public void update() {
            	RdfDialogUtil.removeDialog(PullToRefreshViewActivity.this);
                mTextView.setText("This is "+new Random().nextInt(100));
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void get() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
          };
        });
        
        mAbTask.execute(item);
    }
	
	public void loadMoreTask(){
		RdfTask mAbTask = RdfTask.newInstance();
        final RdfTaskItem item = new RdfTaskItem();
        item.setListener(new RdfTaskListener() {

            @Override
            public void update() {
            	RdfDialogUtil.removeDialog(PullToRefreshViewActivity.this);
                mTextView.append("+"+new Random().nextInt(100));
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void get() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
          };
        });
        
        mAbTask.execute(item);
    }
   
}


