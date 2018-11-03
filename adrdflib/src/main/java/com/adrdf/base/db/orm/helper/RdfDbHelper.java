package com.adrdf.base.db.orm.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.adrdf.base.db.orm.table.RdfTableCreater;
/**
 * Copyright © CapRobin
 *
 * Name：RdfDbHelper
 * Describe：手机内部数据库 在data/data下面的数据库
 * Date：2017-06-27 11:36:41
 * Author: CapRobin@yeah.net
 *
 */
public class RdfDbHelper extends SQLiteOpenHelper{
	
	/** The model classes. */
	private Class<?>[] modelClasses;
	
	
	/**
	 * 初始化一个DBHelper.
	 * @param context 应用context
	 * @param name 数据库名
	 * @param factory 数据库查询的游标工厂
	 * @param version 数据库的新版本号
	 * @param modelClasses 要初始化的表的对象
	 */
	public RdfDbHelper(Context context, String name,
                       CursorFactory factory, int version, Class<?>[] modelClasses) {
		super(context, name, factory, version);
		this.modelClasses = modelClasses;
	}
	
	
	/**
     * 表的创建.
     * @param db 数据库对象
     */
    public void onCreate(SQLiteDatabase db) {
		RdfTableCreater.createTablesByClasses(db, this.modelClasses);
	}

	/**
	 * 表的重建.
	 * @param db 数据库对象
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		RdfTableCreater.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}
