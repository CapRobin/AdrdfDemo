package com.adrdf.base.asynctask;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskItem
 * Describe：数据执行单位
 * Date：2017-06-27 08:35:11
 * Author: CapRobin@yeah.net
 *
 */
public class RdfTaskItem {
	
	/** 记录的当前索引. */
	private int position;
	 
 	/** 执行完成的回调接口. */
    private RdfTaskListener listener;
    
	/**
	 * Instantiates a new ab task item.
	 */
	public RdfTaskItem() {
		super();
	}

	/**
	 * Instantiates a new ab task item.
	 *
	 * @param listener the listener
	 */
	public RdfTaskItem(RdfTaskListener listener) {
		super();
		this.listener = listener;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public RdfTaskListener getListener() {
		return listener;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(RdfTaskListener listener) {
		this.listener = listener;
	}

} 

