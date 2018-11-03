package com.adrdf.base.asynctask;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskListener
 * Describe：数据监听器
 * Date：2016-12-01 14:35:58
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTaskListener {

	/**
	 * 执行开始.
	 * 
	 * @return 返回的结果对象
	 */
	public void get() {
	};

	/**
	 * 执行开始后调用.
	 * */
	public void update() {
	};

	/**
	 * 监听进度变化.
	 * 
	 * @param values the values
	 */
	public void onProgressUpdate(Integer... values) {
	};

}
