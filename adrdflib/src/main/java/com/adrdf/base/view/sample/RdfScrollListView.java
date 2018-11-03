package com.adrdf.base.view.sample;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.view.listener.RdfOnScrollListener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfScrollListView
 * Describe：有滚动距离返回的ListView
 * Date：2018-06-27 11:29:46
 * Author: CapRobin@yeah.net
 *
 */
public class RdfScrollListView extends ListView {

    private RdfOnScrollListener onScrollListener;

    private int lastScrollY;

    private int headerHeight = 0;

    public RdfScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滚动监听器
     * @param onScrollListener
     */
    public void setOnScrollListener(RdfOnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    /**
     * 用于用户手指离开ScrollView的时候获取ScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            int scrollY = getListScrollY();
            RdfLogUtil.e("scrollY","scrollY:"+scrollY);
            if(scrollY < 0){
                scrollY = 0;
            }

            if(msg.what==1){
                if(onScrollListener != null && lastScrollY != scrollY){

                    onScrollListener.onScrollY(scrollY);

                    lastScrollY = scrollY;
                    //惯性问题
                    handler.sendMessageDelayed(handler.obtainMessage(1), 20);

                }else{
                    lastScrollY = scrollY;
                }

            }else if(msg.what==2){
                if(onScrollListener != null && lastScrollY != scrollY){
                    onScrollListener.onScrollY(scrollY);
                }
                lastScrollY = scrollY;

                //惯性问题
                handler.sendMessageDelayed(handler.obtainMessage(1), 20);
            }

        }

    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastScrollY = getListScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                handler.sendMessage(handler.obtainMessage(1));
                break;
            case MotionEvent.ACTION_UP:
                handler.sendMessage(handler.obtainMessage(2));
                break;
        }
        return super.onTouchEvent(ev);
    }

    public int getListScrollY() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();

        int top = c.getTop();
        Log.e("TAG",firstVisiblePosition + "," + top);
        int newScrollY = -top + firstVisiblePosition * c.getHeight();
        if(headerHeight > 0 && firstVisiblePosition > 0){
            return newScrollY + headerHeight;
        }else{
            return newScrollY;
        }

    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }
}
