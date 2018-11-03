package com.adrdf.base.db.orm.annotation;

/**
 * Copyright © CapRobin
 *
 * Name：ActionType
 * Describe：关联关系操作类型
 * Date：2017-06-27 11:34:03
 * Author: CapRobin@yeah.net
 *
 */
public class ActionType {
    
    /** 对于关系表只关联查询，多个用符号"下横线"分割. */
    public static final String query = "query";
    
    /** 对于关系表只关联插入. */
    public static final String insert = "insert";

    /** 对于关系表只关联更新. */
    public static final String update = "update";
    
    /** 对于关系表只关联删除. */
    public static final String delete = "delete";

}
