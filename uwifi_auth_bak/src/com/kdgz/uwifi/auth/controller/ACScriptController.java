package com.kdgz.uwifi.auth.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.kdgz.uwifi.auth.constant.Constants;
import com.kdgz.uwifi.auth.model.AcConfig;
import com.kdgz.uwifi.auth.model.AcInfo;
import com.kdgz.uwifi.auth.model.BwList;

/**
 * AC脚本下放控制器
 * 
 * @author lanbo
 * 
 */
public class ACScriptController extends BaseController {

	/**
	 * 获取AC载入脚本
	 */
	public void getLoaderScript() {

		// AC Id
		String acId = getPara("acid", "");
		// AC系统
		String acos = getPara("acos", "");

		String script = getACScriptStr("uwifi_loader.sh");

		String host = getRequest().getServerName() + ":"
				+ getRequest().getServerPort();
		String path = getRequest().getContextPath().split("/")[1];

		script = script.replace("$hostname", host);
		script = script.replace("$path", path);
		script = script.replace("$acid", acId);
		script = script.replace("$acos", acos);

		renderText(script);
	}

	/**
	 * 获取AC配置脚本
	 */
	public void getConfigScript() {

		// AC Id
		String acId = getPara("acid");
		// AC系统
		String acos = getPara("acos");

		String script = "";
		// DDWRT
		if (Constants.AC_TYPE_DDWRT.equals(acos)) {
			script = getACScriptStr("wifiap_configure.sh");
			// OPENWRT
		} else if (Constants.AC_TYPE_OPENWRT.equals(acos)) {

			AcConfig acConfig = AcConfig.dao.selectAcConfigInfo(acId);

			script = getACScriptStr("uwifi_config_load.sh");
			script = script.replace("$leasetime", acConfig == null ? "1440"
					: String.valueOf(acConfig.get("dhcplease")));
		} else {
			renderNull();
			return;
		}
		String host = getRequest().getServerName() + ":"
				+ getRequest().getServerPort();
		String path = getRequest().getContextPath().split("/")[1];
		script = script.replace("$hostname", host);
		script = script.replace("$path", path);
		script = script.replace("$acid", acId);
		script = script.replace("$acos", acos);

		renderText(script);
	}

	/**
	 * 获取AC wifidog配置脚本
	 */
	public void getWifidogConfigScript() {

		// AC Id
		String acId = getPara("acid");
		// AC系统
		String acos = getPara("acos");

		AcConfig acConfig = AcConfig.dao.selectAcConfigInfo(acId);
		boolean defFlag = false;
		if (acConfig == null) {
			defFlag = true;
		}
		StringBuffer sb = new StringBuffer();
		String script = "";
		// DDWRT
		if (Constants.AC_TYPE_DDWRT.equals(acos)) {

			sb.append("wd_httpdcon=")
					.append(defFlag ? "30" : acConfig.get("maxuser"))
					.append("\n");
			sb.append("dhcp_num=")
					.append(defFlag ? "30" : acConfig.get("maxuser"))
					.append("\n");
			sb.append("dhcp_lease=")
					.append(defFlag ? "1440" : acConfig.get("dhcplease"))
					.append("\n");
			sb.append("wd_timeout=")
					.append(defFlag ? "5" : acConfig.get("clienttimeout"))
					.append("\n");
			script = sb.toString();
			// OPENWRT
		} else if (Constants.AC_TYPE_OPENWRT.equals(acos)) {

			if (defFlag) {
				script = getACScriptStr("wifidog.conf.default");
				
				script = script.replace("$gatewayid", acId);
				
				// Mac白名单
				AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
				List<BwList> trustMacList = BwList.dao.selectBwList(
						acInfo.getInt("businessid"), Constants.WHTYPE_WHITE,
						Constants.CTLTYPE_MAC);

				StringBuilder sb2 = new StringBuilder();
				if (trustMacList.size() > 0) {
					for (BwList bwList : trustMacList) {
						sb2.append(bwList.getStr("controlvalue")).append(",");
					}
				}
				script = script.replace("$trustedMACList", sb2.toString());
			} else {
				script = getACScriptStr("wifidog.conf");
				script = script.replace("$gatewayid",
						acConfig.getStr("gatewayid"));
				// 暂时不用
				// script = script.replace("$externalinterface",
				// acConfig.getStr("externalinterface"));
				// script = script.replace("$internalinterface",
				// acConfig.getStr("internalinterface"));
				script = script.replace("$gatewayaddress",
						acConfig.getStr("gatewayaddress"));
				script = script.replace("$authserverhostname",
						acConfig.getStr("authserverhostname"));
				script = script.replace("$authserverport",
						String.valueOf(acConfig.get("authserverport")));
				script = script.replace("$authserverpath",
						acConfig.getStr("authserverpath"));
				script = script.replace("$gatewayport",
						String.valueOf(acConfig.get("gatewayport")));
				script = script.replace("$checkinterval",
						String.valueOf(acConfig.get("checkinterval")));
				script = script.replace("$clienttimeout",
						String.valueOf(acConfig.get("clienttimeout")));
				script = script.replace("$maxuser",
						String.valueOf(acConfig.get("maxuser")));

				// Mac白名单
				AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
				List<BwList> trustMacList = BwList.dao.selectBwList(
						acInfo.getInt("businessid"), Constants.WHTYPE_WHITE,
						Constants.CTLTYPE_MAC);

				StringBuilder sb2 = new StringBuilder();
				if (trustMacList.size() > 0) {
					for (BwList bwList : trustMacList) {
						sb2.append(bwList.getStr("controlvalue")).append(",");
					}
				}
				script = script.replace("$trustedMACList", sb2.toString());

			}

		} else {
			renderNull();
			return;
		}

		renderText(script);
	}

