package com.adrdf.base.view.listener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnItemSelectListener
 * Describe：选择事件监听器
 * Date：2017-05-11 10:18:41
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfOnItemSelectListener {

    /**
     * 被点击.
     * @param position the position
     */
    public void onSelect(int position){};

    /**
     * 被点击.
     * @param position1 the position
     * @param position2 the position
     */
    public void onSelect(int position1,int position2){};

}
