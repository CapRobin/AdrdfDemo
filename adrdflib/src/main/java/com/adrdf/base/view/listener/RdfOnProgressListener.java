package com.adrdf.base.view.listener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfOnProgressListener
 * Describe：进度事件监听器
 * Date：2017-06-27 11:19:06
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfOnProgressListener {

    /**
     * 进度.
     *
     * @param progress the progress
     */
    public void onProgress(int progress);

    /**
     * 完成.
     */
    public void onComplete();
}
