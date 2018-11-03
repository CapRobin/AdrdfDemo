package com.adrdf.base.cache.http;

import com.adrdf.base.http.model.RdfHttpStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：RdfHttpCacheResponse
 * Describe：响应实体类
 * Date：2017-06-17 02:08:34
 * Author: CapRobin@yeah.net
 *
 */
public class RdfHttpCacheResponse {
	
	/** 响应码. */
	public final int statusCode;

	/** 响应数据. */
	public final byte[] data;

	/** 响应头. */
	public final Map<String, String> headers;

	/**
	 * 构造.
	 *
	 * @param statusCode the status code
	 * @param data the data
	 * @param headers the headers
	 */
	public RdfHttpCacheResponse(int statusCode, byte[] data,
								Map<String, String> headers) {
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 */
	public RdfHttpCacheResponse(byte[] data) {
		this(RdfHttpStatus.SUCCESS_CODE, data, Collections.<String, String> emptyMap());
	}

	/**
	 * 构造.
	 *
	 * @param data the data
	 * @param headers the headers
	 */
	public RdfHttpCacheResponse(byte[] data, Map<String, String> headers) {
		this(RdfHttpStatus.SUCCESS_CODE, data, headers);
	}

}