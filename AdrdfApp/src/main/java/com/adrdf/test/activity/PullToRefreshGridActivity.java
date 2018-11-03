package com.adrdf.test.activity;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.adrdf.test.R;
import com.adrdf.test.adapter.PullToRefreshGridAdapter;
import com.adrdf.test.global.MyApplication;
import com.adrdf.test.model.ImageInfo;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.asynctask.RdfTask;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskListener;
import com.adrdf.base.util.RdfAppUtil;
import com.adrdf.base.util.RdfToastUtil;
import com.adrdf.base.util.RdfViewUtil;
import com.adrdf.base.view.refresh.RdfPullToRefreshView;

/**
 * Copyright © CapRobin
 *
 * Name：PullToRefreshGridActivity
 * Describe：
 * Date：2018-01-23 14:58:17
 * Author: CapRobin@yeah.net
 *
 */
public class PullToRefreshGridActivity extends RdfBaseActivity implements RdfPullToRefreshView.OnHeaderRefreshListener, RdfPullToRefreshView.OnFooterLoadListener {
	
	private int currentPage = 1;
	private MyApplication application;
	private ArrayList<ImageInfo> imageInfoList = null;
	private ArrayList<ImageInfo> imageInfoNewList = null;
	private RdfPullToRefreshView mAbPullToRefreshView;
	private GridView mGridView = null;
	private PullToRefreshGridAdapter pullToRefreshGridAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int total = 5000;
	private int pageSize = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pull_to_refresh_grid);

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
        
	    
	    for (int i = 0; i < 23; i++) {
            mPhotoList.add("http://www.amsoft.cn/demo/rand/" + i + ".jpg");
		}
	    
		application = (MyApplication) this.getApplication();
		//获取ListView对象
        mAbPullToRefreshView = (RdfPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mGridView = (GridView)this.findViewById(R.id.mGridView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        //mAbPullListView.getDefaultHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        //mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        DisplayMetrics dm = RdfAppUtil.getDisplayMetrics(this);
        int width = (dm.widthPixels-25)/3;
		mGridView.setColumnWidth(RdfViewUtil.scaleValue(this, width));
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(5);
		
		//Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
	    //得到一个LayoutAnimationController对象;
	    //LayoutAnimationController lac = new LayoutAnimationController(animation);
		//mGridView.setLayoutAnimation(lac);
		/*AlphaAnimation animationAlpha = new AlphaAnimation(0.0f,1.0f);  
	    //得到一个LayoutAnimationController对象;
	    LayoutAnimationController lac = new LayoutAnimationController(animationAlpha);
	    //设置控件显示的顺序;
	    lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
	    //设置控件显示间隔时间;
	    lac.setDelay(0.5f);
	    //为View设置LayoutAnimationController属性;
		mGridView.setLayoutAnimation(lac);*/

		mGridView.setNumColumns(GridView.AUTO_FIT);
		mGridView.setPadding(0, 0, 0, 0);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(10);
		// ListView数据
        imageInfoList = new ArrayList<ImageInfo>();
		// 使用自定义的Adapter
        pullToRefreshGridAdapter = new PullToRefreshGridAdapter(this, imageInfoList,width,width,imageLoader);
		mGridView.setAdapter(pullToRefreshGridAdapter);

        mAbPullToRefreshView.headerRefreshing();
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				RdfToastUtil.showToast(PullToRefreshGridActivity.this,""+position);
			}
    		
    	});

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
        //定义两种查询的事件
        final RdfTaskItem item = new RdfTaskItem();
        item.setListener(new RdfTaskListener() {

            @Override
            public void update() {
                imageInfoList.clear();
                if(imageInfoNewList!=null && imageInfoNewList.size()>0){
                    imageInfoList.addAll(imageInfoNewList);
                    pullToRefreshGridAdapter.notifyDataSetChanged();
                    imageInfoNewList.clear();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage = 1;
                    Thread.sleep(1000);
                    imageInfoNewList =  new ArrayList<ImageInfo>() ;
                    
                    for (int i = 0; i < pageSize; i++) {
                        final ImageInfo imageInfo = new ImageInfo();
                        if(i>=mPhotoList.size()){
                            imageInfo.setPath(mPhotoList.get(mPhotoList.size()-1));
                        }else{
                            imageInfo.setPath(mPhotoList.get(i));
                        }

                        imageInfoNewList.add(imageInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    RdfToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
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
                if(imageInfoNewList!=null && imageInfoNewList.size()>0){
                    imageInfoList.addAll(imageInfoNewList);
                    pullToRefreshGridAdapter.notifyDataSetChanged();
                    imageInfoNewList.clear();
                }
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage++;
                    Thread.sleep(1000);
                    imageInfoNewList =  new ArrayList<ImageInfo>() ;
                    for (int i = 0; i < pageSize; i++) {
                        final ImageInfo imageInfo = new ImageInfo();
                        imageInfo.setPath(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
                        if((imageInfoList.size()+imageInfoNewList.size()) < total){
                            imageInfoNewList.add(imageInfo);
                        }
                        
                    }
                } catch (Exception e) {
                    currentPage--;
                    imageInfoNewList.clear();
                    RdfToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
                }
               
          };
        });
        
        mAbTask.execute(item);
    }


}
