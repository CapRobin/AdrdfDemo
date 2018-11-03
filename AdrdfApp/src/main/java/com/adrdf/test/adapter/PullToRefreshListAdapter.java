package com.adrdf.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adrdf.test.R;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskMultiQueue;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.cache.image.RdfImageCacheImpl;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfFileUtil;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfStrUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：PullToRefreshListAdapter
 * Describe：适配器 网络URL的图片和本地,资源文件图片显示.
 * Date：2018-01-18 19:50:18
 * Author: CapRobin@yeah.net
 *
 */
public class PullToRefreshListAdapter extends BaseAdapter {

	/** 上下文. */
	private Context context;

	/** 图片的路径. */
	private List<Map<String, Object>> images = null;

	/** 图片宽度. */
	private int width;

	/** 图片高度. */
	private int height;

	/**图片下载器*/
	private RdfImageLoader imageLoader = null;
	private RdfImageCacheImpl imageCache = null;
	private RdfTaskMultiQueue task;

	public PullToRefreshListAdapter(Context context, List<Map<String, Object>> images, int width, int height) {
		this.context = context;
		this.images = images;
		this.width = width;
		this.height = height;
		this.imageLoader = new RdfImageLoader(context);
		this.imageCache = new RdfImageCacheImpl(context);
		this.task = RdfTaskMultiQueue.getInstance();
	}

	/**
	 * 获取数量.
	 * @return the count
	 */
	public int getCount() {
		return images.size();
	}

	/**
	 * 获取索引位置的路径.
	 * @param position the position
	 * @return the item
	 */
	public Object getItem(int position) {
		return images.get(position);
	}

	/**
	 * 获取位置.
	 * @param position the position
	 * @return the item id
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 显示View.
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			holder.title = (TextView) convertView.findViewById(R.id.item_title);
			holder.intro = (TextView) convertView.findViewById(R.id.item_intro);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Map<String, Object> map = images.get(position);
		final String image = (String)map.get("itemsIcon");
        holder.title.setText((String)map.get("itemsTitle"));
        holder.intro.setText((String)map.get("itemsText"));

		RdfLogUtil.i(this.context,position+"路径："+image);

		final String currentTag = image+"_"+position;

		String oldTag = (String)holder.icon.getTag(R.id.image_view);

		final String cacheKey = image + "w" + width + "h" + height;

		//标记变化才执行
		if(!currentTag.equals(oldTag)){

			holder.icon.setImageBitmap(null);
			holder.icon.setImageDrawable(null);

			//设置标记
			holder.icon.setTag(R.id.image_view,currentTag);

			if(!RdfStrUtil.isEmpty(image)){
				if(image.indexOf("http://")!=-1){
					//图片的下载
					imageLoader.display(holder.icon,image,this.width,this.height);

				}else if(RdfStrUtil.isNumber(image)){
					//索引图片
					try {
						int res  = Integer.parseInt(image);
						holder.icon.setImageDrawable(context.getResources().getDrawable(res));
					} catch (Exception e) {
					}
				}else {

					Bitmap bitmap = this.imageCache.getBitmap(cacheKey);

					if (bitmap != null) {
						RdfLogUtil.i("TAG", "从缓存中取到了数据" + position);
						holder.icon.setImageBitmap(bitmap);
					} else {

						RdfLogUtil.i("TAG", "从缓存中没取到数据" + position);
						final RdfTaskItem item = new RdfTaskItem();
						item.setPosition(position);
						item.setListener(new RdfTaskObjectListener() {
							@Override
							public <T> T getObject() {
								Bitmap bitmap = RdfFileUtil.getBitmapFromSD(new File(image), RdfImageUtil.SCALEIMG, width, height);
								return (T) bitmap;
							}

							@Override
							public <T> void update(T t) {
								if(t == null){
									return;
								}
								Bitmap bitmap = (Bitmap) t;
								imageCache.putBitmap(cacheKey, bitmap);
								if (currentTag.equals(holder.icon.getTag(R.id.image_view))) {
									holder.icon.setImageBitmap(bitmap);
								}

							}
						});
						task.execute(item);
					}
				}
			}

		}

		return convertView;
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView intro;
	}

}
