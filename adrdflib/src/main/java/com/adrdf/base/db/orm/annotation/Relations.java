package com.adrdf.base.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © CapRobin
 *
 * Name：Relations
 * Describe：表示关联表
 * Date：2017-06-27 11:35:13
 * Author: CapRobin@yeah.net
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Relations {
	
	/**
	 * 关联名,对象内唯一即可.
	 *
	 * @return the string
	 */
	public abstract String name();
	
	/**
	 * 外键.
	 *
	 * @return the string
	 */
	public abstract String foreignKey();
	
	/**
	 * 关联类型.
	 *
	 * @return the string  one2one  one2many many2many
	 */
	public abstract String type();
	
	/**
	 * 关联类型.
	 *
	 * @return the string  query insert query_insert
	 */
	public abstract String action() default "query_insert";
}
