package com.adrdf.base.view.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskMultiQueue;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.cache.image.RdfImageCacheImpl;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfFileUtil;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.util.RdfStrUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfPhotoImageViewPagerAdapter
 * Describe：ViewPager适配器
 * Date：2017-04-23 11:20:32
 * Author: CapRobin@yeah.net
 *
 */
public class RdfPhotoImageViewPagerAdapter extends PagerAdapter{

	/** 上下文. */
	private Context context;

	/** View列表. */
	final LinkedList<RdfPhotoImageView> viewCache = new LinkedList<RdfPhotoImageView>();

	private List<String> urlPathList = null;

	private RdfPhotoImageViewPager photoImageViewPager = null;

	/** 图片加载. */
	private RdfImageLoader imageLoader = null;

	private RdfTaskMultiQueue task;

	private RdfImageCacheImpl imageCache = null;

	/**
	 * 构造函数.
	 * @param context
	 * @param urlPathList
	 */
	public RdfPhotoImageViewPagerAdapter(Context context, RdfPhotoImageViewPager photoImageViewPager, List<String>  urlPathList, RdfImageLoader imageLoader) {
		this.context = context;
		this.urlPathList = urlPathList;
		this.photoImageViewPager = photoImageViewPager;
		this.imageLoader = imageLoader;
		this.imageCache = new RdfImageCacheImpl(context);
		this.task = RdfTaskMultiQueue.getInstance();
	}

	/**
	 * 获取数量.
	 * @return the count
	 */
	@Override
	public int getCount() {
		return this.urlPathList.size();
	}

	/**
	 * Object是否对应这个View.
	 * @param view the arg0
	 * @param obj the arg1
	 * @return true, if is view from object
	 */
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == (obj);
	}

	/**
	 * 显示View.
	 * @param container the container
	 * @param position the position
	 * @return the object
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final RdfPhotoImageView photoImageView;
		if (viewCache.size() > 0) {
			photoImageView = viewCache.remove();
			photoImageView.setImageBitmap(null);
			photoImageView.reset();
		} else {
			photoImageView = new RdfPhotoImageView(context);
		}
		final String urlPath = this.urlPathList.get(position);
		loadImage(position,photoImageView,urlPath);

		container.addView(photoImageView);
		return photoImageView;
	}

	/**
	 * 移除View.
	 * @param container the container
	 * @param position the position
	 * @param object the object
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		RdfPhotoImageView photoImageView = (RdfPhotoImageView) object;
		container.removeView(photoImageView);
		viewCache.add(photoImageView);
	}
	
	/**
	 * 很重要，否则不能notifyDataSetChanged.
	 * @param object the object
	 * @return the item position
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}


	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		final RdfPhotoImageView photoImageView = (RdfPhotoImageView) object;
		this.photoImageViewPager.setMainImageView(photoImageView);
		final String urlPath = this.urlPathList.get(position);
		loadImage(position,photoImageView,urlPath);
	}


	public void  loadImage(final int position, final RdfPhotoImageView photoImageView, final String urlPath){

		final String cacheKey = urlPath + "wall" + "hall";

		if(!RdfStrUtil.isEmpty(urlPath)) {
			if (urlPath.indexOf("http://") != -1 || urlPath.indexOf("https://") != -1 || urlPath.indexOf("www.") != -1) {
				//图片的下载
				this.imageLoader.display(photoImageView, urlPath);
			} else if (RdfStrUtil.isNumber(urlPath)) {
				//索引图片
				try {
					int res = Integer.parseInt(urlPath);
					photoImageView.setImageDrawable(context.getResources().getDrawable(res));
				} catch (Exception e) {
				}
			} else {

				Bitmap bitmap = this.imageCache.getBitmap(cacheKey);
				if (bitmap != null) {
                    photoImageView.setImageBitmap(bitmap);
				} else {
					final RdfTaskItem item = new RdfTaskItem();
					item.setListener(new RdfTaskObjectListener() {
						@Override
						public <T> T getObject() {
							Bitmap bitmap = RdfFileUtil.getBitmapFromSD(new File(urlPath), RdfImageUtil.SCALEIMG, 1280, 720);
							return (T) bitmap;
						}

						@Override
						public <T> void update(T t) {
							if(t == null){
								return;
							}
							Bitmap bitmap = (Bitmap) t;
							imageCache.putBitmap(cacheKey, bitmap);
							photoImageView.setImageBitmap(bitmap);
						}
					});
					task.execute(item);
				}

			}
		}
	}
}
