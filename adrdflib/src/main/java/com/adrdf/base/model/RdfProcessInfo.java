package com.adrdf.base.model;
/**
 * Copyright © CapRobin
 *
 * Name：RdfProcessInfo
 * Describe：进程信息
 * Date：2017-03-27 08:55:09
 * Author: CapRobin@yeah.net
 *
 */
public class RdfProcessInfo {

	/**
	 * 进程uid.
	 */
	public String uid;
	
	/** 进程名称 */
	public String processName;

	/** 进程pid */
	public int pid;

	/**  占用的内存 B. */
	public long memory;
	
	/**  占用的CPU. */
	public String cpu;
	
	/**  进程的状态，其中S表示休眠，R表示正在运行，Z表示僵死状态，N表示该进程优先值是负数. */
	public String status;
	
	/**  当前使用的线程数. */
	public String threadsCount;
	
	/**
	 * 空构造函数.
	 */
	public RdfProcessInfo() {
		super();
	}

	/**
	 * 构造函数.
	 *
	 * @param processName the process name
	 * @param pid the pid
	 */
	public RdfProcessInfo(String processName, int pid) {
		super();
		this.processName = processName;
		this.pid = pid;
	}


}
