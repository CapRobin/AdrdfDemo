
package com.adrdf.base.view.cropimage;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
/**
 * Copyright © CapRobin
 *
 * Name：RdfCropImageView
 * Describe：支持裁剪框的图片显示.
 * Date：2017-06-22 20:12:32
 * Author: CapRobin@yeah.net
 *
 */
public class RdfCropImageView extends RdfScaleImageView {

    private Context context;
    
    /** 全部View  包括人脸框. */
    public ArrayList<RdfHighlightView> highlightViews = new ArrayList<RdfHighlightView>();
    
    /** 正在操作的AbHighlightView. */
    private RdfHighlightView motionHighlightView = null;
    
    /** The m last y. */
    float mLastX, mLastY;
    
    /** The m motion edge. */
    int mMotionEdge;

    /** The m crop image. */
    private RdfCropHelper cropHelper;

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public RdfCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bitmapDisplayed.getBitmap() != null) {
            for (RdfHighlightView hv : highlightViews) {
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    @Override
    protected void zoomTo(float scale, float centerX, float centerY) {
        super.zoomTo(scale, centerX, centerY);
        for (RdfHighlightView hv : highlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void zoomIn() {
        super.zoomIn();
        for (RdfHighlightView hv : highlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void zoomOut() {
        super.zoomOut();
        for (RdfHighlightView hv : highlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    @Override
    protected void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < highlightViews.size(); i++) {
            RdfHighlightView hv = highlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    private void recomputeFocus(MotionEvent event) {
        for (int i = 0; i < highlightViews.size(); i++) {
            RdfHighlightView hv = highlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }

        for (int i = 0; i < highlightViews.size(); i++) {
            RdfHighlightView hv = highlightViews.get(i);
            int edge = hv.getHit(event.getX(), event.getY());
            if (edge != RdfHighlightView.GROW_NONE) {
                if (!hv.hasFocus()) {
                    hv.setFocus(true);
                    hv.invalidate();
                }
                break;
            }
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (cropHelper.saving) {
            return false;
        }

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (cropHelper.waitingToPick) {
                recomputeFocus(event);
            } else {
                for (int i = 0; i < highlightViews.size(); i++) { 
                    RdfHighlightView hv = highlightViews.get(i);
                    int edge = hv.getHit(event.getX(), event.getY());
                    if (edge != RdfHighlightView.GROW_NONE) {
                        mMotionEdge = edge;
                        motionHighlightView = hv;
                        mLastX = event.getX();
                        mLastY = event.getY();
                        motionHighlightView.setMode((edge == RdfHighlightView.MOVE) ? RdfHighlightView.ModifyMode.Move
                                : RdfHighlightView.ModifyMode.Grow);
                        break;
                    }
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            if (cropHelper.waitingToPick) {
                for (int i = 0; i < highlightViews.size(); i++) {
                    RdfHighlightView hv = highlightViews.get(i);
                    if (hv.hasFocus()) {
                        cropHelper.highlightView = hv;
                        for (int j = 0; j < highlightViews.size(); j++) {
                            if (j == i) { 
                                continue;
                            }
                            highlightViews.get(j).setHidden(true);
                        }
                        centerBasedOnHighlightView(hv);
                        cropHelper.waitingToPick = false;
                        return true;
                    }
                }
            } else if (motionHighlightView != null) {
                centerBasedOnHighlightView(motionHighlightView);
                motionHighlightView.setMode(RdfHighlightView.ModifyMode.None);
            }
            motionHighlightView = null;
            break;
        case MotionEvent.ACTION_MOVE:
            if (cropHelper.waitingToPick) {
                recomputeFocus(event);
            } else if (motionHighlightView != null) {
                motionHighlightView.handleMotion(mMotionEdge, event.getX() - mLastX, event.getY() - mLastY);
                mLastX = event.getX();
                mLastY = event.getY();

                if (true) {
                    ensureVisible(motionHighlightView);
                }
            }
            break;
        }

        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            center(true, true);
            break;
        case MotionEvent.ACTION_MOVE:
            center(true, true);
            break;
        }

        return true;
    }


    private void ensureVisible(RdfHighlightView hv) {
        Rect r = hv.mDrawRect;

        int panDeltaX1 = Math.max(0, getLeft() - r.left);
        int panDeltaX2 = Math.min(0, getRight() - r.right);

        int panDeltaY1 = Math.max(0, getTop() - r.top);
        int panDeltaY2 = Math.min(0, getBottom() - r.bottom);

        int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    private void centerBasedOnHighlightView(RdfHighlightView hv) {
        Rect drawRect = hv.mDrawRect;
        
        float width = drawRect.width();
        float height = drawRect.height();

        float thisWidth = getWidth();
        float thisHeight = getHeight();

        float z1 = thisWidth / width * .6F;
        float z2 = thisHeight / height * .6F;

        float zoom = Math.min(z1, z2);
        zoom = zoom * this.getScale();
        zoom = Math.max(1F, zoom);//assure getScale()>1

        if ((Math.abs(zoom - getScale()) / zoom) > 0.1) {
            float[] coordinates = new float[] { hv.mCropRect.centerX(), hv.mCropRect.centerY() };
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300F); // CR: 300.0f.
        }

        ensureVisible(hv);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < highlightViews.size(); i++) {
            highlightViews.get(i).draw(canvas);
        }
    }

    public void add(RdfHighlightView hv) {
    	highlightViews.clear();
        highlightViews.add(hv);
        invalidate();
    }

    public void setCropHelper(RdfCropHelper cropHelper){
        this.cropHelper = cropHelper;
    }

    public void resetView(Bitmap b){
    	setImageBitmap(b);
		setImageBitmapResetBase(b, true);
		setImageMatrix(getImageViewMatrix());
		int width = bitmapDisplayed.getWidth();
        int height = bitmapDisplayed.getHeight();
        Rect imageRect = new Rect(0, 0, width, height);
        int cropWidth = Math.min(width, height) * 4 / 5;
        int cropHeight = cropWidth;
        int x = (width - cropWidth) / 2;
        int y = (height - cropHeight) / 2;
        RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
        RdfHighlightView hv = new RdfHighlightView(this);
        hv.setup(getImageViewMatrix(), imageRect, cropRect, false, true);
        hv.setFocus(true);
		add(hv);
		centerBasedOnHighlightView(hv);
		hv.setMode(RdfHighlightView.ModifyMode.None);
		center(true, true);
		invalidate();
    }

}
