package com.adrdf.base.cache.disk;

import java.util.Collections;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：RdfDiskCacheEntry
 * Describe：缓存数据实体
 * Date：2017-06-17 02:07:25
 * Author: CapRobin@yeah.net
 *
 */
public class RdfDiskCacheEntry {


    /** 缓存数据. */
    public byte[] data;

    /** ETag. */
    public String etag;

    /** 缓存时间 总毫秒数. */
    public long serverTimeMillis;

    /** 失效日期 总毫秒数. */
    public long expiredTimeMillis;

    /** 响应头信息. */
    public Map<String, String> responseHeaders = Collections.emptyMap();

    /**
     *  是否记录已经失效.
     *
     * @return true, if is expired
     */
    public boolean isExpired() {
        return this.expiredTimeMillis < System.currentTimeMillis();
    }

    /**
     * 是否记录已经失效
     * @param cacheTimeMillis  指定一个失效间隔即 多少毫秒后失效
     * @return
     */
    public boolean isExpired(long cacheTimeMillis) {
        if(cacheTimeMillis < 0){
            return this.expiredTimeMillis < System.currentTimeMillis();
        }else if(cacheTimeMillis == 0){
            //强制刷新
            return true;
        }
        return (this.serverTimeMillis + cacheTimeMillis) < System.currentTimeMillis();
    }

}
