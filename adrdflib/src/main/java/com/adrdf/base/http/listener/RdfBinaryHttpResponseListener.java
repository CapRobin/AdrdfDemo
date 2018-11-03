package com.adrdf.base.http.listener;

import com.adrdf.base.http.RdfHttpUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfBinaryHttpResponseListener
 * Describe：Http响应监听器，返回二进制数据
 * Date：2017-03-27 15:46:55
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfBinaryHttpResponseListener extends RdfHttpResponseListener {
	
    /**
     * 空构造函数.
     */
	public RdfBinaryHttpResponseListener() {
		super();
	}
	
	/**
	 * 获取数据成功会调用这里.
	 *
	 * @param statusCode the status code
	 * @param content the content
	 */
    public abstract void onSuccess(int statusCode,byte[] content);
    

	/**
     * 成功消息.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void sendSuccessMessage(int statusCode,byte[] content){
    	sendMessage(obtainMessage(RdfHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
    

}
