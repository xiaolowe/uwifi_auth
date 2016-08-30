package com.kdgz.uwifi.auth.bean;

public class AdLog {

	// AC Id
	private String acId;

	// mac地址
	private String mac;

	// 模板Id
	private String templetId;

	// 广告内容Id
	private String adId;

	// 跳转URL 
	private String url;

	// 排序
	private String sort;

	private String actionTime;

	private long actionTimeLong;

	// 商家Id
	private String busId;
	
	// 认证类型
	private String authType;

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getTempletId() {
		return templetId;
	}

	public void setTempletId(String templetId) {
		this.templetId = templetId;
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
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

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}
	
	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public static String[] getColumnArray() {

		String[] array = new String[] { "acId", "mac", "busId", "templetId",
				"adId", "url", "sort", "actionTime", "actionTimeLong", "authType" };

		return array;
	}

}
