package com.adrdf.test.global;

import android.webkit.JavascriptInterface;
/**
 * Copyright © CapRobin
 *
 * Name：AndH5Interface
 * Describe：
 * Date：2018-03-07 14:47:30
 * Author: CapRobin@yeah.net
 *
 */
public interface AndH5Interface {

	/**拍照，js回调 takePictureCallBack(localPath) */
	@JavascriptInterface
	public abstract boolean takePicture(int cameraId, int orientation);
	
	/**下载文件*/
	@JavascriptInterface
	public abstract String getPictureFile(String requestUrl, String localPath);
	
	/**开始录像 ,js回调 videoRecoderCallback(localPath)*/
	@JavascriptInterface
	public abstract boolean startVideoRecoder(int cameraId, int orientation);
	
	/**获取录制的文件*/
	@JavascriptInterface
	public abstract String getVideoRecoderFile(String requestUrl, String localPath);
	
}
