package com.adrdf.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView.ScaleType;

import com.adrdf.test.model.ImageInfo;
import com.adrdf.library.R;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskMultiQueue;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.cache.image.RdfImageCacheImpl;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfStrUtil;
import com.adrdf.base.view.listener.RdfOnClickListener;
import com.adrdf.base.view.sample.RdfFilterImageView;

import java.io.File;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：AlbumGridAdapter
 * Describe：适配器 网络URL的图片和本地,资源文件图片显示.
 * Date：2018-02-17 12:00:17
 * Author: CapRobin@yeah.net
 *
 */
public class AlbumGridAdapter extends BaseAdapter {

	/** 上下问. */
	private Context context;

	/** 图片的路径. */
	private List<ImageInfo> images = null;

	/** 图片宽度. */
	private int width;

	/** 图片高度. */
	private int height;

	/** 滑动中. */
	private boolean fling = false;

	/**图片下载器*/
	private RdfImageLoader imageLoader = null;
	private RdfImageCacheImpl imageCache = null;
	private RdfTaskMultiQueue task;
	private AdapterView.OnItemClickListener onItemClickListener;

	public AlbumGridAdapter(Context context, List<ImageInfo> images, int width, int height,RdfImageLoader imageLoader) {
		this.context = context;
		this.images = images;
		this.width = width;
		this.height = height;
		this.imageLoader = imageLoader;
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
        if(convertView == null){
            holder = new ViewHolder();

            RdfFilterImageView imageView = new RdfFilterImageView(context);
            imageView.setBackgroundResource(R.color.gray_light);
            imageView.setLayoutParams(new GridView.LayoutParams(width, height));
            imageView.setScaleType(ScaleType.CENTER_CROP);
            holder.imageView = imageView;

            convertView = imageView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

		if(!fling){

			if(holder.tag != position){
				holder.imageView.setImageBitmap(null);
				loadImage(position,holder);
			}

		}else{
			holder.imageView.setImageBitmap(null);
		}

		holder.imageView.setOnClickListener(new  RdfOnClickListener(){
			@Override
			public void onClick(View view) {
				if(onItemClickListener!=null){
					onItemClickListener.onItemClick(null,view,position,position);
				}
			}
		});

		holder.tag = position;

        return convertView;
	}


	/**
	 * 增加并改变视图.
	 * @param position the position
	 * @param imagePaths the image paths
	 */
	public void addItem(int position,ImageInfo imagePaths) {
		images.add(position,imagePaths);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 * @param image the image paths
	 */
	public void addItems(List<ImageInfo> image) {
		images.addAll(image);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 */
	public void clearItems() {
		images.clear();
		notifyDataSetChanged();
	}

	/**
	 * View元素.
	 */
	public static class ViewHolder {
		public RdfFilterImageView imageView;
		public String imageTag;
		int tag = -1;
	}

	public AdapterView.OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public boolean isFling() {
		return fling;
	}

	public void setFling(boolean fling) {
		this.fling = fling;
	}

    public void loadImage(final int position,final ViewHolder holder){

        final ImageInfo imageInfo = images.get(position);
        final String image = imageInfo.getThumbnailsPath();

        final String currentTag = image+"_"+position;

        //设置标记
        holder.imageTag = currentTag;

        final String cacheKey = image + "w" + width + "h" + height;

        if(!RdfStrUtil.isEmpty(image)){
            if(image.indexOf("http://")!=-1){
                //图片的下载
                imageLoader.display(holder.imageView,image,this.width,this.height);

            }else if(RdfStrUtil.isNumber(image)){
                //索引图片
                try {
                    int res  = Integer.parseInt(image);
                    holder.imageView.setImageDrawable(context.getResources().getDrawable(res));
                } catch (Exception e) {
                }
            }else {

                Bitmap bitmap = this.imageCache.getBitmap(cacheKey);

                if (bitmap != null) {
                    //AbLogUtil.e("TAG", "从缓存中取到了数据" + position);
                    holder.imageView.setImageBitmap(bitmap);
                } else {

                    //AbLogUtil.e("TAG", "从缓存中没取到数据" + position);
                    final RdfTaskItem item = new RdfTaskItem();
                    item.setPosition(position);
                    item.setListener(new RdfTaskObjectListener() {
                        @Override
                        public <T> T getObject() {
                            Bitmap bitmap = RdfImageUtil.getThumbnail(new File(image),width, height);
							return (T) bitmap;
                        }

                        @Override
                        public <T> void update(T t) {
                            if(t == null){
                                return;
                            }
                            Bitmap bitmap = (Bitmap) t;
							RdfLogUtil.e("TAG", "显示：" + position+","+bitmap.getWidth()+"x"+bitmap.getHeight());
                            imageCache.putBitmap(cacheKey, bitmap);

                            if (currentTag.equals(holder.imageTag)) {
                                holder.imageView.setImageBitmap(bitmap);
                                //AbLogUtil.e("TAG", "显示：" + position+","+bitmap);
                            }

                        }
                    });
                    task.execute(item);
                }

            }
        }



    }

}
