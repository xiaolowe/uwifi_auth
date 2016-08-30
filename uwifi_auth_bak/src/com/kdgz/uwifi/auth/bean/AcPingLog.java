package com.kdgz.uwifi.auth.bean;


/**
 * ac ping日志Bean
 * 
 * @author lanbo
 * 
 */
public class AcPingLog {

	private String acId;
	private String sysUptime;
	private String sysMemfree;
	private String sysLoad;
	private String wifidogUptime;
	private String pingTime;
	private long pingTimeLong;

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getSysUptime() {
		return sysUptime;
	}

	public void setSysUptime(String sysUptime) {
		this.sysUptime = sysUptime;
	}

	public String getSysMemfree() {
		return sysMemfree;
	}

	public void setSysMemfree(String sysMemfree) {
		this.sysMemfree = sysMemfree;
	}

	public String getSysLoad() {
		return sysLoad;
	}

	public void setSysLoad(String sysLoad) {
		this.sysLoad = sysLoad;
	}

	public String getWifidogUptime() {
		return wifidogUptime;
	}

	public void setWifidogUptime(String wifidogUptime) {
		this.wifidogUptime = wifidogUptime;
	}


	public String getPingTime() {
		return pingTime;
	}

	public void setPingTime(String pingTime) {
		this.pingTime = pingTime;
	}

	public long getPingTimeLong() {
		return pingTimeLong;
	}

	public void setPingTimeLong(long pingTimeLong) {
		this.pingTimeLong = pingTimeLong;
	}

	public static String[] getColumnArray() {

		String[] array = new String[] { "acId", "sysUptime", "sysMemfree",
				"sysLoad", "wifidogUptime", "pingTime", "pingTimeLong" };

		return array;
	}

}
