package com.adrdf.base.view.wheel;

import com.adrdf.base.util.RdfStrUtil;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfStringWheelAdapter
 * Describe：轮子适配器（字符串）
 * Date：2018-03-20 15:29:13
 * Author: CapRobin@yeah.net
 *
 */
public class RdfStringWheelAdapter implements RdfWheelAdapter {
	
	/** 条目列表. */
	private List<String> items;

	/** 长度. */
	private int length = -1;

	/**
	 * 构造函数.
	 * @param items the items
	 */
	public RdfStringWheelAdapter(List<String> items) {
		this.items = items;
        getMaximumLength();
	}


	@Override
	public String getItem(int index) {
		if (index >= 0 && index < items.size()) {
			return items.get(index);
		}
		return null;
	}


	@Override
	public int getItemsCount() {
		return items.size();
	}


	@Override
	public int getMaximumLength() {
		if(length!=-1){
			return length;
		}
		for(int i=0;i<items.size();i++){
			String cur = items.get(i);
			int l = RdfStrUtil.strLength(cur);
			if(length<l){
                length = l;
			}
		}
		return length;
	}

}
