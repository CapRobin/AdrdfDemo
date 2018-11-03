package com.adrdf.base.app.global;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * Copyright © CapRobin
 *
 * Name：RdfActivityManager
 * Describe：用于处理退出程序时可以退出所有的activity，而编写的通用类
 * Date：2017-08-27 16:57:04
 * Author: CapRobin@yeah.net
 *
 */
public class RdfActivityManager {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static RdfActivityManager instance;

	private RdfActivityManager() {
	}

	/**
	 * 单例模式中获取唯一的AbActivityManager实例.
	 * @return
	 */
	public static RdfActivityManager getInstance() {
		if (null == instance) {
			instance = new RdfActivityManager();
		}
		return instance;
	}

	/**
	 * 当前数量
	 * @return
     */
	public int count(){
		return activityList.size();
	}

	/**
	 * 添加Activity到容器中.
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	/**
	 * 移除Activity从容器中.
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	/**
	 * 移除Activity从容器中.
	 * @param index
	 */
	public void removeActivity(int index) {
		activityList.remove(index);
	}

	/**
	 * 遍历所有Activity并finish.
	 */
	public void clearAllActivity() {
		for (Activity activity : activityList) {
			if(activity!=null){
				activity.finish();
			}
		}
	}
}