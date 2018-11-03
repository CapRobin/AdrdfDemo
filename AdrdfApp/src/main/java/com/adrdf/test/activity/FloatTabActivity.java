package com.adrdf.test.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.adrdf.test.R;
import com.adrdf.test.adapter.PullToRefreshListAdapter;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.asynctask.RdfTask;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskListListener;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfToastUtil;
import com.adrdf.base.view.listener.RdfOnScrollListener;
import com.adrdf.base.view.refresh.RdfPullToRefreshView;
import com.adrdf.base.view.sample.RdfScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：FloatTabActivity
 * Describe：
 * Date：2018-02-12 13:56:15
 * Author: CapRobin@yeah.net
 *
 */
public class FloatTabActivity extends RdfBaseActivity implements RdfPullToRefreshView.OnHeaderRefreshListener, RdfPullToRefreshView.OnFooterLoadListener {

    private MyApplication application;
    private List<Map<String, Object>> list = null;
    private RdfPullToRefreshView mAbPullToRefreshView = null;
    private RdfScrollListView mListView = null;
    private int currentPage = 1;
    private ArrayList<String> mPhotoList = new ArrayList<String>();
    private PullToRefreshListAdapter pullToRefreshListAdapter = null;
    private int total = 50000;
    private int pageSize = 15;

    private LinearLayout top_bar = null;
    private int headerHeight = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_tab);
        application = (MyApplication)this.getApplication();

        top_bar =  (LinearLayout)findViewById(R.id.top_bar);

        final  RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)top_bar.getLayoutParams();
        top_bar.setVisibility(View.INVISIBLE);

        // 获取ListView对象
        mAbPullToRefreshView = (RdfPullToRefreshView) this.findViewById(R.id.mPullRefreshView);

        for (int i = 0; i < 23; i++) {
            mPhotoList.add("http://www.amsoft.cn/demo/rand/" + i + ".jpg");
        }


        mListView = (RdfScrollListView) this.findViewById(R.id.mListView);
        mListView.setOnScrollListener(onScrollListener);
        mListView.addHeaderView(View.inflate(this,R.layout.activity_float_tab_list_header,null));
        mListView.setHeaderHeight(headerHeight);
        // ListView数据
        list = new ArrayList<Map<String, Object>>();

        // 设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);

        // 设置进度条的样式
        mAbPullToRefreshView.getDefaultHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        // 使用自定义的Adapter
        pullToRefreshListAdapter = new PullToRefreshListAdapter(this, list,200,200);
        mListView.setAdapter(pullToRefreshListAdapter);

        // item被点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        refreshTask();

    }

    @Override
    public void onFooterLoad(RdfPullToRefreshView view) {
        loadMoreTask();
    }

    @Override
    public void onHeaderRefresh(RdfPullToRefreshView view) {
        refreshTask();

    }

    private RdfOnScrollListener onScrollListener = new  RdfOnScrollListener(){
        @Override
        public void onScrollPosition(int position) {

        }

        @Override
        public void onScrollY(int scrollY) {

            if(scrollY == 0){
                //恢复位置
                top_bar.setVisibility(View.INVISIBLE);

            }else if(scrollY >= headerHeight){
                //到达顶部
                top_bar.setVisibility(View.VISIBLE);

            }else{
                //中间区域
                top_bar.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void refreshTask() {
        RdfLogUtil.prepareLog(PullToRefreshListActivity.class);
        RdfTask mAbTask = RdfTask.newInstance();
        final RdfTaskItem item = new RdfTaskItem();
        item.setListener(new RdfTaskListListener() {
            @Override
            public List<?> getList() {
                List<Map<String, Object>> newList = null;
                try {
                    Thread.sleep(1000);
                    currentPage = 1;
                    newList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;

                    for (int i = 0; i < pageSize; i++) {
                        map = new HashMap<String, Object>();
                        map.put("itemsIcon", mPhotoList.get(i));
                        map.put("itemsTitle", "item" + (i + 1));
                        map.put("itemsText", "item..." + (i + 1));
                        newList.add(map);

                    }
                } catch (Exception e) {
                }
                return newList;
            }

            @Override
            public void update(List<?> paramList) {

                //通知Dialog
                RdfLogUtil.d(PullToRefreshListActivity.class, "返回", true);
                List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
                list.clear();
                if (newList != null && newList.size() > 0) {
                    list.addAll(newList);
                    pullToRefreshListAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

        });

        mAbTask.execute(item);
    }

    public void loadMoreTask() {
        RdfTask mAbTask = RdfTask.newInstance();
        final RdfTaskItem item = new RdfTaskItem();
        item.setListener(new RdfTaskListListener() {

            @Override
            public void update(List<?> paramList) {
                List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
                if (newList != null && newList.size() > 0) {
                    list.addAll(newList);
                    pullToRefreshListAdapter.notifyDataSetChanged();
                    newList.clear();
                }
                mAbPullToRefreshView.onFooterLoadFinish();

            }

            @Override
            public List<?> getList() {
                List<Map<String, Object>> newList = null;
                try {
                    currentPage++;
                    Thread.sleep(1000);
                    newList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map = null;

                    for (int i = 0; i < pageSize; i++) {
                        map = new HashMap<String, Object>();
                        map.put("itemsIcon", mPhotoList.get(i));
                        map.put("itemsTitle", "item上拉"
                                + ((currentPage - 1) * pageSize + (i + 1)));
                        map.put("itemsText", "item上拉..."
                                + ((currentPage - 1) * pageSize + (i + 1)));
                        if ((list.size() + newList.size()) < total) {
                            newList.add(map);
                        }
                    }

                } catch (Exception e) {
                    currentPage--;
                    newList.clear();
                    RdfToastUtil.showToastInThread(
                            FloatTabActivity.this, e.getMessage());
                }
                return newList;
            };
        });

        mAbTask.execute(item);
    }

}
