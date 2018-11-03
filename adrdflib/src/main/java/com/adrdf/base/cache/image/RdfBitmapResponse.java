package  com.adrdf.base.cache.image;

import android.graphics.Bitmap;

/**
 * Copyright © CapRobin
 *
 * Name：RdfBitmapResponse
 * Describe：响应实体
 * Date：2017-02-15 22:18:57
 * Author: CapRobin@yeah.net
 *
 */
public class RdfBitmapResponse {
	
	/** Bitmap实体. */
	private Bitmap bitmap;
	
	/** 请求URL. */
	private String requestURL;

	/**
	 * 
	 * 构造.
	 * @param requestURL
	 */
	public RdfBitmapResponse(String requestURL) {
		super();
		this.requestURL = requestURL;
	}

	/**
	 * 获取Bitmap.
	 *
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * 设置Bitmap.
	 *
	 * @param bitmap the new bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * 获取请求的URL.
	 *
	 * @return the request url
	 */
	public String getRequestURL() {
		return requestURL;
	}

	/**
	 * 设置请求的URL.
	 *
	 * @param requestURL the new request url
	 */
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}
