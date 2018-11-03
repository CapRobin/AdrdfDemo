package com.adrdf.base.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfFragmentPagerAdapter
 * Describe：AbBaseFragment 的适配器
 * Date：2017-06-27 08:47:00
 * Author: CapRobin@yeah.net
 *
 */
public class RdfFragmentPagerAdapter extends FragmentPagerAdapter {


    /**
     * 标题数组.
     */
    private String[] titleList = null;

    /**
     * Fragment列表.
     */
    private List<Fragment> fragmentList = null;


    /**
     * 构造适配器.
     * @param fragmentManager the fragment manager
     * @param fragmentList    the fragment list
     */
    public RdfFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
        super(fragmentManager);
        this.titleList = new String[fragmentList.size()];
        this.fragmentList = fragmentList;
    }

    /**
     * 构造适配器.
     * @param fragmentManager
     * @param titleList
     * @param fragmentList
     */
    public RdfFragmentPagerAdapter(FragmentManager fragmentManager, String[] titleList, List<Fragment> fragmentList) {
        super(fragmentManager);
        this.titleList = titleList;
        this.fragmentList = fragmentList;
    }

    /**
     * 获取元素数量.
     * @return the count
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 获取索引位置的Fragment.
     * @param position the position
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position < fragmentList.size()) {
            fragment = fragmentList.get(position);
        } else {
            fragment = fragmentList.get(0);
        }
        return fragment;

    }

    /**
     * 获取这个位置的标题
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position % titleList.length];
    }

}

