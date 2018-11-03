package com.adrdf.base.view.listener;

import android.widget.CompoundButton;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnCheckedChangedListener
 * Describe：自定义tab点击回调接口
 * Date：2017-09-19 10:17:11
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfOnCheckedChangedListener {

    public void onCheckedChanged(int position, boolean isChecked){};
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){};
}
