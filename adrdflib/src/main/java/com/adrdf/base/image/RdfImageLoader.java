package com.adrdf.base.image;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.adrdf.library.R;
import com.adrdf.base.asynctask.RdfTask;
import com.adrdf.base.asynctask.RdfTaskItem;
import com.adrdf.base.asynctask.RdfTaskObjectListener;
import com.adrdf.base.cache.image.RdfBitmapResponse;
import com.adrdf.base.cache.image.RdfImageCacheImpl;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfStrUtil;

/**
 * Copyright © CapRobin
 *
 * Name：RdfImageLoader
 * Describe：图片下载器类
 * Date：2017-03-27 10:52:46
 * Author: CapRobin@yeah.net
 *
 */
public class RdfImageLoader {
	
    /** 上下文. */
    private Context context = null;
    
    /** 图片缓存. */
    private RdfImageCacheImpl imageCache;

    /** 显示空的资源. */
    private int emptyImageResId = -1;

    /** 显示错误的资源. */
    private int errorImageResId = -1;

    /** 显示加载的资源. */
    private int loadingImageResId = -1;

	/** 任务. */
	private List<RdfTask> taskList = null;

    /**
     * 构造图片下载器.
     *
     * @param context the context
     */
    public RdfImageLoader(Context context) {
    	this.context = context;
    	this.taskList = new ArrayList<RdfTask>();
    	this.imageCache = new RdfImageCacheImpl(context);
    }
    
    
	/**
	 * 
	 * 获得一个实例.
	 * @param context
	 * @return
	 */
	public static RdfImageLoader getInstance(Context context) {
		RdfImageLoader imageLoader = new RdfImageLoader(context);
		return imageLoader;
	}


    /**
     * 显示图片(按照原图尺寸).
     * ImageView使用android:scaleType="fitXY"属性仍旧可以充满
     * @param imageView
     * @param url
     */
    public void display(final ImageView imageView,String url){
        display(imageView,url,-1,-1);
    }

    /**
     *
     * 显示图片.
     * @param imageView  显示图片的ImageView
     * @param url   图片的网络地址
     * @param desiredWidth  ImageView的估计宽度
     * @param desiredHeight ImageView的估计高度
     */
    public void display(final ImageView imageView,final String url,final int desiredWidth,final int desiredHeight){

		//要判断这个imageView的url有变化，如果没有变化才set
		//有变化就取消，解决列表的重复利用View的问题
		imageView.setTag(R.id.image_view,url);

		download(url,desiredWidth,desiredHeight,new OnImageDownloadListener() {

            @Override
            public void onEmpty() {
				String  oldUrl = (String)imageView.getTag(R.id.image_view);
				if(url.equals(oldUrl)){
                    if(emptyImageResId > 0){
                        imageView.setImageResource(emptyImageResId);
                    }else{
                        imageView.setImageBitmap(null);
                    }

				}
            }

            @Override
            public void onLoading() {
				String  oldUrl = (String)imageView.getTag(R.id.image_view);
				if(url.equals(oldUrl)){
                    if(loadingImageResId > 0){
                        imageView.setImageResource(loadingImageResId);
                    }else{
                        imageView.setImageBitmap(null);
                    }
				}
            }

            @Override
            public void onError() {
				String  oldUrl = (String)imageView.getTag(R.id.image_view);
				if(url.equals(oldUrl)){
                    if(errorImageResId > 0){
                        imageView.setImageResource(errorImageResId);
                    }else{
                        imageView.setImageBitmap(null);
                    }
				}
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
				String  oldUrl = (String)imageView.getTag(R.id.image_view);
				if(url.equals(oldUrl)){
					imageView.setImageBitmap(bitmap);
				}

            }

		});
    }

    /**
     * 显示图片(按照原图尺寸).
     * ImageView使用android:scaleType="fitXY"属性仍旧可以充满
     * @param url
     * @param onImageDownloadListener
     */
    public void download(final String url,final OnImageDownloadListener onImageDownloadListener) {
        download(url,-1,-1,onImageDownloadListener);
    }

