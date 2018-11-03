package com.adrdf.test.db.inside;

import android.content.Context;

import com.adrdf.test.model.LocalUser;
import com.adrdf.test.model.Phone;
import com.adrdf.test.model.Stock;
import com.adrdf.base.db.orm.helper.RdfDbHelper;

/**
 * Copyright © CapRobin
 *
 * Name：DBInsideHelper
 * Describe：手机data/data下面的数据库
 * Date：2018-03-11 16:51:16
 * Author: CapRobin@yeah.net
 *
 */
public class DBInsideHelper extends RdfDbHelper {

	private static DBInsideHelper dbHelper;

	// 数据库名
	private static final String DB_NAME = "adrdf.db";
    
    // 当前数据库的版本
	private static final int DB_VERSION = 1;

	// 要初始化的表
	private static final Class<?>[] clazz = { LocalUser.class, Stock.class,Phone.class};

	public static DBInsideHelper getInstance(Context context) {
		if(dbHelper==null){
			dbHelper = new DBInsideHelper(context);
		}
		return dbHelper;
	}

	public DBInsideHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION, clazz);
	}

}



