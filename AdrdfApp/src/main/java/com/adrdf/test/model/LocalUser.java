package com.adrdf.test.model;

import com.adrdf.base.db.orm.annotation.Column;
import com.adrdf.base.db.orm.annotation.Id;
import com.adrdf.base.db.orm.annotation.Relations;
import com.adrdf.base.db.orm.annotation.Table;

import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：LocalUser
 * Describe：用户表
 * Date：2018-03-17 12:49:00
 * Author: CapRobin@yeah.net
 *
 */
@Table(name = "local_user")
public class LocalUser {

	// ID @Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	private int _id;

	// 服务器上的用户id
	@Column(name = "user_id")
	private String userId;

	// 登录用户名 length=20数据字段的长度是20
	@Column(name = "name", length = 20)
	private String name;

	// 用户密码
	@Column(name = "password")
	private String password;

	// 用户头像
	@Column(name = "head_url")
	private String headUrl;

	// 年龄一般是数值,用type = "INTEGER"规范一下.
	@Column(name = "age", type = "INTEGER")
	private int age;

	// 创建时间
	@Column(name = "create_time")
	private String createTime;

	// 包含实体的存储，(name 是关联的表名，foreignKey是关联的表的外键的列名，action是操作的时候是否处理关联表)
	@Relations(name="phone",type="one2one",foreignKey = "u_id",action="query_insert_delete")
	private Phone phone;

	// 包含List的存储，指定外键
	@Relations(name="stocks",type="one2many",foreignKey = "u_id",action="query_insert_delete")
	private List<Stock> stocks;
	
	// 有些字段您可能不希望保存到数据库中,不用@Column注释就不会映射到数据库.
	private String remark;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
