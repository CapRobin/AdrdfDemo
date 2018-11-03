package com.adrdf.base.cache.disk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.SystemClock;

import com.adrdf.base.cache.http.RdfHttpCacheResponse;
import com.adrdf.base.config.RdfAppConfig;
import com.adrdf.base.util.RdfAppUtil;
import com.adrdf.base.util.RdfDateUtil;
import com.adrdf.base.util.RdfFileUtil;
import com.adrdf.base.util.RdfLogUtil;
import com.adrdf.base.util.RdfStrUtil;
import com.adrdf.base.util.RdfStreamUtil;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Copyright © CapRobin
 *
 * Name：RdfDiskCacheImpl
 * Describe：磁盘缓存实现类
 * Date：2017-06-17 02:08:07
 * Author: CapRobin@yeah.net
 *
 */
public class RdfDiskCacheImpl implements RdfDiskCache {

    /**  单例. */
    private static RdfDiskCacheImpl diskCache;

    /**  所有缓存文件. */
    private final Map<String, CacheHeader> mEntries = new LinkedHashMap<String, CacheHeader>(16, .75f, true);

    /** 当前缓存大小. */
    private long mTotalSize = 0;

    /** 缓存根目录. */
    private File cacheDir;

    /** 最大缓存字节数. */
    private final int mMaxCacheSizeInBytes;

    /**  缓存达到高水品的百分比. */
    private static final float HYSTERESIS_FACTOR = 0.9f;

    /** 文件头标识. */
    private static final int CACHE_MAGIC = 0x20120504;

    /**
     * 构造.
     */
    public RdfDiskCacheImpl(Context context) {

        PackageInfo info = RdfAppUtil.getPackageInfo(context);
        if(!RdfFileUtil.isCanUseSD()){
            cacheDir = new File(context.getCacheDir(), info.packageName);
        }else{
            cacheDir = new File(RdfFileUtil.getCacheDownloadDir(context));
        }
        mMaxCacheSizeInBytes =  RdfAppConfig.MAX_DISK_USAGE_INBYTES;
        initialize();
    }

    /**
     *
     * 获得一个实例.
     * @param context
     * @return
     */
    public static RdfDiskCacheImpl getInstance(Context context) {
        if(diskCache == null){
            diskCache = new RdfDiskCacheImpl(context);
        }
        return diskCache;
    }

