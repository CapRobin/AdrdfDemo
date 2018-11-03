package com.adrdf.base.model;
/**
 * Copyright © CapRobin
 *
 * Name：RdfCPUInfo
 * Describe：CPU信息
 * Date：2017-04-26 15:54:06
 * Author: CapRobin@yeah.net
 *
 */
public class RdfCpuInfo {

	public String User;

	public String System;

	public String IOW;

	public String IRQ;

	public RdfCpuInfo() {
		super();
	}

	public RdfCpuInfo(String user, String system, String iOW, String iRQ) {
		super();
		User = user;
		System = system;
		IOW = iOW;
		IRQ = iRQ;
	}

}
