package com.kdgz.uwifi.auth.constant;

public class ActionType {
	public static final Integer YES = 1; // 成功
	public static final Integer NO = 0; // 失败
	public static final String LOGIN = "login"; // 登录界面
	public static final String LOGIN_PAGE = "loginPage"; // 登录界面
	public static final String LOGIN_VALIDATE = "validate"; // 验证
	public static final String GETCODE = "getcode"; // 获取验证码
	public static final String AD_PAGE = "adpage"; // 广告页
	public static final String AD_CLICK = "adclick"; // 广告页点击
	public static final String AUTH = "auth"; // 调用auth接口
	public static final String PORTAL = "portal"; // 调用portal接口
	public static final String PING = "ping"; // 调用ping接口

	/**
	 * auth 状态
	 */
	public static final Integer ERROR = -1; // 认证错误
	public static final Integer DENIED = 0; // 拒绝认证
	public static final Integer ALLOWED = 1; // 认证通过
	public static final Integer VALIDATION = 5; // 正在认证
	public static final Integer VALIDATION_FAILED = 6; // 正在认证失败
	public static final Integer LOCKED = 254; // 认证锁定状态

	/**
	 * stage状态
	 */
	public static final String STAGE_COUNTER = "counter";
	public static final String STAGE_login = "login";
	public static final String STAGE_logout = "logout";

}
