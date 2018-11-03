package com.adrdf.test.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.test.fragment.TestFragment;
import com.adrdf.test.global.MyApplication;
import com.adrdf.base.app.adapter.RdfFragmentPagerAdapter;
import com.adrdf.base.app.base.RdfBaseActivity;
import com.adrdf.base.view.sample.RdfViewPager;

import java.util.ArrayList;
/**
 * Copyright © CapRobin
 *
 * Name：BottomBarActivity
 * Describe：
 * Date：2018-02-23 15:54:48
 * Author: CapRobin@yeah.net
 *
 */
public class BottomBarActivity extends RdfBaseActivity {

    private MyApplication application;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RdfViewPager viewPager = null;


    private String[] titleList = null;
    private int[] icons = new int[]{
            R.drawable.menu1_n,
            R.drawable.menu2_n,
            R.drawable.menu3_n,
            R.drawable.menu4_n
    };

    private int[] icons_press = new int[]{
            R.drawable.menu1_f,
            R.drawable.menu2_f,
            R.drawable.menu3_f,
            R.drawable.menu4_f
    };


    private TestFragment fragment1;
    private TestFragment fragment2;
    private TestFragment fragment3;
    private TestFragment fragment4;

    private ArrayList<Fragment> fragmentList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(R.string.title_bottom_bar);
        toolbar.setContentInsetsRelative(0, 0);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        application = (MyApplication) this.getApplication();

        initFragment();

    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()==0){
            finish();
        }else{
            viewPager.setCurrentItem(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    public void initFragment(){
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        viewPager = (RdfViewPager)findViewById(R.id.view_paper);

        viewPager.setOffscreenPageLimit(4);

        fragmentList = new ArrayList<Fragment>();

        fragment1 = new TestFragment();
        fragment2 = new TestFragment();
        fragment3 = new TestFragment();
        fragment4 = new TestFragment();

        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        titleList = new String[]{
               "页面1","页面2","页面3","页面4"
        };

        RdfFragmentPagerAdapter adapter = new RdfFragmentPagerAdapter(getSupportFragmentManager(),titleList,fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //禁止滑动
        //viewPager.setPagingEnabled(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                toolbar.setTitle(titleList[position]);

                for(int i=0;i<titleList.length;i++){
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    View view = tab.getCustomView();
                    ImageView img = (ImageView)view.findViewById(R.id.tab_icon);

                    if(position==i){
                        img.setImageResource(icons_press[i]);
                    }else{
                        img.setImageResource(icons[i]);
                    }


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //为TabLayout添加tab名称
        for(int i=0;i<titleList.length;i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        viewPager.setCurrentItem(0);

    }

    /**
     * 添加getTabView的方法，来进行自定义Tab的布局View
     * @param position
     * @return
     */
    public View getTabView(int position){
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = null;

        view = mInflater.inflate(R.layout.item_bottom_tab,null);
        TextView tv = (TextView)view.findViewById(R.id.textView);
        tv.setText(titleList[position]);
        ImageView img = (ImageView)view.findViewById(R.id.tab_icon);
        if(position == 0){
            img.setImageResource(icons_press[position]);
        }else{
            img.setImageResource(icons[position]);
        }
        return view;
    }

    public void selectPager(int index) {
        viewPager.setCurrentItem(index);
    }

}
