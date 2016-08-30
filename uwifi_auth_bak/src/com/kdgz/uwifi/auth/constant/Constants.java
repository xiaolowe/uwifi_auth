package com.kdgz.uwifi.auth.constant;

/**
 * 定数类
 * 
 * @author lanbo
 * 
 */
public final class Constants {

	// 操作成功
	public static final String OPERATION_SUCCEED = "1";

	// 操作失败
	public static final String OPERATION_FAILED = "0";

	public static final String DATETIME_FORMAT_YMD = "yyyy-MM-dd";

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATETIME_FORMAT_SHORT = "yyyyMMddHHmm";

	// 模板类型：认证页
	public static final int TEMPTYPE_AUTH = 1;

	// 模板类型：广告页首页
	public static final int TEMPTYPE_AD = 2;

	// 模板类型：广告页列表页
	public static final int TEMPTYPE_AD_SUB = 3;

	// 模板类型：广告页详情页
	public static final int TEMPTYPE_AD_ET = 4;

	// DDWRT
	public static final String AC_TYPE_DDWRT = "1";

	// OPENWRT
	public static final String AC_TYPE_OPENWRT = "2";

	// 白名单
	public static final int WHTYPE_WHITE = 0;

	// 黑名单
	public static final int WHTYPE_BLACK = 1;

	// 名单类型：域名
	public static final int CTLTYPE_DOMAIN = 1;

	// 名单类型：IP
	public static final int CTLTYPE_IP = 2;

	// 名单类型：MAC
	public static final int CTLTYPE_MAC = 3;
	
	// 认证方式:一键上网
	public static final int AUTHTYPE_ONKEY = 0;
	
	// 认证方式:短信
	public static final int AUTHTYPE_SMS = 1;
	
	// 认证方式:微信
	public static final int AUTHTYPE_WEIXIN = 2;
	
	// 认证方式:多认证
	public static final int AUTHTYPE_MULTI = 3;
	
	// 认证后行为:跳转到广告页面
	public static final int AFTERAUTH_PORTAL = 1;
		
	// 认证后行为:跳转到指定页面
	public static final int AUTHTYPE_WEBSITE = 2;

	// 数据Ehcache cachename
	public static final String DATACACHE = "dataCache";
	
	
	// 逗号
	public static final String COMMA = ",";
	
	// 分号
	public static final String SEMICOLON = ";";
	
	// 冒号
	public static final String COLON = ":";
	
	//认证页下载apps
	public static final int DOWNLOADAPP = 0;

}
