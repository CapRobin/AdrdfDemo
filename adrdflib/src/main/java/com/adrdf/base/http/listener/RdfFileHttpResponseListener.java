package com.adrdf.base.http.listener;

import java.io.File;
import android.content.Context;
import com.adrdf.base.http.RdfHttpUtil;
import com.adrdf.base.util.RdfFileUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfFileHttpResponseListener
 * Describe：Http响应监听器，返回文件
 * Date：2016-11-11 10:47:49
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfFileHttpResponseListener extends RdfHttpResponseListener {

    /** 当前下载的文件. */
    private File file;

	/**
	 * 默认的构造.
	 */
	public RdfFileHttpResponseListener() {
		super();
	}

	/**
     * 下载文件的构造,指定文件名称.
     * @param file 文件名称
     */
    public RdfFileHttpResponseListener(File file) {
        super();
	    this.file = file;
    }
	
	/**
	 * 下载文件成功.
	 *
	 * @param statusCode the status code
	 * @param file the file
	 */
    public abstract void onSuccess(int statusCode,File file);

   
   /**
    * 成功消息.
    * @param statusCode the status code
    */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(RdfHttpUtil.SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 失败消息.
     * @param statusCode the status code
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(RdfHttpUtil.FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	/**
	 * 获取文件.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 设置文件.
	 * @param file the new file
	 */
	private void setFile(File file) {
		this.file = file;
		try {
			if(!file.getParentFile().exists()){
			      file.getParentFile().mkdirs();
			}
			file.deleteOnExit();
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 设置文件.
	 * @param context the context
	 * @param name the name
	 */
	public String setFile(Context context,String name) {
		//生成缓存文件
        if(RdfFileUtil.isCanUseSD()){
	    	File file = new File(RdfFileUtil.getFileDownloadDir(context) + name);
	    	setFile(file);
			return file.getPath();
        }
		return null;

	}
    
}
