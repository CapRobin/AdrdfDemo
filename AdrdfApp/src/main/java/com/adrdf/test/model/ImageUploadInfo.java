package com.adrdf.test.model;
/**
 * Copyright © CapRobin
 *
 * Name：ImageUploadInfo
 * Describe：
 * Date：2018-01-18 15:48:41
 * Author: CapRobin@yeah.net
 *
 */
public class ImageUploadInfo {

	private int id;
	private String path;
	private long bytesWritten;
	private long totalSize;
	private boolean delete;
	private boolean isCamBtn = false;

	public ImageUploadInfo() {
	}

	public ImageUploadInfo(String path) {
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getBytesWritten() {
		return bytesWritten;
	}

	public void setBytesWritten(long bytesWritten) {
		this.bytesWritten = bytesWritten;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isCamBtn() {
		return isCamBtn;
	}

	public void setCamBtn(boolean camBtn) {
		isCamBtn = camBtn;
	}
}
