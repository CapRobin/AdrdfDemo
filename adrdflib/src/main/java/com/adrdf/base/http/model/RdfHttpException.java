
package com.adrdf.base.http.model;

import com.adrdf.base.config.RdfAppConfig;
import com.adrdf.base.util.RdfStrUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Copyright © CapRobin
 *
 * Name：RdfHttpException
 * Describe：异常类
 * Date：2017-10-23 10:49:36
 * Author: CapRobin@yeah.net
 *
 */
public class RdfHttpException extends Exception {

	/** 异常码. */
	private int code = -1;
	/** 异常消息. */
	private String message = null;

	/**
	 * 构造异常类.
	 * @param e 异常
	 */
	public RdfHttpException(Exception e) {
		super();

		if( e instanceof HttpHostConnectException) {
			code = RdfHttpStatus.CONNECT_FAILURE_CODE;
			message = RdfAppConfig.UNKNOWN_HOST_EXCEPTION;
		}else if (e instanceof UnknownHostException) {
			code = RdfHttpStatus.CONNECT_FAILURE_CODE;
			message = RdfAppConfig.UNKNOWN_HOST_EXCEPTION;
		}
		else if (e instanceof ConnectException || e instanceof SocketException) {
			code = RdfHttpStatus.CONNECT_FAILURE_CODE;
			message = RdfAppConfig.CONNECT_EXCEPTION;
		}else if (e instanceof ConnectTimeoutException || e instanceof SocketTimeoutException) {
			code = RdfHttpStatus.CONNECT_TIMEOUT_CODE;
			message = RdfAppConfig.CONNECT_TIMEOUT_EXCEPTION;
		}
		else if( e instanceof ClientProtocolException) {
			code = RdfHttpStatus.PROTOCOL_FAILURE_CODE;
			message = RdfAppConfig.CLIENT_PROTOCOL_EXCEPTION;
		}
		else if( e instanceof NullPointerException) {
			code = RdfHttpStatus.PROGRAM_FAILURE_CODE;
			message = RdfAppConfig.REMOTE_SERVICE_EXCEPTION;
		}
		else {
            code = RdfHttpStatus.UNTREATED_CODE;
			if (e == null || RdfStrUtil.isEmpty(e.getMessage())) {
				message = RdfAppConfig.REMOTE_SERVICE_EXCEPTION;
			}else{
				message = e.getMessage();
			}
		}

	}

	/**
	 * 用一个消息构造异常类.
	 * @param code
	 * @param message 异常的消息
	 */
	public RdfHttpException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	/**
	 * 获取异常信息.
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * 获取异常码.
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}
