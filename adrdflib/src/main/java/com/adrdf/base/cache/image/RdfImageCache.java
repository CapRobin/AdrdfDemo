package  com.adrdf.base.cache.image;

import android.graphics.Bitmap;

/**
 * Copyright © CapRobin
 *
 * Name：RdfImageCache
 * Describe：
 * Date：2017-02-21 11:42:59
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfImageCache {
	
	/**
	 * Gets the bitmap.
	 *
	 * @param cacheKey the cache key
	 * @return the bitmap
	 */
	public Bitmap getBitmap(String cacheKey);

	/**
	 * Put bitmap.
	 *
	 * @param cacheKey the cache key
	 * @param bitmap the bitmap
	 */
	public void putBitmap(String cacheKey, Bitmap bitmap);


	/**
	 * Removes the bitmap.
	 * @param cacheKey the cacheKey
	 */
	public void removeBitmap(String cacheKey);

	/**
	 * Gets the cache key.
	 *
	 * @param requestUrl the request url
	 * @param maxWidth the max width
	 * @param maxHeight the max height
	 * @return the cache key
	 */
	public String getCacheKey(String requestUrl, int maxWidth, int maxHeight);
}
