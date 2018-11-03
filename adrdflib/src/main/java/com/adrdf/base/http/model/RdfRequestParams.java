package com.adrdf.base.http.model;
import com.adrdf.base.http.entity.HttpMultipartMode;
import com.adrdf.base.http.entity.MultipartEntity;
import com.adrdf.base.http.entity.mine.content.ByteArrayBody;
import com.adrdf.base.http.entity.mine.content.ContentBody;
import com.adrdf.base.http.entity.mine.content.FileBody;
import com.adrdf.base.http.entity.mine.content.StringBody;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright © CapRobin
 *
 * Name：RdfRequestParams
 * Describe：Http请求参数
 * Date：2017-06-27 10:51:26
 * Author: CapRobin@yeah.net
 *
 */
public class RdfRequestParams {

	/** url参数. */
	protected ConcurrentHashMap<String, String> urlParams;

	/** 文件参数. */
	protected ConcurrentHashMap<String, ContentBody> fileParams;

    /** 文件变量. */
	private MultipartEntity multiPart = null;
	private final static int boundaryLength = 32;
	private final static String boundaryAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
	private String boundary;


    /**
     * 默认构造函数
     */
    public RdfRequestParams() {
        super();
        boundary = getBoundary();
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, ContentBody>();
        multiPart = new MultipartEntity(HttpMultipartMode.STRICT, boundary,Charset.forName("UTF-8"));
    }

    /**
     * 获取二进制字符串
     * @return
     */
	private String getBoundary() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < boundaryLength; ++i)
			sb.append(boundaryAlphabet.charAt(random.nextInt(boundaryAlphabet
					.length())));
		return sb.toString();
	}

    /**
     * 获取文件实体
     * @return
     */
	public MultipartEntity getMultiPart() {
		return multiPart;
	}


	/**
	 * @return multipart boundary string
	 */
	public String boundaryString() {
		return boundary;
	}

	/**
	 * 添加一个文件参数
	 * 
	 * @param attr
	 *            属性名
	 * @param file
	 *            文件
	 */
	public void put(String attr, File file) {
		if (attr != null && file != null) {
			fileParams.put(attr, new FileBody(file));
		}

	}

	/**
	 * 添加一个byte[]参数
	 * 
	 * @param attr
	 *            属性名
	 * @param fileName
	 *            文件名
	 * @param data
	 *            字节数组
	 */
	public void put(String attr, String fileName, byte[] data) {
		if (attr != null && fileName != null) {
			fileParams.put(attr, new ByteArrayBody(data, fileName));
		}
	}
	
	/**
	 * 添加一个int参数
	 * 
	 * @param attr
	 * @param value
	 */
	public void put(String attr, int value) {
		try {
			urlParams.put(attr, String.valueOf(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加一个String参数
	 * 
	 * @param attr
	 * @param value
	 */
	public void put(String attr, String value) {
		try {
			if (attr != null && value != null) {
				urlParams.put(attr, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除参数
	 *
	 * @param attr
	 */
	public void remove(String attr) {
		try {
			if (attr != null) {
				urlParams.remove(attr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 参数数量
	 * 0:urlParams
	 * 1:fileParams
	 */
	public int[] size() {
		return new int[]{urlParams.size(),fileParams.size()};
	}

	/**
	 * 获取参数字符串.
	 * 
	 * @return the param string
	 */
	public String getParamString() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return URLEncodedUtils.format(paramsList, HTTP.UTF_8);
	}

	/**
	 * 获取参数列表.
	 * 
	 * @return the params list
	 */
	public List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return paramsList;
	}

	/**
	 * 
	 * 获取HttpEntity.
	 */
	public HttpEntity getEntity() {

		if (fileParams.isEmpty()) {
			// 不包含文件的
			return createFormEntity();
		} else {
			// 包含文件和参数的
			return createMultipartEntity();
		}
	}

	/**
	 * 创建HttpEntity.
	 * 
	 * @return the http entity
	 */
	public HttpEntity createFormEntity() {
		try {
			return new UrlEncodedFormEntity(getParamsList(), HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建文件域HttpEntity.
	 * 
	 * @return
	 * @throws IOException
	 */
	private HttpEntity createMultipartEntity() {

		try {
			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : urlParams
					.entrySet()) {
				multiPart.addPart(
						entry.getKey(),
						new StringBody(entry.getValue(), Charset
								.forName("UTF-8")));
			}

			// Add file params
			for (ConcurrentHashMap.Entry<String, ContentBody> entry : fileParams
					.entrySet()) {
				ContentBody contentBody = entry.getValue();
				multiPart.addPart(entry.getKey(), contentBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return multiPart;
	}

	/**
	 * 获取url参数.
	 * 
	 * @return the url params
	 */
	public ConcurrentHashMap<String, String> getUrlParams() {
		return urlParams;
	}

	/**
	 * 获取文件参数.
	 * 
	 * @return the file params
	 */
	public ConcurrentHashMap<String, ContentBody> getFileParams() {
		return fileParams;
	}

}