    /**
     * 初始化磁盘缓存文件.
     */
    @Override
    public synchronized void initialize() {
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                RdfLogUtil.e(RdfDiskCacheImpl.class,"缓存目录创建失败，"+cacheDir.getAbsolutePath());
            }
            return;
        }

        File[] files = cacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                CacheHeader entry = CacheHeader.readHeader(fis);
                entry.size = file.length();
                putEntry(entry.key, entry);
            } catch (Exception e) {
                if (file != null) {
                   file.delete();
                }
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) { 
                }
            }
        }
    }

    /**
     * 清空所有磁盘缓存.
     */
    @Override
    public synchronized void clear() {
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        mEntries.clear();
        mTotalSize = 0;
        RdfLogUtil.d(RdfDiskCacheImpl.class,"Cache cleared.");
    }

    /**
     * 获取缓存实体.
     *
     * @param key the key
     * @return the entry
     */
    @Override
    public synchronized RdfDiskCacheEntry get(String key) {
        CacheHeader entry = mEntries.get(key);
        // if the entry does not exist, return.
        if (entry == null) {
            return null;
        }

        File file = getFileForKey(key);
        RdfLogUtil.d(RdfDiskCacheImpl.class, "想要从缓存中获取文件"+file.getAbsolutePath());
        CountingInputStream cis = null;
        try {
            cis = new CountingInputStream(new FileInputStream(file));
            CacheHeader.readHeader(cis); // eat header
            byte[] data = RdfStreamUtil.stream2Bytes(cis, (int) (file.length() - cis.bytesRead));
            return entry.toCacheEntry(data);
        } catch (Exception e) {
        	e.printStackTrace();
            remove(key);
            return null;
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (Exception ioe) {
                	ioe.printStackTrace();
                    return null;
                }
            }
        }
    }

    /**
     * 添加实体到缓存.
     *
     * @param key the key
     * @param entry the entry
     */
    @Override
    public synchronized void put(String key, RdfDiskCacheEntry entry) {
        pruneIfNeeded(entry.data.length);
        File file = getFileForKey(key);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            CacheHeader e = new CacheHeader(key, entry);
            e.writeHeader(fos);
            fos.write(entry.data);
            fos.close();
            putEntry(key, e);
            return;
        } catch (IOException e) {
        }
        boolean deleted = file.delete();
        if (!deleted) {
            RdfLogUtil.d(RdfDiskCacheImpl.class,"缓存文件删除失败"+file.getAbsolutePath());
        }
    }

    /**
     * 从缓存中移除实体.
     *
     * @param key the key
     */
    @Override
    public synchronized void remove(String key) {
        boolean deleted = getFileForKey(key).delete();
        removeEntry(key);
        if (!deleted) {
            RdfLogUtil.d(RdfDiskCacheImpl.class,"缓存文件删除失败");
        }
    }

    /**
     * 从key中生成文件名.
     * @param key The key to generate a file name for.
     * @return A pseudo-unique filename.
     */
    private String getFileNameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    /**
     * 从key中得到文件.
     *
     * @param key the key
     * @return the file for key
     */
    public File getFileForKey(String key) {
        return new File(cacheDir, getFileNameForKey(key));
    }

    /**
     * Prunes the cache to fit the amount of bytes specified.
     * @param neededSpace The amount of bytes we are trying to fit into the cache.
     */
    private void pruneIfNeeded(int neededSpace) {
    	//可以缓存
        if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes) {
            return;
        }

        //释放部分空间
        long before = mTotalSize;
        int prunedFiles = 0;
        long startTime = SystemClock.elapsedRealtime();

        Iterator<Map.Entry<String, CacheHeader>> iterator = mEntries.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CacheHeader> entry = iterator.next();
            CacheHeader e = entry.getValue();
            //删除
            boolean deleted = getFileForKey(e.key).delete();
            if (deleted) {
                mTotalSize -= e.size;
            } else {
               RdfLogUtil.d(RdfDiskCacheImpl.class,"Could not delete cache entry for key=%s, filename=%s",
                       e.key, getFileNameForKey(e.key));
            }
            iterator.remove();
            prunedFiles++;
            
            //删除缓存到这个级别
            if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes * HYSTERESIS_FACTOR) {
                break;
            }
        }

        if (RdfLogUtil.D) {
        	RdfLogUtil.d(RdfDiskCacheImpl.class,"pruned %d files, %d bytes, %d ms",
                    prunedFiles, (mTotalSize - before), SystemClock.elapsedRealtime() - startTime);
        }
    }

    /**
     * 将实体加入到缓存中.
     * @param key The key to identify the entry by.
     * @param entry The entry to cache.
     */
    private void putEntry(String key, CacheHeader entry) {
        if (!mEntries.containsKey(key)) {
            mTotalSize += entry.size;
        } else {
            CacheHeader oldEntry = mEntries.get(key);
            mTotalSize += (entry.size - oldEntry.size);
        }
        mEntries.put(key, entry);
    }

    /**
     * 从缓存中移除某个实体.
     *
     * @param key the key
     */
    private void removeEntry(String key) {
        CacheHeader entry = mEntries.get(key);
        if (entry != null) {
            mTotalSize -= entry.size;
            mEntries.remove(key);
        }
    }

    /**
     * 获取用于缓存的Key.
     * @param url
     * @return
     */
    public String getCacheKey(String url) {
        return new StringBuilder(url.length()).append(url).toString();
    }

    /**
     * 缓存头部信息.
     */
    static class CacheHeader {
    	
        /** 内容大小 */
        public long size;

        /** 实体的key. */
        public String key;

        /** ETag仅仅是一个和文件相关的标记. */
        public String etag;

        /** 缓存时间 总毫秒数. */
        public long serverTimeMillis;

        /** 失效日期 总毫秒数. */
        public long expiredTimeMillis;

        /** 响应头信息. */
        public Map<String, String> responseHeaders;

        /**
         * 构造.
         */
        private CacheHeader() { }

        /**
         * 构造.
         *
         * @param key The key that identifies the cache entry
         * @param entry The cache entry.
         */
        public CacheHeader(String key, RdfDiskCacheEntry entry) {
            this.key = key;
            this.size = entry.data.length;
            this.etag = entry.etag;
            this.serverTimeMillis = entry.serverTimeMillis;
            this.expiredTimeMillis = entry.expiredTimeMillis;
            this.responseHeaders = entry.responseHeaders;
        }

        /**
         * Reads the header off of an InputStream and returns a CacheHeader object.
         *
         * @param is The InputStream to read from.
         * @return the cache header
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public static CacheHeader readHeader(InputStream is) throws Exception {
            CacheHeader entry = new CacheHeader();
            int magic = RdfStreamUtil.readInt(is);
            if (magic != CACHE_MAGIC) {
                // don't bother deleting, it'll get pruned eventually
                throw new IOException();
            }
            entry.key = RdfStreamUtil.readString(is);
            entry.etag = RdfStreamUtil.readString(is);
            if (entry.etag.equals("")) {
                entry.etag = null;
            }
            entry.serverTimeMillis = RdfStreamUtil.readLong(is);
            entry.expiredTimeMillis = RdfStreamUtil.readLong(is);
            entry.responseHeaders = RdfStreamUtil.readStringStringMap(is);
            return entry;
        }

        /**
         * Creates a cache entry for the specified data.
         *
         * @param data the data
         * @return the entry
         */
        public RdfDiskCacheEntry toCacheEntry(byte[] data) {
            RdfDiskCacheEntry e = new RdfDiskCacheEntry();
            e.data = data;
            e.etag = etag;
            e.serverTimeMillis = serverTimeMillis;
            e.expiredTimeMillis = expiredTimeMillis;
            e.responseHeaders = responseHeaders;
            return e;
        }


        /**
         * Writes the contents of this CacheHeader to the specified OutputStream.
         *
         * @param os the os
         * @return true, if successful
         */
        public boolean writeHeader(OutputStream os) {
            try {
            	RdfStreamUtil.writeInt(os, CACHE_MAGIC);
            	RdfStreamUtil.writeString(os, key);
            	RdfStreamUtil.writeString(os, etag == null ? "" : etag);
            	RdfStreamUtil.writeLong(os, serverTimeMillis);
            	RdfStreamUtil.writeLong(os, expiredTimeMillis);
            	RdfStreamUtil.writeStringStringMap(responseHeaders, os);
                os.flush();
                return true;
            } catch (IOException e) {
                RdfLogUtil.d(RdfDiskCacheImpl.class,"%s", e.toString());
                return false;
            }
        }

    }

    /**
     * 从连接中获取响应信息.
     *
     * @param url the url
     * @return the cache response
     */
    public RdfHttpCacheResponse getCacheResponse(String url, String sessionId){
        URLConnection con = null;
        InputStream is = null;
        RdfHttpCacheResponse response = null;
        try {
            URL imageURL = new URL(url);
            con = imageURL.openConnection();
            con.setConnectTimeout(RdfAppConfig.DEFAULT_CONNECT_TIMEOUT);
            con.setReadTimeout(RdfAppConfig.DEFAULT_READ_TIMEOUT);
            con.setDoInput(true);
            con.setRequestProperty("Cookie", "JSESSIONID="+sessionId);
            con.connect();
            is = con.getInputStream();

            byte [] data = RdfStreamUtil.stream2Bytes(is);
            Map<String, List<String>> headers = con.getHeaderFields();
            Map<String,String> mapHeaders = new HashMap<String,String>();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {

                String key = entry.getKey();
                List<String> values = entry.getValue();
                if(RdfStrUtil.isEmpty(key)){
                    key = "adrdf";
                }
                mapHeaders.put(key, values.get(0));

            }

			/*key = null and value = [HTTP/1.1 200 OK]
		    key = Accept-Ranges and value = [bytes]
			key = Connection and value = [Keep-Alive]
			key = Content-Length and value = [4357]
			key = Content-Type and value = [image/png]
			key = Date and value = [Thu, 02 Apr 2015 10:42:54 GMT]
			key = ETag and value = ["620e07-1105-4f5d6331a2300"]
			key = Keep-Alive and value = [timeout=15, max=97]
			key = Last-Modified and value = [Sun, 30 Mar 2014 17:23:56 GMT]
			key = Server and value = [Apache]
			key = X-Android-Received-Millis and value = [1427971373392]
			key = X-Android-Sent-Millis and value = [1427971373356]*/

            response = new RdfHttpCacheResponse(data, mapHeaders);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     *
     * 将响应解析成缓存实体.
     * @param response
     * @return
     */
    public RdfDiskCacheEntry parseCacheHeaders(RdfHttpCacheResponse response, long cacheTimeMillis) {

        //{adrdflib=HTTP/1.1 200 OK, ETag="620e0d-dae-4f5d6331a2300", Date=Fri, 26 Jun 2015 02:17:54 GMT,
        //Content-Length=3502, Last-Modified=Sun, 30 Mar 2014 17:23:56 GMT, X-Android-Received-Millis=1435285072907,
        //Keep-Alive=timeout=15, max=100, Content-Type=image/png, Connection=Keep-Alive, Accept-Ranges=bytes,
        //Server=Apache, Cache-Control=max-age=600000, X-Android-Sent-Millis=1435285072809}
        Map<String, String> headers = response.headers;
        long serverTimeMillis = 0;
        long expiredTimeMillis = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;
        String serverEtag = null;
        String headerValue;

        //获取响应的内容的时间
        headerValue = headers.get("Date");
        if (headerValue != null) {
            try {
                serverTimeMillis = RdfDateUtil.getDateByFormat(headerValue, RdfDateUtil.PATTERN_RFC1123, Locale.ENGLISH).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //服务器的时间可能小于当前时间，会导致始终过期
        if(serverTimeMillis == 0  || serverTimeMillis < System.currentTimeMillis()){
            serverTimeMillis = System.currentTimeMillis();
        }

        //Cache-Control有值才使用缓存超时的设置
        headerValue = headers.get("Cache-Control");
        if (headerValue != null) {
            hasCacheControl = true;
            String[] tokens = headerValue.split(",");
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i].trim();
                if (token.equals("no-cache") || token.equals("no-store")) {
                    break;
                } else if (token.startsWith("max-age=")) {
                    try {
                        maxAge = Long.parseLong(token.substring(8));
                    } catch (Exception e) {
                    }
                } else if (token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                    maxAge = 0;
                }
            }
        }

        //服务端未设置Header缓存，才使用app的设置
        if(maxAge==0 && cacheTimeMillis > 0){
            hasCacheControl = true;
            maxAge = cacheTimeMillis;
        }

        serverEtag = headers.get("ETag");

        if (hasCacheControl) {
            expiredTimeMillis = serverTimeMillis + maxAge;
        }

        RdfDiskCacheEntry entry = new RdfDiskCacheEntry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.serverTimeMillis = serverTimeMillis;
        entry.expiredTimeMillis = expiredTimeMillis;
        entry.responseHeaders = headers;

        return entry;

    }

    /**
     * 计数.
     */
    private static class CountingInputStream extends FilterInputStream {
        
        /** The bytes read. */
        private int bytesRead = 0;

        private CountingInputStream(InputStream in) {
            super(in);
        }
        
        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result != -1) {
                bytesRead++;
            }
            return result;
        }
        
        @Override
        public int read(byte[] buffer, int offset, int count) throws IOException {
            int result = super.read(buffer, offset, count);
            if (result != -1) {
                bytesRead += result;
            }
            return result;
        }
    }

}
