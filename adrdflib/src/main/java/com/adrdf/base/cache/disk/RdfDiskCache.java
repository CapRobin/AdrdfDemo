package com.adrdf.base.cache.disk;

/**
 * Copyright © CapRobin
 *
 * Name：RdfDiskCache
 * Describe：磁盘缓存接口
 * Date：2017-06-21 08:06:53
 * Author: CapRobin@yeah.net
 *
 */
public interface RdfDiskCache {

    /**
     * Retrieves an entry from the cache.
     * @param key Cache key
     * @return An {@link RdfDiskCacheEntry} or null in the event of a cache miss
     */
    public RdfDiskCacheEntry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     * @param key Cache key
     * @param entry Data to store and metadata for cache coherency, TTL, etc.
     */
    public void put(String key, RdfDiskCacheEntry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     */
    public void initialize();


    /**
     * Removes an entry from the cache.
     * @param key Cache key
     */
    public void remove(String key);

    /**
     * Empties the cache.
     */
    public void clear();


    /**
     * 获取缓存的Key.
     * @param url
     * @return key
     */
    public String getCacheKey(String url);



}
