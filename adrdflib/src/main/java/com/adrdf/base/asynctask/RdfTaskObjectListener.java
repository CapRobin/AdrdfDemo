package com.adrdf.base.asynctask;

/**
 * Copyright © CapRobin
 *
 * Name：RdfTaskObjectListener
 * Describe：数据监听器
 * Date：2016-10-24 01:12:51
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfTaskObjectListener extends RdfTaskListener {
	
	/**
	 * 执行开始
	 * @return 返回的结果对象
	 */
    public abstract <T extends Object> T getObject();
    
    /**
     * 执行开始后调用.
     * @param obj
     */
    public abstract <T extends Object> void update(T obj); 
    
	
}
