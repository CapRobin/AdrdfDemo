package com.adrdf.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
/**
 * Copyright © CapRobin
 *
 * Name：RdfToastUtil
 * Describe：提示工具类
 * Date：2017-03-18 11:10:13
 * Author: CapRobin@yeah.net
 *
 */
public class RdfToastUtil {

    /** 上下文. */
    private static Context mContext = null;
    
    /** 显示Toast. */
    public static final int SHOW_TOAST = 0;
    
    /**
	 * 主要Handler类，在线程中可用
	 * what：0.提示文本信息
	 */
	@SuppressLint("HandlerLeak")
	private static Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_TOAST:
					showToast(mContext,msg.getData().getString("TEXT"));
					break;
				default:
					break;
			}
		}
	};
    
    /**
     * Toast提示文本.
	 * @param context
     * @param text  文本
     */
	public static void showToast(Context context,String text) {
		mContext = context;
		if(!RdfStrUtil.isEmpty(text)){
			Toast.makeText(context,text, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
     * Toast提示文本.
	 * @param context
     * @param resId  文本的资源ID
     */
	public static void showToast(Context context,int resId) {
		mContext = context;
		Toast.makeText(context,""+context.getResources().getText(resId), Toast.LENGTH_SHORT).show();
	}
    
    /**
	 * 在线程中提示文本信息.
	 * @param context
	 * @param resId 要提示的字符串资源ID，消息what值为0,
	 */
	public static void showToastInThread(Context context,int resId) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", context.getResources().getString(resId));
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}

	/**
	 * 在线程中提示文本信息.
	 * @param context
	 * @param text
     */
	public static void showToastInThread(Context context,String text) {
		mContext = context;
		Message msg = baseHandler.obtainMessage(SHOW_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("TEXT", text);
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}


}
