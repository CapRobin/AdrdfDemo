package com.adrdf.test.global;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.adrdf.test.activity.CaptureActivity;
import com.adrdf.test.activity.RecoderCameraActivity;
import com.adrdf.base.global.RdfConstant;
import com.adrdf.base.http.entity.MultipartEntity;
import com.adrdf.base.http.model.RdfRequestParams;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright © CapRobin
 *
 * Name：AndH5Impl
 * Describe：
 * Date：2018-02-27 17:47:13
 * Author: CapRobin@yeah.net
 *
 */
public class AndH5Impl implements AndH5Interface {
	
	private Context context = null;
	
	public AndH5Impl(Context context) {
		super();
		this.context = context;
	}

	@Override
    @JavascriptInterface
	public boolean takePicture(int cameraId,int orientation) {
		Intent intent = new Intent(this.context,CaptureActivity.class);
		/**前后摄像头  1是前置  0是后置*/
		intent.putExtra("cameraId", cameraId);
		/**横屏为0  竖屏为1*/
		intent.putExtra("orientation", orientation);
		((Activity)(this.context)).startActivityForResult(intent, RdfConstant.REQUEST_CODE_TAKEPICTURE);
		return true;
	}
	
	@Override
    @JavascriptInterface
	public String getPictureFile(String requestUrl,String localPath) {
		RdfRequestParams params = new RdfRequestParams();
		params.put("data", "1");
		params.put("file", new File(localPath));
		String result = request(requestUrl, params);
		return result;
	}

	@Override
    @JavascriptInterface
	public boolean startVideoRecoder(int cameraId,int orientation) {
		Intent intent = new Intent(this.context,RecoderCameraActivity.class);
		/**前后摄像头  1是前置  0是后置*/
		intent.putExtra("cameraId", cameraId);
		/**横屏为0  竖屏为1*/
		intent.putExtra("orientation", orientation);
		((Activity)(this.context)).startActivityForResult(intent, RdfConstant.REQUEST_CODE_RECODER);
		return true;
	}

	@Override
    @JavascriptInterface
	public String getVideoRecoderFile(String requestUrl, String localPath) {
		return getPictureFile(requestUrl,localPath);
	}


	/**
	 * 简单的请求,只支持返回的数据是String类型,不支持转发重定向
	 * @param url
	 * @param params
	 * @return
	 */
	public static String request(final String url, final RdfRequestParams params) {
		HttpURLConnection urlConn = null;
		String resultString = null;
		try {
			URL requestUrl = new URL(url);
			urlConn = (HttpURLConnection) requestUrl.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setConnectTimeout(10000);
			urlConn.setReadTimeout(10000);
			urlConn.setDoOutput(true);

			if(params!=null){
				urlConn.setRequestProperty("connection", "keep-alive");
				urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
				MultipartEntity reqEntity = (MultipartEntity)params.getEntity();
				reqEntity.writeTo(urlConn.getOutputStream());
			}else{
				urlConn.connect();
			}

			if (urlConn.getResponseCode() == HttpStatus.SC_OK){
				resultString = readString(urlConn.getInputStream());
			}else{
				resultString = readString(urlConn.getErrorStream());
			}
			urlConn.getInputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConn != null)
				urlConn.disconnect();
		}
		return resultString;
	}

	private static String readString(InputStream is) {
		StringBuffer rst = new StringBuffer();

		byte[] buffer = new byte[1024];
		int len = 0;

		try {
			while ((len = is.read(buffer)) > 0){
				rst.append(new String(buffer, 0,len,"UTF-8"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rst.toString();
	}
}
