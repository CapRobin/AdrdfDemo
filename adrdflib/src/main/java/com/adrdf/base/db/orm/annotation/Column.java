package com.adrdf.base.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Copyright © CapRobin
 *
 * Name：Column
 * Describe：对应的列
 * Date：2017-06-27 11:34:30
 * Author: CapRobin@yeah.net
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Column {
	
	/**
	 * 列名.
	 * @return the string
	 */
	public abstract String name();

	/**
	 * 列类型.
	 * 支持：INTEGER，INT，BIGINT，FLOAT，DOUBLE，BLOB
	 * @Column(name = "xxx", type = "INTEGER")
	 * @return the string
	 */
	public abstract String type() default "";

	/**
	 * 字段长度.
	 * @return the int
	 */
	public abstract int length() default 0;
	
}

