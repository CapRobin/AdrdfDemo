package com.adrdf.test.model;


import com.adrdf.base.db.orm.annotation.Column;
import com.adrdf.base.db.orm.annotation.Id;
import com.adrdf.base.db.orm.annotation.Table;

/**
 * Copyright © CapRobin
 *
 * Name：Phone
 * Describe：要保证有无参数的构造
 * Date：2018-03-17 15:49:14
 * Author: CapRobin@yeah.net
 *
 */
@Table(name = "phone")
public class Phone {

	// ID @Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	public int _id;

	@Column(name = "u_id")
	public String uId;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "desc")
	public String desc;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
