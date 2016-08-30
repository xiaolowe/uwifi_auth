package com.kdgz.uwifi.auth.bean;

/**
 * wifidog日志Bean
 * 
 * @author lanbo
 * 
 */
public class WifidogLog {

	private String acId;
	private String clientIp;
	private String clientMac;
	private String clientToken;
	private String action;
	private int actionResult;
	private String actionTime;
	private long actionTimeLong;
	private String originalUrl;
	private String stage;
	private String incoming;
	private String outgoing;
	private String userAgent;
	private String businessId;
	private String authType;
	private String desc;
	private int mixType;

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getClientMac() {
		return clientMac;
	}

	public void setClientMac(String clientMac) {
		this.clientMac = clientMac;
	}

	public String getClientToken() {
		return clientToken;
	}

	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getActionResult() {
		return actionResult;
	}

	public void setActionResult(int actionResult) {
		this.actionResult = actionResult;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getActionTimeLong() {
		return actionTimeLong;
	}

	public void setActionTimeLong(long actionTimeLong) {
		this.actionTimeLong = actionTimeLong;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getIncoming() {
		return incoming;
	}

	public void setIncoming(String incoming) {
		this.incoming = incoming;
	}

	public String getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(String outgoing) {
		this.outgoing = outgoing;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}
	
	public int getMixType() {
		return mixType;
	}

	public void setMixType(int mixType) {
		this.mixType = mixType;
	}

	public static String[] getColumnArray() {

		String[] array = new String[] { "acId", "clientIp", "clientMac",
				"clientToken", "action", "actionResult", "actionTime",
				"actionTimeLong", "originalUrl", "stage", "incoming",
				"outgoing", "userAgent", "businessId", "authType", "desc", "mixtype" };

		return array;
	}

}
