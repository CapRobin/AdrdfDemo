package com.adrdf.test.global;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.adrdf.test.R;
import com.adrdf.base.global.RdfConstant;
import com.adrdf.base.util.RdfSharedUtil;
/**
 * Copyright © CapRobin
 *
 * Name：MyApplication
 * Describe：
 * Date：2018-02-17 18:47:49
 * Author: CapRobin@yeah.net
 *
 */
public class MyApplication extends Application {

	public SharedPreferences sharedPreferences = null;

	/** 主题*/
	public int themeId = -1;

	@Override
	public void onCreate() {
		super.onCreate();
		sharedPreferences = RdfSharedUtil.getDefaultSharedPreferences(this);
		initTheme();

	}

	public void initTheme(){

		themeId = RdfSharedUtil.getInt(this, RdfConstant.THEME_ID,-1);
		if(themeId==-1){
			themeId = R.style.AppTheme1;
			this.setTheme(themeId);
            Editor editor = sharedPreferences.edit();
            editor.putInt(RdfConstant.THEME_ID, themeId);
            editor.commit();

		}
	}
	
	public void updateTheme(int themeId){
		this.themeId = themeId;
		Editor editor = sharedPreferences.edit();
		editor.remove(RdfConstant.THEME_ID);
		editor.putInt(RdfConstant.THEME_ID, this.themeId);
		editor.commit();
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
