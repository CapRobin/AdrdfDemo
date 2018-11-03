package com.adrdf.base.asynctask;

import java.util.List;

import android.os.AsyncTask;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTask
 * Describe：下载数据的任务实现
 * Date：2016-12-23 08:08:31
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTask extends AsyncTask<RdfTaskItem, Integer, RdfTaskItem> {
	
	/** 监听器. */
	private RdfTaskListener listener;
	
	/** 结果. */
	private Object result;
	
	/**
	 * Task的空构造.
	 */
	public RdfTask() {
		super();
	}
	
	/**
	 * 实例化.
	 */
	public static RdfTask newInstance() {
		RdfTask task = new RdfTask();
		return task;
	}
	
	/**
	 * 
	 * 执行任务.
	 * @param items
	 * @return
	 */
	@Override
	protected RdfTaskItem doInBackground(RdfTaskItem... items) {
		RdfTaskItem item = items[0];
		this.listener = item.getListener();
		if (this.listener != null) { 
			if(this.listener instanceof RdfTaskListListener){
				result = ((RdfTaskListListener)this.listener).getList();
        	}else if(this.listener instanceof RdfTaskObjectListener){
        		result = ((RdfTaskObjectListener)this.listener).getObject();
        	}else{
        		this.listener.get(); 
        	}
        } 
		return item;
	}

	/**
	 * 
	 * 取消.
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/**
	 * 
	 * 执行完成.
	 * @param item
	 */
	@Override
	protected void onPostExecute(RdfTaskItem item) {
		if (this.listener != null) {
			if(this.listener instanceof RdfTaskListListener){
        		((RdfTaskListListener)this.listener).update((List<?>)result);
        	}else if(this.listener instanceof RdfTaskObjectListener){
        		((RdfTaskObjectListener)this.listener).update(result);
        	}else{
        		this.listener.update(); 
        	}
		}
	}

	/**
	 * 
	 * 执行前.
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 
	 * 进度更新.
	 * @param values
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (this.listener != null) { 
			this.listener.onProgressUpdate(values);
		}
	}

}