    /**
     *
     * 开始下载图片.
     * @param url   图片的网络地址
     * @param desiredWidth  ImageView的估计宽度
     * @param desiredHeight ImageView的估计高度
     * @param onImageDownloadListener 监听器
     */
    public void download(final String url,final int desiredWidth,final int desiredHeight,final OnImageDownloadListener onImageDownloadListener) {

		if(RdfStrUtil.isEmpty(url) || (url.indexOf("http://")==-1 && url.indexOf("https://")==-1 && url.indexOf("www.")==-1)){
			if(onImageDownloadListener!=null){
				onImageDownloadListener.onEmpty();
			}
			return;
		}

    	final String cacheKey = imageCache.getCacheKey(url, desiredWidth, desiredHeight);
    	//先看内存
    	Bitmap bitmap = imageCache.getBitmap(cacheKey);

    	if(bitmap != null){
			RdfLogUtil.i(RdfImageLoader.class, "从LRUCache中获取到的图片"+cacheKey+":"+bitmap);
    		if(onImageDownloadListener!=null){
                onImageDownloadListener.onSuccess(bitmap);
    		}
    	}else{

    		if(onImageDownloadListener!=null){
                onImageDownloadListener.onLoading();
			}
			RdfTask task = RdfTask.newInstance();
    		RdfTaskItem item = new RdfTaskItem();
            item.setListener(new RdfTaskObjectListener(){

                @Override
                public <T> void update(T entity) {
                	RdfBitmapResponse response = (RdfBitmapResponse)entity;
                	if(response == null){
                		if(onImageDownloadListener!=null){
                            onImageDownloadListener.onError();
                    	}
                	}else{
                		Bitmap bitmap = response.getBitmap();

                		if(bitmap == null){
                			if(onImageDownloadListener!=null){
                                onImageDownloadListener.onEmpty();
                        	}
                		}else {
                    		if(onImageDownloadListener!=null){
                                onImageDownloadListener.onSuccess(bitmap);
                        	}
                    	}
						RdfLogUtil.i(RdfImageLoader.class, "从网络下载到的图片"+cacheKey+":"+bitmap);
                	}

                }

    			@Override
                public RdfBitmapResponse getObject() {
                    try {
                    	return imageCache.getBitmapResponse(url, desiredWidth, desiredHeight,true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

            });

			task.execute(item);
			taskList.add(task);
    	}

    }

	/**
	 * 取消当前所有
	 */
	public void cancelCurrentTask(){
		try{
			RdfTask task = null;
			for(int i =0;i<taskList.size();i++){
				task = taskList.get(i);
				task.cancel(true);
				taskList.remove(task);
				i--;
			}
			RdfLogUtil.e("AbHttpUtil","[AbImageLoader]取消了当前任务");
			imageCache.releaseRemovedBitmap();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 监听器1
	 */
	public interface OnImageDisplayListener {
		
		/**
		 * On Empty.
		 * @param imageView
		 */
		public void onEmpty(ImageView imageView);
		
		/**
		 * On Loading.
		 * @param imageView
		 */
		public void onLoading(ImageView imageView);
		
		/**
		 * On Error.
		 * @param imageView
		 */
		public void onError(ImageView imageView);
		
		/**
		 * 
		 * On response.
		 * @param imageView
		 * @param bitmap
		 */
		public void onSuccess(ImageView imageView,Bitmap bitmap);
	}
	
	/**
	 * 监听器2
	 */
	public interface OnImageDownloadListener {
		
		/**
		 * On Empty.
		 */
		public void onEmpty();
		
		/**
		 * On Loading.
		 */
		public void onLoading();
		
		/**
		 * On Error.
		 */
		public void onError();
		
		/**
		 * 
		 * On Success.
		 * @param bitmap
		 */
		public void onSuccess(Bitmap bitmap);
	}

    public int getLoadingImageResId() {
        return loadingImageResId;
    }

    public void setLoadingImageResId(int loadingImageResId) {
        this.loadingImageResId = loadingImageResId;
    }

    public int getErrorImageResId() {
        return errorImageResId;
    }

    public void setErrorImageResId(int errorImageResId) {
        this.errorImageResId = errorImageResId;
    }

    public int getEmptyImageResId() {
        return emptyImageResId;
    }

    public void setEmptyImageResId(int emptyImageResId) {
        this.emptyImageResId = emptyImageResId;
    }
}

