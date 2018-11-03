package com.adrdf.base.view.wheel;

/**
 * Copyright © CapRobin
 *
 * Name：RdfWheelAdapter
 * Describe：轮子适配器接口
 * Date：2017-05-24 18:29:34
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfWheelAdapter {
	
	/**
	 * 获取条目数量.
	 *
	 */
	public int getItemsCount();
	
	/**
	 * 获取条目的值.
	 * @param index 索引
	 */
	public String getItem(int index);
	
	/**
	 * 获取条目的最大字符长度，中文表示2个.
	 */
	public int getMaximumLength();
}
