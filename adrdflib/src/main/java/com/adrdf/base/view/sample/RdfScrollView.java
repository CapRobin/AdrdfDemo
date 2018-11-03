package com.adrdf.base.view.sample;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.view.listener.RdfOnScrollListener;

/**
 * Copyright © CapRobin
 *
 * Name：RdfScrollView
 * Describe：ScrollView支持实时滚动监听
 * Date：2017-08-27 19:27:10
 * Author: CapRobin@yeah.net
 *
 */
public class RdfScrollView extends ScrollView {

    private RdfOnScrollListener onScrollListener;
    /**
     * 手指离开ScrollView，ScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public RdfScrollView(Context context) {
        this(context, null);
    }

    public RdfScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RdfScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
            int scrollY = getScrollY();
            RdfLogUtil.e("scrollY","scrollY:"+scrollY);
            if(scrollY < 0){
                scrollY = 0;
            }

            if(msg.what==1){
                if(onScrollListener != null && lastScrollY != scrollY){

                    onScrollListener.onScrollY(scrollY);

                    lastScrollY = scrollY;
                    //惯性问题
                    handler.sendMessageDelayed(handler.obtainMessage(1), 5);

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

    /**
     * 重写onTouchEvent， 当用户的手在ScrollView上面的时候，
     * 直接将ScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * ScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * ScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastScrollY = this.getScrollY();
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

    public int getLastScrollY() {
        return lastScrollY;
    }
}
