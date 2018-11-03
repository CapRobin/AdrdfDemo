
package com.adrdf.base.view.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Copyright © CapRobin
 *
 * Name：RdfScrollTextView
 * Describe：跑马灯一直跑
 * Date：2017-04-27 11:26:49
 * Author: CapRobin@yeah.net
 *
 */
public class RdfScrollTextView extends TextView {


	public RdfScrollTextView(Context context) {
		this(context,null);
	}

	public RdfScrollTextView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}


	public RdfScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//关键
		this.setSingleLine(true);

	}

	/**
	 * 设置为焦点，能一直滚动.
	 */
	@Override
	public boolean isFocused() {
		return true;
	}

}
