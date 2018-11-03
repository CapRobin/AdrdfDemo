package com.adrdf.base.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Copyright © CapRobin
 *
 * Name：Table
 * Describe：数据表
 * Date：2017-06-27 11:35:45
 * Author: CapRobin@yeah.net
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.TYPE })
public @interface Table {
	
	/**
	 * 表名.
	 * @return the string
	 */
	public abstract String name();
}
