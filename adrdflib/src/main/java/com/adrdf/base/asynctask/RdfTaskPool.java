package com.adrdf.base.asynctask;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Message;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskPool
 * Describe：线程池
 * Date：2017-06-27 09:04:16
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTaskPool {
	
	/** 单例对象 The http pool. */
	private static RdfTaskPool abTaskPool = null;
	
	/** 线程执行器. */
	public static Executor mExecutorService = null;
	
	/**  存放返回的任务结果. */
    private static HashMap<String,Object> result;
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
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
	 * 构造线程池.
	 */
    private RdfTaskPool() {
        result = new HashMap<String,Object>();
        mExecutorService = RdfThreadFactory.getExecutorService();
    } 
	
	/**
	 * 单例构造图片下载器.
	 *
	 * @return single instance of AbHttpPool
	 */
    public static RdfTaskPool getInstance() {
    	if (abTaskPool == null) { 
    		abTaskPool = new RdfTaskPool();
        } 
        return abTaskPool;
    } 
    
    /**
     * 执行任务.
     * @param item the item
     */
    public void execute(final RdfTaskItem item) {
    	mExecutorService.execute(new Runnable() { 
    		public void run() {
    			try {
    				//定义了回调
                    if (item.getListener() != null) { 
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
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}                         
    		}                 
    	});                 
    	
    }
	
}
