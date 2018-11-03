package com.adrdf.base.view.tabpager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adrdf.library.R;
import com.adrdf.base.app.adapter.RdfFragmentPagerAdapter;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.view.sample.RdfViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTabPagerView
 * Describe：动的tab,tab不固定超出后可滑动. 用TabLayout+ViewPager实现
 * Date：2017-08-15 18:28:25
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTabPagerView extends LinearLayout {

	/** The context. */
	private Context context;

    /** TabLayout. */
    private TabLayout tabLayout;

	/** ViewPager. */
	private RdfViewPager viewPager;

    /** FragmentManager. */
    private FragmentManager fragmentManager;

	/** 内容区域的适配器. */
    private RdfFragmentPagerAdapter fragmentPagerAdapter = null;

    /** tab的文字. */
    private String[] tabTextList = null;

    /** tab的图标. */
    private List<Drawable[]> tabDrawableList = null;

    /** tab的文字颜色. */
    private int[] tabTextColors = null;

    /** tab的文字大小. */
    private int tabTextSize = 18;

    /** tab的文字View. */
    private List<TextView> tabTextViewList = null;

    /** 内容的View. */
    private List<Fragment> tabFragmentList = null;

    /**
     * 构造函数..
     * @param context the context
     */
    public RdfTabPagerView(Context context) {
        this(context, null);
    }

    /**
     * 构造函数.
     * @param context the context
     * @param attrs the attrs
     */
    public RdfTabPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //要求必须是FragmentActivity的实例
        if(!(this.context instanceof FragmentActivity)){
            RdfLogUtil.e(RdfTabPagerView.class, "构造AbTabPagerView的参数context,必须是FragmentActivity的实例。");
            return;
        }

        this.fragmentManager =  ((FragmentActivity)this.context).getSupportFragmentManager();
        initView();
    }

    /**
     * 如果控件的父亲是个Fragment，就不要使用xml声明，而是应该用这个方法代替，
     * 关键是getChildFragmentManager，否则你的fragment将不能显示内容
     * @param fragment the fragment
     */
    public RdfTabPagerView(Fragment fragment) {
        super(fragment.getActivity());
        this.context = fragment.getActivity();
        this.fragmentManager = fragment.getChildFragmentManager();
        initView();


    }

    /**
     * 初始化View.
     */
    public void initView(){
    	this.setOrientation(LinearLayout.VERTICAL);

        View contentView = View.inflate(context,R.layout.view_tab_pager,null);
        tabLayout = (TabLayout)contentView.findViewById(R.id.tab_layout);
        viewPager = (RdfViewPager)contentView.findViewById(R.id.view_pager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(tabTextViewList == null){
                    return;
                }
                for(int i=0;i<tabTextViewList.size();i++){
                    TextView textView  = tabTextViewList.get(i);
                    if(position == i){
                        textView.setTextColor(tabTextColors[1]);
                    }else{
                        textView.setTextColor(tabTextColors[0]);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        this.addView(contentView);


    }

    /**
     * 设置Tab文字颜色
     * @param tabTextColors
     */
    public void setTabTextColors(int[] tabTextColors){
        this.tabTextColors = tabTextColors;
    }

    /**
     * 设置Tab文字大小
     * @param tabTextSize
     */
    public void setTabTextSize(int tabTextSize){
        this.tabTextSize = tabTextSize;
    }

    /**
     * 设置Tab布局背景
     * @param resId
     */
    public void setTabBackgroundResource(int resId){
        this.tabLayout.setBackgroundResource(resId);
    }

    /**
     * 设置数据
     * @param tabTextList
     * @param tabFragmentList
     */
    public void setTabs(String[] tabTextList,List<Drawable[]> tabDrawableList,List<Fragment> tabFragmentList){
        this.tabDrawableList = tabDrawableList;
        setTabs(tabTextList,tabFragmentList);
    }

    /**
     * 设置数据
     * @param tabTextList
     * @param tabFragmentList
     */
    public void setTabs(String[] tabTextList,List<Fragment> tabFragmentList){
        this.tabTextList = tabTextList;
        this.tabFragmentList = tabFragmentList;
        this.tabTextViewList = new ArrayList<TextView>();
        fragmentPagerAdapter = new RdfFragmentPagerAdapter(fragmentManager,tabTextList, tabFragmentList);

        //viewpager加载adapter
        viewPager.setAdapter(fragmentPagerAdapter);

        //TabLayout加载viewpager
        //tabLayout.setViewPager(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //为TabLayout添加tab名称
        for(int i=0;i<tabTextList.length;i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

    }

    /**
     * 添加getTabView的方法，来进行自定义Tab的布局View
     * @param position
     * @return
     */
    public View getTabView(int position){
        View view = View.inflate(context,R.layout.item_tab_view,null);
        TextView tabText = (TextView)view.findViewById(R.id.tab_text);
        tabText.setText(this.tabTextList[position]);
        tabText.setTextSize(this.tabTextSize);
        this.tabTextViewList.add(tabText);

        ImageView tabIcon = (ImageView)view.findViewById(R.id.tab_icon);
        if(this.tabDrawableList!=null && this.tabDrawableList.size()==this.tabTextList.length){
            tabIcon.setVisibility(View.VISIBLE);
            Drawable[] drawables = tabDrawableList.get(position);
            if(position == 0){
                tabIcon.setImageDrawable(drawables[1]);
            }else{
                tabIcon.setImageDrawable(drawables[0]);
            }
        }else{
            tabIcon.setVisibility(View.GONE);
        }

        if(tabTextColors==null || tabTextColors.length<2){
            tabTextColors = new int[]{context.getResources().getColor(R.color.gray_content),context.getResources().getColor(R.color.blue_light)};
        }

        if(position == 0){
            tabText.setTextColor(tabTextColors[1]);
        }else{
            tabText.setTextColor(tabTextColors[0]);
        }
        return view;
    }

	/**
	 * 获取ViewPager.
	 *
	 * @return the view pager
	 */
	public RdfViewPager getViewPager() {
		return viewPager;
	}

    /**
     * 获取TabLayout
     * @return
     */
    public TabLayout getTabLayout() {
        return tabLayout;
    }


    /**
	 * 设置是否可以滑动控制.
	 * @param enabled
	 */
	public void setPagingEnabled(boolean enabled) {
		viewPager.setPagingEnabled(enabled);
	}


    /**
     * 设置TAB 模式.
     * @param mode  TabLayout.MODE_FIXED,TabLayout.MODE_SCROLLABLE
     */
    public void setTabMode(int mode) {
        tabLayout.setTabMode(mode);
    }

}
