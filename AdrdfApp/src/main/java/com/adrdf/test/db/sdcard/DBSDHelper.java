package com.adrdf.test.db.sdcard;

import android.content.Context;

import com.adrdf.test.model.LocalUser;
import com.adrdf.test.model.Phone;
import com.adrdf.test.model.Stock;
import com.adrdf.base.db.orm.helper.RdfSdDbHelper;
import com.adrdf.base.util.RdfFileUtil;


public class DBSDHelper extends RdfSdDbHelper {

	private static DBSDHelper dbHelper;

	// 数据库名
	private static final String DBNAME = "adrdf.db";
    
    // 当前数据库的版本
	private static final int DBVERSION = 1;
	// 要初始化的表
	private static final Class<?>[] clazz = {LocalUser.class, Stock.class,Phone.class};

	public static DBSDHelper getInstance(Context context) {
		if(dbHelper==null){
			dbHelper = new DBSDHelper(context);
		}
		return dbHelper;
	}

	public DBSDHelper(Context context) {
		super(context, RdfFileUtil.getDbDownloadDir(context), DBNAME, null, DBVERSION, clazz);
	}

}



