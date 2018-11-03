
package com.adrdf.base.asynctask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Process;

/**
 * Copyright © CapRobin
 *
 * Name：RdfThreadFactory
 * Describe：线程工厂
 * Date：2017-06-17 09:05:39
 * Author: CapRobin@yeah.net
 *
 */
public class RdfThreadFactory {

	/** 任务执行器. */
	public static Executor executorService = null;

    /**
     * 获取执行器.
     * @return the executor service
     */
    public static Executor getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newCachedThreadPool();
        }
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        return executorService;
    }

}