	/**
	 * 获取白名单脚本
	 */
	public void getWhiteListScript() {

		// AC Id
		String acId = getPara("acid");
		// AC系统
		String acos = getPara("acos");

		String script = "";
		// DDWRT
		if (Constants.AC_TYPE_DDWRT.equals(acos)) {
			script = getACScriptStr("whitelist_configure.sh");
			// OPENWRT
		} else if (Constants.AC_TYPE_OPENWRT.equals(acos)) {
			script = getACScriptStr("uwifi_whitelist_load.sh");
		} else {
			renderNull();
			return;
		}
		String host = getRequest().getServerName() + ":"
				+ getRequest().getServerPort();
		String path = getRequest().getContextPath().split("/")[1];
		script = script.replace("$hostname", host);
		script = script.replace("$path", path);
		script = script.replace("$acid", acId);
		renderText(script);
	}

	/**
	 * 获取Qos脚本
	 */
	public void getQosScript() {
		// AC Id
		String acId = getPara("acid");
		// AC系统
		String acos = getPara("acos");

		String script = "";
		// DDWRT
		if (Constants.AC_TYPE_DDWRT.equals(acos)) {
			script = getACScriptStr("uwifi_qos_load.sh");
			// OPENWRT
		} else if (Constants.AC_TYPE_OPENWRT.equals(acos)) {
			script = getACScriptStr("uwifi_qos_load.sh");
		} else {
			renderNull();
			return;
		}
		AcConfig acConfig = AcConfig.dao.selectAcConfigInfo(acId);
		if (acConfig == null) {
			renderNull();
		} else if (acConfig.getInt("totalupload") != 0
				&& acConfig.getInt("totaldownload") != 0
				&& acConfig.getInt("clientspeed") != 0) {
			// 上行总带宽（kbt）
			script = script.replace("$tothoutalUpload",
					String.valueOf(acConfig.get("totalupload")));
			// 下行总带宽（kbt）
			script = script.replace("$totalDownload",
					String.valueOf(acConfig.get("totaldownload")));
			// 每个终端可以使用的带宽（kbt）
			script = script.replace("$clientSpeed",
					String.valueOf(acConfig.get("clientspeed")));
			renderText(script);
			return;
		}
		renderNull();
	}

	/**
	 * 获取AC域名/IP白名单
	 */
	public void getACDomainRules() {

		// AC Id
		String acId = getPara("acid", null);

		// 黑白名单类型
		String type = getPara("type", null);

		if (acId == null || type == null) {
			renderNull();
			return;
		}

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			renderNull();
			return;
		}

		int busId = acInfo.getInt("businessid");

		List<BwList> domainBwList = BwList.dao.selectBwList(busId,
				Integer.parseInt(type), Constants.CTLTYPE_DOMAIN);

		List<BwList> iPBwList = BwList.dao.selectBwList(busId,
				Integer.parseInt(type), Constants.CTLTYPE_IP);

		StringBuilder sb = new StringBuilder();

		for (BwList bwList : domainBwList) {
			sb.append(bwList.getStr("controlvalue")).append("\n");
		}

		for (BwList bwList : iPBwList) {
			sb.append(bwList.getStr("controlvalue")).append("\n");
		}
		// 文件服务器HTTP访问IP默认加到白名单里面
		sb.append(getProperty().getFileProxyIp()).append("\n");
		// 添加微信白名单
		String weixin = getACScriptStr("weixin.txt");
		sb.append(weixin);
		renderText(sb.toString());

	}

	/**
	 * 获取AC设备白名单
	 */
	public void getACMacRules() {

		// AC Id
		String acId = getPara("acid", null);

		// 黑白名单类型
		String type = getPara("type", null);

		if (acId == null || type == null) {
			renderNull();
			return;
		}
		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			renderNull();
		}

		List<BwList> macBwList = BwList.dao.selectBwList(
				acInfo.getInt("businessid"), Integer.parseInt(type),
				Constants.CTLTYPE_MAC);

		StringBuilder sb = new StringBuilder();

		for (BwList bwList : macBwList) {
			sb.append(bwList.getStr("controlvalue")).append("\n");
		}

		renderText(sb.toString());
	}

	/**
	 * 获取sh脚本
	 */
	private String getACScriptStr(String scriptName) {

		BufferedReader bufferReader = null;
		StringBuffer sb = new StringBuffer();
		String str = "";
		try {
			File fileSh = new File(PathKit.getWebRootPath() + "/ap_script/"
					+ scriptName);
			FileReader reader = new FileReader(fileSh);
			bufferReader = new BufferedReader(reader);
			while ((str = bufferReader.readLine()) != null) {
				sb.append(str + "\n");
			}

		} catch (Exception e) {
			// nothing
		} finally {
			try {
				bufferReader.close();
			} catch (IOException e) {
				// nothing
			}
		}
		return sb.toString();
	}

}
