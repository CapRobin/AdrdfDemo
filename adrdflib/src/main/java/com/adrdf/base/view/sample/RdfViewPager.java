
package com.adrdf.base.view.sample;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Copyright © CapRobin
 *
 * Name：RdfViewPager
 * Describe：可设置是否滑动的ViewPager.
 * Date：2018-06-27 11:30:44
 * Author: CapRobin@yeah.net
 *
 */

public class RdfViewPager extends ViewPager {

	private boolean enabled;

	public RdfViewPager(Context context) {
		super(context);
		this.enabled = true;
	}
	

	public RdfViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	/**
	 * 是否允许滑动
	 * @param enabled
     */
	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
