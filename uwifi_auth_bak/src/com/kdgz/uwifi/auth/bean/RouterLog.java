package com.kdgz.uwifi.auth.bean;

/**
 * 固件升级日志Bean
 * 
 * @author allen
 * 
 */
public class RouterLog {

	private String code;
	private String mac;
	private String version;
	private String actionTime;
	private long actionTimeLong;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
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

	public static String[] getColumnArray() {

		String[] array = new String[] { "code", "mac", "version", "actionTime", "actionTimeLong" };

		return array;
	}

}
