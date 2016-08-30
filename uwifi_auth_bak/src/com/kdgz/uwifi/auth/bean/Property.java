package com.kdgz.uwifi.auth.bean;

import java.io.Serializable;

/**
 * 共通配置参数
 * 
 * @author lanbo
 * 
 */
public class Property implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6181442616003793508L;

	private String csvLogPath;

	private String defaultAuthTpl;

	private String fileProxyDomain;

	private String defaultPortalUrl;

	private String fileProxyIp;

	private String weixinUrl;

	public String getCsvLogPath() {
		return csvLogPath;
	}

	public void setCsvLogPath(String csvLogPath) {
		this.csvLogPath = csvLogPath;
	}

	public String getDefaultAuthTpl() {
		return defaultAuthTpl;
	}

	public void setDefaultAuthTpl(String defaultAuthTpl) {
		this.defaultAuthTpl = defaultAuthTpl;
	}

	public String getFileProxyDomain() {
		return fileProxyDomain;
	}

	public void setFileProxyDomain(String fileProxyDomain) {
		this.fileProxyDomain = fileProxyDomain;
	}

	public String getDefaultPortalUrl() {
		return defaultPortalUrl;
	}

	public void setDefaultPortalUrl(String defaultPortalUrl) {
		this.defaultPortalUrl = defaultPortalUrl;
	}

	public String getFileProxyIp() {
		return fileProxyIp;
	}

	public void setFileProxyIp(String fileProxyIp) {
		this.fileProxyIp = fileProxyIp;
	}

	public String getWeixinUrl() {
		return weixinUrl;
	}

	public void setWeixinUrl(String weixinUrl) {
		this.weixinUrl = weixinUrl;
	}

}
