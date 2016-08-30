package com.kdgz.uwifi.auth.bean;

import java.io.Serializable;

/**
 * 登录认证参数
 * 
 * @author lanbo
 * 
 */
public class LoginParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2314960963956569553L;
	private String gw_address;
	private String gw_port;
	private String gw_id;
	private String url;
	private String dev_id;
	private String mac;
	private String token;
	private int authType;
	private int businessId;
	private int key;

	public String getGw_address() {
		return gw_address;
	}

	public void setGw_address(String gwAddress) {
		gw_address = gwAddress;
	}

	public String getGw_port() {
		return gw_port;
	}

	public void setGw_port(String gwPort) {
		gw_port = gwPort;
	}

	public String getGw_id() {
		return gw_id;
	}

	public void setGw_id(String gwId) {
		gw_id = gwId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDev_id() {
		return dev_id;
	}

	public void setDev_id(String devId) {
		dev_id = devId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public int getBusinessId() {
		return businessId;
	}

	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

}