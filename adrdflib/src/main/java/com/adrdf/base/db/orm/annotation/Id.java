package com.adrdf.base.db.orm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © CapRobin
 *
 * Name：Id
 * Describe：主键 Id
 * Date：2017-06-27 11:34:39
 * Author: CapRobin@yeah.net
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Id {

    /**
     * 列自增.
     * 0不自增  1自增
     */
    public abstract int autoincrement() default 1;

}
