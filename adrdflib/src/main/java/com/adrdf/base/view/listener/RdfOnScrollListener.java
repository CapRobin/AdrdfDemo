package com.adrdf.base.view.listener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnScrollListener
 * Describe：滚动事件监听器
 * Date：2017-05-27 11:19:44
 * Author: CapRobin@yeah.net
 *
 */
public abstract  class RdfOnScrollListener {

    /**
     * 滚动事件
     * @param position 位置
     */
    public void onScrollPosition(int position){};

    /**
     * 滚动事件
     * @param scrollY Y滚动的距离
     */
    public void onScrollY(int scrollY){};


}
