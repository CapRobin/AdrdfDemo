package com.adrdf.base.view.listener;

import android.view.MotionEvent;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnTouchListener
 * Describe：接触事件监听器
 * Date：2018-06-27 11:22:53
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfOnTouchListener {

    /**
     * 被接触
     * @param event
     */
    public void onTouch(MotionEvent event);
}
