package com.adrdf.base.config;

/**
 * Copyright © CapRobin
 *
 * Name：RdfAppConfig
 * Describe：开发框架设置
 * Date：2018-01-24 10:23:35
 * Author: CapRobin@yeah.net
 *
 */
public class RdfAppConfig {
	
	/**  UI设计的基准宽度. */
	public static int UI_WIDTH = 720;
	
	/**  UI设计的基准高度. */
	public static int UI_HEIGHT = 1280;
	
	/**  UI设计的密度. */
	public static int UI_DENSITY = 2;
	
	/** 默认 SharePreferences文件名. */
	public static String SHARED_PATH = "app_share";
	
	/** 默认下载文件地址. */
	public static  String DOWNLOAD_ROOT_DIR = "download";
	
    /** 默认下载图片文件地址. */
	public static  String DOWNLOAD_IMAGE_DIR = "images";
	
    /** 默认下载文件地址. */
	public static  String DOWNLOAD_FILE_DIR = "files";
	
	/** APP缓存目录. */
	public static  String CACHE_DIR = "cache";
	
	/** DB目录. */
	public static  String DB_DIR = "db";
	
	/** 默认磁盘缓存超时时间1小时设置，毫秒. */
	public static long DISK_CACHE_EXPIRES_TIME = 48*3600*1000;
	
	/** 内存缓存大小  单位10M. */
	public static int MAX_CACHE_SIZE_INBYTES = 10*1024*1024;
	
	/** 磁盘缓存大小  单位10M. */
	public static int MAX_DISK_USAGE_INBYTES = 10*1024*1024;

    /** 最大连接数. */
    public static int DEFAULT_MAX_CONNECTIONS = 10;

    /** 连接超时时间. */
    public static int DEFAULT_CONNECT_TIMEOUT = 5000;

	/** 数据传输超时时间. */
	public static int DEFAULT_READ_TIMEOUT = 20000;

    /** 重试次数. */
    public static int DEFAULT_MAX_RETRIES = 1;

    /** 缓冲大小. */
    public static int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

    /** HTTP编码. */
    public static String HTTP_ENCODE = "UTF-8";

    /** 用户代理. */
    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.43 BIDUBrowser/6.x Safari/537.31";

	/** Session ID. */
	public static String sessionId = null;

	/** 证书验证模式. 0 忽略  1 验证*/
	public static int trustMode = 0;

	/** 证书文件. */
	public static int caRes = -1;

	/** 证书密码. */
	public static String caPassword = "123456";

	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static String UNKNOWN_HOST_EXCEPTION = "远程服务连接失败";
	
	/** The Constant CONNECTEXCEPTION. */
	public static String CONNECT_EXCEPTION = "网络连接出错，请重试";
	
	/** The Constant CONNECT  SOCKET TIMEOUTEXCEPTION. */
	public static String CONNECT_TIMEOUT_EXCEPTION = "连接超时，请重试";
	
	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static String CLIENT_PROTOCOL_EXCEPTION = "HTTP请求参数错误";
	
	/** 参数个数不够. */
	public static String MISSING_PARAMETERS = "参数没有包含足够的值";
	
	/** The Constant REMOTESERVICEEXCEPTION. */
	public static String REMOTE_SERVICE_EXCEPTION = "抱歉，远程服务出错了";
	
	/** 资源未找到. */
	public static String NOT_FOUND_EXCEPTION = "请求的资源无效404";
	
	/** 没有权限访问资源. */
	public static String FORBIDDEN_EXCEPTION = "没有权限访问资源";

	/** 程序下载任务过多请稍后再试. */
	public static String THREAD_EXCEPTION = "程序下载任务过多请稍后再试";

	/** 其他异常. */
	public static String UNTREATED_EXCEPTION = "未处理的异常";

	/** debug开关. */
	public static boolean LOG_DEBUG = true;

	/** info开关. */
	public static boolean LOG_INFO = true;

	/** error开关. */
	public static boolean LOG_ERROR = true;
	

}
