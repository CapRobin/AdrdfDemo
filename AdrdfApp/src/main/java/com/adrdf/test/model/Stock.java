package com.adrdf.test.model;


import com.adrdf.base.db.orm.annotation.Column;
import com.adrdf.base.db.orm.annotation.Id;
import com.adrdf.base.db.orm.annotation.Table;

/**
 * Copyright © CapRobin
 *
 * Name：Stock
 * Describe：要保证有无参数的构造
 * Date：2018-03-04 17:49:37
 * Author: CapRobin@yeah.net
 *
 */
@Table(name = "stocks")
public class Stock {

	//@Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	public int _id;

	@Column(name = "u_id")
	public String uId;
	
	@Column(name = "text1")
	public String text1;
	
	@Column(name = "text2")
	public String text2;
	
	@Column(name = "text3")
	public String text3;
	
	@Column(name = "text4")
	public String text4;
	
	@Column(name = "text5")
	public String text5;
	
	@Column(name = "text6")
	public String text6;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getText3() {
		return text3;
	}

	public void setText3(String text3) {
		this.text3 = text3;
	}

	public String getText4() {
		return text4;
	}

	public void setText4(String text4) {
		this.text4 = text4;
	}

	public String getText5() {
		return text5;
	}

	public void setText5(String text5) {
		this.text5 = text5;
	}

	public String getText6() {
		return text6;
	}

	public void setText6(String text6) {
		this.text6 = text6;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}


}
