package com.adrdf.base.model;

import android.graphics.drawable.Drawable;
/**
 * Copyright © CapRobin
 *
 * Name：RdfAppProcessInfo
 * Describe：进程信息
 * Date：2017-06-27 10:53:42
 * Author: CapRobin@yeah.net
 *
 */
public class RdfAppProcessInfo implements Comparable<RdfAppProcessInfo>{
	
	/** app名称 */
	public String appName;

	/** 进程名称 */
	public String processName;

	/** 进程PID */
	public int pid;

	/** 进程uid */
	public int uid;

	/** app 图标. */
	public Drawable icon;

	/**  占用的内存. */
	public long memory;
	
	/**  占用的内存. */
	public String cpu;
	
	/**  进程的状态，其中S表示休眠，R表示正在运行，Z表示僵死状态，N表示该进程优先值是负数. */
	public String status;
	
	/**  当前使用的线程数. */
	public String threadsCount;
	
	/**
	 * 空的构造.
	 */
	public RdfAppProcessInfo() {
		super();
	}

	/**
	 * 构造函数.
	 *
	 * @param processName the process name
	 * @param pid the pid
	 * @param uid the uid
	 */
	public RdfAppProcessInfo(String processName, int pid, int uid) {
		super();
		this.processName = processName;
		this.pid = pid;
		this.uid = uid;
	}

	/**
	 * 比较
	 * @param another
	 * @return
     */
	@Override
	public int compareTo(RdfAppProcessInfo another) {
		if(this.processName.compareTo(another.processName)==0){
			if(this.memory < another.memory){
	    		return 1;
	    	}else if(this.memory == another.memory){
	    		return 0;
	    	}else{
	    		return -1;
	    	}
		}else{
			return this.processName.compareTo(another.processName);
		}
	}

}
