package com.adrdf.base.asynctask;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskMultiQueue
 * Describe：多任务队列
 * Date：2017-06-27 10:37:12
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTaskMultiQueue {

	/** 单例对象. */
	private static RdfTaskMultiQueue taskMultiQueue = null;

	/** 最大队列数. */
	private int  maxQueueCount = 5;

	/** 请求队列. */
	private List<RdfTaskQueue> taskQueueList;

	/**
	 * 构造图片下载器.
	 */
	public RdfTaskMultiQueue() {
		this.taskQueueList = new ArrayList<RdfTaskQueue>();
	}


	/**
	 *
	 * 获得一个实例.
	 * @return
	 */
	public static RdfTaskMultiQueue getInstance() {
		if (taskMultiQueue == null) {
			taskMultiQueue = new RdfTaskMultiQueue();
		}
		return taskMultiQueue;
	}

	/**
	 *
	 * 增加到最少的队列中.
	 * @param item
	 */
	public void execute(RdfTaskItem item){
		int minQueueIndex = 0;
		if(taskQueueList.size() == 0){
			RdfTaskQueue queue = RdfTaskQueue.newInstance();
			taskQueueList.add(queue);
			queue.execute(item);
		}else{
			int minSize = 0;
			for(int i=0;i<taskQueueList.size();i++){
				RdfTaskQueue queue = taskQueueList.get(i);
				int size = queue.getTaskItemListSize();
				if(i==0){
					minSize = size;
					minQueueIndex = i;
				}else{
					if(size < minSize){
						minSize = size;
						minQueueIndex = i;
					}
				}
			}
			if(taskQueueList.size() < maxQueueCount && minSize > 2){
				RdfTaskQueue queue = RdfTaskQueue.newInstance();
				taskQueueList.add(queue);
				queue.execute(item);
			}else{
				RdfTaskQueue minQueue = taskQueueList.get(minQueueIndex);
				minQueue.execute(item);
			}

		}

		/*for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			int size = queue.getTaskItemListSize();
			AbLogUtil.i(AbImageLoader.class, "线程队列["+i+"]的任务数："+size);
		}*/

	}

	/**
	 *
	 * 释放资源.
	 */
	public void cancelAll(){
		for(int i=0;i<taskQueueList.size();i++){
			RdfTaskQueue queue = taskQueueList.get(i);
			queue.cancel(true);
		}
		taskQueueList.clear();
	}

	public int getMaxQueueCount() {
		return maxQueueCount;
	}

	public void setMaxQueueCount(int maxQueueCount) {
		this.maxQueueCount = maxQueueCount;
	}
}

