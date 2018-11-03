package com.adrdf.base.http.listener;

import com.adrdf.base.http.RdfHttpUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfStringHttpResponseListener
 * Describe：通用Http响应监听器  返回字符串类型
 * Date：2017-06-17 12:48:50
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfStringHttpResponseListener extends RdfHttpResponseListener {
	
    /**
     * 构造.
     */
	public RdfStringHttpResponseListener() {
		super();
	}

	/**
	 * 获取数据成功会调用这里.
	 *
	 * @param statusCode the status code
	 * @param content the content
	 */
    public abstract void onSuccess(int statusCode,String content);

    /**
     * 成功消息.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void sendSuccessMessage(int statusCode,String content){
    	sendMessage(obtainMessage(RdfHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
		

}
