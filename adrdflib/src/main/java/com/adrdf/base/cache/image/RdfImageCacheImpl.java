package  com.adrdf.base.cache.image;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.adrdf.base.cache.disk.RdfDiskCacheEntry;
import com.adrdf.base.cache.disk.RdfDiskCacheImpl;
import com.adrdf.base.cache.http.RdfHttpCacheResponse;
import com.adrdf.base.config.RdfAppConfig;
import com.adrdf.base.image.RdfImageLoader;
import com.adrdf.base.util.RdfImageUtil;
import com.adrdf.base.util.RdfLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © CapRobin
 *
 * Name：RdfImageCacheImpl
 * Describe：图片缓存实现类
 * Date：2018-06-27 09:10:08
 * Author: CapRobin@yeah.net
 *
 */
public class RdfImageCacheImpl implements RdfImageCache {

    /**
     * LruCache.
     */
    private static LruCache<String, Bitmap> lruCache;

    /**
     * 待释放的bitmap.
     */
    private static List<Bitmap> releaseBitmapList;

    /**
     * 磁盘缓存.
     */
    public RdfDiskCacheImpl diskCache;

    /**
     * 构造方法.
     */
    public RdfImageCacheImpl(Context context) {
        super();
        int maxSize = RdfAppConfig.MAX_CACHE_SIZE_INBYTES;
        releaseBitmapList = new ArrayList<Bitmap>();
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                //
                RdfLogUtil.e(RdfImageCacheImpl.class, "entryRemoved key:" + key + oldValue);
                releaseBitmapList.add(oldValue);

            }

        };

        this.diskCache = RdfDiskCacheImpl.getInstance(context);
    }

    /**
     * 根据key获取缓存中的Bitmap.
     *
     * @param cacheKey the cache key
     * @return the bitmap
     */
    @Override
    public Bitmap getBitmap(String cacheKey) {
        return lruCache.get(cacheKey);
    }

    /**
     * 增加一个Bitmap到缓存中.
     *
     * @param cacheKey the cache key
     * @param bitmap   the bitmap
     */
    @Override
    public void putBitmap(String cacheKey, Bitmap bitmap) {
        lruCache.put(cacheKey, bitmap);
    }

    /**
     * 从缓存中删除一个Bitmap.
     *
     * @param cacheKey the cacheKey
     */
    @Override
    public void removeBitmap(String cacheKey) {
        lruCache.remove(cacheKey);
    }

    /**
     * 释放所有缓存.
     */
    public void clearBitmap() {
        lruCache.evictAll();
    }


    /**
     * 获取用于缓存的Key.
     *
     * @param url       the request url
     * @param maxWidth  the max width
     * @param maxHeight the max height
     * @return the cache key
     */
    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }

    /**
     * 获取AbBitmapResponse.
     *
     * @param url
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public RdfBitmapResponse getBitmapResponse(String url, int desiredWidth, int desiredHeight, boolean lruCache) {
        RdfBitmapResponse bitmapResponse = null;
        try {
            final String cacheKey = getCacheKey(url, desiredWidth, desiredHeight);
            Bitmap bitmap = null;
            //看磁盘
            RdfDiskCacheEntry entry = diskCache.get(cacheKey);
            if (entry == null || entry.isExpired()) {
                if (entry == null) {
                    RdfLogUtil.i(RdfImageLoader.class, "磁盘中没有这个图片");
                } else {
                    if (entry.isExpired()) {
                        RdfLogUtil.i(RdfImageLoader.class, "磁盘中图片已经过期");
                    }
                }

                RdfHttpCacheResponse response = diskCache.getCacheResponse(url, null);
                if (response != null && response.data != null && response.data.length > 0) {
                    bitmap = RdfImageUtil.getBitmap(response.data, desiredWidth, desiredHeight);
                    if (bitmap != null) {
                        putBitmap(cacheKey, bitmap);
                        RdfLogUtil.i(RdfImageLoader.class, "图片缓存成功");
                        diskCache.put(cacheKey, diskCache.parseCacheHeaders(response, RdfAppConfig.DISK_CACHE_EXPIRES_TIME));
                    }
                }
            } else {
                //磁盘中有
                byte[] bitmapData = entry.data;
                bitmap = RdfImageUtil.getBitmap(bitmapData, desiredWidth, desiredHeight);
                if(lruCache){
                    putBitmap(cacheKey, bitmap);
                }

            }

            bitmapResponse = new RdfBitmapResponse(url);
            bitmapResponse.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmapResponse;
    }

    /**
     * 释放已经被移除的Bitmap
     */
    public void releaseRemovedBitmap(){
        RdfImageUtil.releaseBitmapList(releaseBitmapList);
        releaseBitmapList.clear();
    }
}
