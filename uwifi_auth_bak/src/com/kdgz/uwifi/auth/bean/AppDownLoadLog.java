package com.kdgz.uwifi.auth.bean;

/**
 * 应用下载日志Bean
 * 
 * @author Lanbo
 *
 */
public class AppDownLoadLog {

	// AC-Id
	private String acId;

	private int businessId;

	// 应用Id
	private int appId;

	// 应用版本
	private String version;

	private String actionTime;

	private long actionTimeLong;
	
	private String ua;

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public long getActionTimeLong() {
		return actionTimeLong;
	}

	public void setActionTimeLong(long actionTimeLong) {
		this.actionTimeLong = actionTimeLong;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public static String[] getColumnArray() {

		String[] array = new String[] { "acId", "businessId", "appId",
				"version", "actionTime", "actionTimeLong", "ua" };

		return array;
	}

}
