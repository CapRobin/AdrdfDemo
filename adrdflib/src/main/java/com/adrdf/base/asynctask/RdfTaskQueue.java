package com.adrdf.base.asynctask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Message;

import com.adrdf.base.util.RdfLogUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskQueue
 * Describe：任务队列
 * Date：2016-06-27 09:05:04
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTaskQueue extends Thread {
	
	/** 等待执行的任务. 用 LinkedList增删效率高*/
	private LinkedList<RdfTaskItem> taskItemList = null;
  	
  	/** 停止的标记. */
	private boolean quit = false;
	
	/**  存放返回的任务结果. */
    private HashMap<String,Object> result;
	
	/** 执行完成后的消息句柄. */
    private Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	RdfTaskItem item = (RdfTaskItem)msg.obj;
        	if(item.getListener() instanceof RdfTaskListListener){
        		((RdfTaskListListener)item.getListener()).update((List<?>)result.get(item.toString()));
        	}else if(item.getListener() instanceof RdfTaskObjectListener){
        		((RdfTaskObjectListener)item.getListener()).update(result.get(item.toString()));
        	}else{
        		item.getListener().update(); 
        	}
        	result.remove(item.toString());
        } 
    }; 
    
    /**
     * 
     * 获取一个实例.
     * @return
     */
    public static RdfTaskQueue newInstance() {
        RdfTaskQueue abTaskQueue = new RdfTaskQueue();
        return abTaskQueue;
    } 
	
	/**
	 * 构造执行线程队列.
	 */
    private RdfTaskQueue() {
    	quit = false;
    	taskItemList = new LinkedList<RdfTaskItem>();
    	result = new HashMap<String,Object>();
    	//从线程池中获取
    	Executor mExecutorService  = RdfThreadFactory.getExecutorService();
    	mExecutorService.execute(this); 
    }
    
    /**
     * 开始执行任务.
     * @param item 执行单位
     */
    public void execute(RdfTaskItem item) {
         addTaskItem(item); 
    } 
    
    
    /**
     * 开始一个执行任务并清除原来队列.
     * @param item 执行单位
     * @param cancel 清空之前的任务
     */
    public void execute(RdfTaskItem item, boolean cancel) {
	    if(cancel){
	    	 cancel(true);
	    }
    	addTaskItem(item); 
    } 
     
    /**
     * 添加到执行线程队列.
     * @param item 执行单位
     */
    private synchronized void addTaskItem(RdfTaskItem item) {
    	taskItemList.add(item);
    	//添加了执行项就激活本线程 
        this.notify();
        
    }

    /**
	 * 线程运行
	 */
    @Override 
    public void run() { 
        while(!quit) { 
        	try {
        	    while(taskItemList.size() > 0){
            
					RdfTaskItem item = taskItemList.remove(0);
					//定义了回调
				    if (item!=null && item.getListener() != null) {
				        if(item.getListener() instanceof RdfTaskListListener){
                            result.put(item.toString(), ((RdfTaskListListener)item.getListener()).getList());
                        }else if(item.getListener() instanceof RdfTaskObjectListener){
                            result.put(item.toString(), ((RdfTaskObjectListener)item.getListener()).getObject());
                        }else{
                        	item.getListener().get();
                            result.put(item.toString(), null);
                        }
				    	//交由UI线程处理 
				        Message msg = handler.obtainMessage(); 
				        msg.obj = item; 
				        handler.sendMessage(msg); 
				    } 
				    
				    //停止后清空
				    if(quit){
				    	taskItemList.clear();
				    	return;
				    }
        	    }
        	    try {
					//没有执行项时等待 
					synchronized(this) { 
					    this.wait();
					}
				} catch (InterruptedException e) {
					RdfLogUtil.e("AbTaskQueue","收到线程中断请求");
					e.printStackTrace();
					//被中断的是退出就结束，否则继续
					if (quit) {
						taskItemList.clear();
	                    return;
	                }
	                continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
        } 
    } 
    
    /**
     * 终止队列释放线程.
     *
     * @param interrupt the may interrupt if running
     */
    public void cancel(boolean interrupt){
		try {
			quit  = true;
			if(interrupt){
				interrupted();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	/**
	 * 获取任务列表
	 * @return
     */
	public LinkedList<RdfTaskItem> getTaskItemList() {
		return taskItemList;
	}

	/**
	 * 获取任务数量
	 * @return
	 */
	public int getTaskItemListSize() {
		return taskItemList.size();
	}
    
}

