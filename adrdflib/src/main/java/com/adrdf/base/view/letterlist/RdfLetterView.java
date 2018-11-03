package com.adrdf.base.view.letterlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.adrdf.base.util.RdfViewUtil;
import com.adrdf.base.view.listener.RdfOnItemSelectListener;
/**
 * Copyright © CapRobin
 *
 * Name：RdfLetterView
 * Describe：字母条
 * Date：2017-03-12 14:16:37
 * Author: CapRobin@yeah.net
 *
 */

public class RdfLetterView extends View {

    /** 字母数组. */
    private char[] letters;

    /** 绘制. */
    private Paint paint;

    /** 宽度. */
    private float widthCenter;

    /** 字母之间的间距. */
    private float singleHeight;

    /** 点击后的背景颜色. */
    private GradientDrawable gradientDrawable = null;

    /** 选择事件监听器. */
    private RdfOnItemSelectListener onItemSelectListener;

    /**
     * 构造函数.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public RdfLetterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 构造函数.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public RdfLetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 构造函数.
     *
     * @param context the context
     */
    public RdfLetterView(Context context) {
        super(context);
        init();
    }

    /**
     * 初始化.
     */
    private void init() {
        letters = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '#' };
        paint = new Paint();
        paint.setColor(Color.parseColor("#949494"));
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(RdfViewUtil.scaleTextValue(this.getContext(),25));
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int []{0x99B0B0B0,0x99B0B0B0});
        gradientDrawable.setCornerRadius(30);

    }

    /**
     * 绘制
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float height = getHeight();
        singleHeight = height / letters.length;
        widthCenter = getMeasuredWidth() / (float) 2;
        for (int i = 0; i < letters.length; i++) {
            canvas.drawText(String.valueOf(letters[i]), widthCenter, singleHeight
                    + (i * singleHeight), paint);
        }

    }


    /**
     * 触屏事件
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int eventY = (int) event.getY();
        int div = (int) singleHeight;
        int position = 0;
        if (div != 0) {
            position = eventY / div;
        }
        if (position >= letters.length) {
            position = letters.length - 1;
        } else if (position < 0) {
            position = 0;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                break;
            case MotionEvent.ACTION_DOWN:
                setBackgroundDrawable(gradientDrawable);
                onItemSelectListener.onSelect(position);
                break;
            case MotionEvent.ACTION_MOVE:
                onItemSelectListener.onSelect(position);

        }
        return true;
    }

    /**
     * 获取监听器
     * @return
     */
    public RdfOnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    /**
     * 设置监听器
     * @param onItemSelectListener
     */
    public void setOnItemSelectListener(RdfOnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * 获取这个位置的字符
     * @param position
     * @return
     */
    public char getLetter(int position) {
        return letters[position];
    }
}
