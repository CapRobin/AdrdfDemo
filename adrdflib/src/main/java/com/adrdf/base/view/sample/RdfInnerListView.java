package com.adrdf.base.view.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Copyright © CapRobin
 *
 * Name：RdfInnerListView
 * Describe：这个ListView不会与父亲是个ScrollView与List的产生事件冲突
 * Date：2018-06-27 11:27:04
 * Author: CapRobin@yeah.net
 *
 */
public class RdfInnerListView extends ListView {

	/** The parent scroll view. */
	private ScrollView parentScrollView;

	/** The max height. */
	private int maxHeight;

	public ScrollView getParentScrollView() {
		return parentScrollView;
	}

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}


	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}


	public RdfInnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (maxHeight > -1) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
					MeasureSpec.AT_MOST);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			setParentScrollAble(false);
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void setParentScrollAble(boolean flag) {
		parentScrollView.requestDisallowInterceptTouchEvent(!flag);
	}

}
