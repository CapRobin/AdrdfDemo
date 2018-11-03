package com.adrdf.base.app.base;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adrdf.base.app.global.RdfActivityManager;
import com.adrdf.base.global.RdfConstant;
import com.adrdf.base.http.RdfHttpUtil;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfSharedUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfBaseActivity
 * Describe：所有Activity要继承这个父类，
 * 便于统一管理;自动加载SharedPreferences中的key为AbConstant.THEME_ID ="themeId"的主题
 * Date：2017-05-27 09:53:10
 * Author: CapRobin@yeah.net
 *
 */
public abstract class RdfBaseActivity extends AppCompatActivity {

    /** 主题*/
	private int themeId = -1;

    /** 当Activity结束会中止请求*/
	public RdfHttpUtil httpUtil = null;

    /** 当Activity结束会中止请求*/
    public RdfImageLoader imageLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        themeId = RdfSharedUtil.getInt(this, RdfConstant.THEME_ID,-1);

        if(themeId!=-1){
            this.setTheme(themeId);
        }else{
            this.setTheme(android.R.style.Theme_NoTitleBar);
        }
		super.onCreate(savedInstanceState);
		
		RdfActivityManager.getInstance().addActivity(this);
		httpUtil = RdfHttpUtil.getInstance(this);
        imageLoader = RdfImageLoader.getInstance(this);
    }

	
	/**
	 * 设置主题ID
	 * @param themeId
	 */
    public void setAppTheme(int themeId){
		this.themeId = themeId;
        this.recreate();  
    }
    
    /**
	 * 返回默认
	 * @param view
	 */
	public void back(View view){
		finish();
	}

    /**
	 * 结束
	 */
	@Override
	public void finish() {
		RdfActivityManager.getInstance().removeActivity(this);
		httpUtil.cancelCurrentTask();
        imageLoader.cancelCurrentTask();
		super.finish();
	}
    
}

