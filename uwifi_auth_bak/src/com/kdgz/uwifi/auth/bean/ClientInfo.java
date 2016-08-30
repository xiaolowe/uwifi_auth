package com.kdgz.uwifi.auth.bean;

import java.io.Serializable;

public class ClientInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4395119083235485179L;

	private String acid;

	private String mac;

	private int businessid;

	// 是否iphone用户
	private boolean iphoneFlag;

	// 最近一次认证的时间戳
	private long lastLoginTime;

	// 在线时长（分钟）
	private long onlineTime;

	// 是否已经认证
	private boolean authenticated;
	
	private int authType;

	public String getAcid() {
		return acid;
	}

	public void setAcid(String acid) {
		this.acid = acid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getBusinessid() {
		return businessid;
	}

	public void setBusinessid(int businessid) {
		this.businessid = businessid;
	}

	public boolean isIphoneFlag() {
		return iphoneFlag;
	}

	public void setIphoneFlag(boolean iphoneFlag) {
		this.iphoneFlag = iphoneFlag;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(long onlineTime) {
		this.onlineTime = onlineTime;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}
}
