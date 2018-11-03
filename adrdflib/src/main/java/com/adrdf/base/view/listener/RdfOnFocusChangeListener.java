package com.adrdf.base.view.listener;

import android.view.View;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnFocusChangeListener
 * Describe：焦点事件监听器
 * Date：2016-12-08 11:17:57
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfOnFocusChangeListener {

    /**
     * 焦点
     * @param view
     * @param hasFocus
     */
    public void onFocusChange(View view, boolean hasFocus);
}
