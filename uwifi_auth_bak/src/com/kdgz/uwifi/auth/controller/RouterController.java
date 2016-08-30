package com.kdgz.uwifi.auth.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.jfinal.kit.PathKit;
import com.kdgz.uwifi.auth.bean.AppDownLoadLog;
import com.kdgz.uwifi.auth.bean.RouterLog;
import com.kdgz.uwifi.auth.constant.Constants;
import com.kdgz.uwifi.auth.model.AcInfo;
import com.kdgz.uwifi.auth.model.Apply;
import com.kdgz.uwifi.auth.model.BwList;
import com.kdgz.uwifi.auth.model.Firmware;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

public class RouterController extends BaseController {
	
	/**
	 * 路由器获取固件更新信息接口
	 */
	public void sysupgrade() {

		// 路由器固件当前版本
		String binVer = getPara("bin_ver");
		// 路由器Mac地址
		String macAddr = getPara("mac_addr");
		// 路由器类型
		String routerType = getPara("router_type");

		if (StringUtils.isEmpty(binVer) || StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}
		String[] vers = StringUtils.split(binVer, '-');

		if (vers.length <= 1) {
			renderNull();
			return;
		}

		// 路由器类型没有的话 从路由器固件当前版本中获取
		if (StringUtils.isEmpty(routerType)) {
			routerType = vers[1];
		}
		// 固件当前版本号
		String osVersion = vers[2];

		Firmware firmware = Firmware.dao.selectFirmware(routerType);

		StringBuilder sb = new StringBuilder();

		if (firmware != null) {
			String version = firmware.getStr("version");
			
			if (version.contains("v")) {
				version = version.replace("v", "");
			}

			if (osVersion.contains("v")) {
				osVersion = osVersion.replace("v", "");
			}

			if (!osVersion.equals(version)) {
				String url = firmware.getStr("url");
				String encrypt = firmware.getStr("encrypt").toLowerCase();
				sb.append(Constants.OPERATION_SUCCEED).append(Constants.SEMICOLON);
				sb.append("tianyu-" + routerType + "-v" + version).append(Constants.SEMICOLON);
				String fileProxy = getProperty().getFileProxyDomain();
				sb.append(fileProxy + url).append(Constants.SEMICOLON);
				sb.append(encrypt);

				RouterLog log = new RouterLog();
				long time = System.currentTimeMillis();
				log.setCode(Constants.OPERATION_SUCCEED);
				log.setMac(macAddr);
				log.setVersion(version);
				log.setActionTime(DateFormatUtils.format(time,
						Constants.DATETIME_FORMAT));
				log.setActionTimeLong(time);
				this.writeRouterLog(log);

				renderText(sb.toString());
				return;
			}
		}

		renderText("0;;;");
	}
	
	/**
	 * 路由器获取固件更新信息接口
	 */
	public void getSystemUpgradeInfo() {

		// 路由器固件当前版本
		String binVer = getPara("bin_ver");
		// 路由器Mac地址
		String macAddr = getPara("mac_addr");
		// 路由器类型
		String routerType = getPara("router_type");

		if (StringUtils.isEmpty(binVer) || StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}
		String[] vers = StringUtils.split(binVer, '-');

		if (vers.length <= 1) {
			renderNull();
			return;
		}

		// 路由器类型没有的话 从路由器固件当前版本中获取
		if (StringUtils.isEmpty(routerType)) {
			routerType = vers[1];
		}
		// 固件当前版本号
		String osVersion = vers[2];

		Firmware firmware = Firmware.dao.selectFirmware(routerType);

		StringBuilder sb = new StringBuilder();

		if (firmware != null) {
			String version = firmware.getStr("version");
			
			if (version.contains("v")) {
				version = version.replace("v", "");
			}

			if (osVersion.contains("v")) {
				osVersion = osVersion.replace("v", "");
			}

			if (!osVersion.equals(version)) {
				String url = firmware.getStr("url");
				String encrypt = firmware.getStr("encrypt").toLowerCase();
				sb.append(Constants.OPERATION_SUCCEED).append(Constants.COMMA);
				sb.append(version).append(Constants.COMMA);
				String fileProxy = getProperty().getFileProxyDomain();
				sb.append(fileProxy + url).append(Constants.COMMA);
				sb.append(encrypt);

				RouterLog log = new RouterLog();
				long time = System.currentTimeMillis();
				log.setCode(Constants.OPERATION_SUCCEED);
				log.setMac(macAddr);
				log.setVersion(version);
				log.setActionTime(DateFormatUtils.format(time,
						Constants.DATETIME_FORMAT));
				log.setActionTimeLong(time);
				this.writeRouterLog(log);

				renderText(sb.toString());
				return;
			}
		}

		renderText("0,,,");
	}

	/**
	 * 路由器同步服务端 门店所绑定的应用
	 */
	public void apkSync() {

		// 路由器已存储APK
		String apkList = getPara("apk_list");

		// 路由器Mac地址
		String macAddr = getPara("mac_addr");

		if (StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}

		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			renderNull();
			return;
		}

		List<Apply> appList = Apply.dao.selectAppList();

		if (appList.size() == 0) {
			renderNull();
			return;
		}

		// 没有接受到路由器存储的apk列表
		if (StringUtils.isEmpty(apkList)) {
			StringBuilder rst = new StringBuilder();
			for (Apply apply : appList) {
				rst.append(apply.get("id"));
				rst.append(Constants.COMMA);
				rst.append(Constants.OPERATION_SUCCEED);
				rst.append(Constants.COMMA);
				String fileProxy = getProperty().getFileProxyDomain();
				rst.append(fileProxy).append(apply.getStr("fileurl"));
				rst.append(Constants.COMMA);
				rst.append(apply.get("appversion"));
				rst.append(Constants.COMMA);
				rst.append(apply.get("applicationname"));
				rst.append("\n");
			}
			renderText(rst.toString());
			return;
		}

		String[] apkArr = apkList.split(Constants.SEMICOLON);
		Map<String, String> apkMap = new HashMap<String, String>();

		for (int i = 0; i < apkArr.length; i++) {
			String[] tmp = apkArr[i].split(Constants.COMMA);
			if (tmp.length >= 2) {
				apkMap.put(tmp[0], tmp[1]);
			}
		}

		StringBuilder rst = new StringBuilder();
		for (Apply apply : appList) {

			rst.append(apply.get("id"));
			rst.append(Constants.COMMA);
			if (apkMap.containsKey(apply.get("id").toString())
					&& (apkMap.get(apply.get("id").toString()).equals(
						apply.getStr("appversion")))) {
					rst.append(Constants.OPERATION_FAILED);
			} else {
				rst.append(Constants.OPERATION_SUCCEED);
			}
			rst.append(Constants.COMMA);
			String fileProxy = getProperty().getFileProxyDomain();
			rst.append(fileProxy).append(apply.getStr("fileurl"));
			rst.append(Constants.COMMA);
			rst.append(apply.get("appversion"));
			rst.append(Constants.COMMA);
			rst.append(apply.get("applicationname"));
			rst.append("\n");
		}

		renderText(rst.toString());
	}

	/**
	 * 手机在连接上路由器后，下载路由器SD中应用后将下载记录上报服务器
	 */
	public void apkDownloadHistSync() {

		// 路由器已存储APK
		String apkId = getPara("apkId");

		String version = getPara("version");

//		String time = getPara("time");
		
		String ua = getPara("ua");

		// 路由器Mac地址
		String macAddr = getPara("mac_addr");

		if (StringUtils.isEmpty(macAddr) || StringUtils.isEmpty(macAddr)) {
			return;
		}
		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			return;
		}

		AppDownLoadLog appLog = new AppDownLoadLog();
		long timeStamp = System.currentTimeMillis();
		appLog.setActionTime(DateFormatUtils.format(timeStamp,
				Constants.DATETIME_FORMAT));
		appLog.setActionTimeLong(timeStamp);
		appLog.setAcId(acId);
		appLog.setBusinessId(acInfo.getInt("businessid"));
		// 手机助手AppId默认为0
		appLog.setAppId(Integer.parseInt(apkId));
		appLog.setVersion(version);
		appLog.setUa(ua);
		// 写入手机助手下载日志
		this.writeAppDownloadLog(appLog);
	}

	/**
	 * 手机在连接上路由器后，下载路由器SD中应用后将下载记录上报服务器(批量上报接口)
	 */
//	public void apkDownloadHistBatchSync() {
//
//		// 路由器已存储APK
//		String histList = getPara("hist_list");
//
//		// 路由器Mac地址
//		String macAddr = getPara("mac_addr");
//
//		if (StringUtils.isEmpty(histList) || StringUtils.isEmpty(macAddr)) {
//			return;
//		}
//		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();
//
//		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
//
//		if (acInfo == null) {
//			return;
//		}
//
//		AppDownLoadLog appLog = new AppDownLoadLog();
//		long time = System.currentTimeMillis();
////		if (StringUtils.isNotEmpty(time)) {
////			long timeStamp = Long.valueOf(time);
//			appLog.setActionTime(DateFormatUtils.format(time,
//					Constants.DATETIME_FORMAT));
//			appLog.setActionTimeLong(time);
////		}
//		appLog.setAcId(acId);
//		appLog.setBusinessId(acInfo.getInt("businessid"));
//		// 手机助手AppId默认为0
////		appLog.setAppId(Integer.parseInt(apkId));
////		appLog.setVersion(version);
//		// 写入手机助手下载日志
//		this.writeAppDownloadLog(appLog);
//	}

	/**
	 * 路由器同步服务端 门店应用套餐页面
	 */
	public void getShopApkHtml() {

		// 路由器Mac地址
		String macAddr = getPara("mac_addr");

		if (StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}

		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			renderNull();
			return;
		}

		List<Apply> appList = Apply.dao.selectAppList();

		// 将fileid中文件名称剪切出来
		for (Apply apply : appList) {
			String fileId = apply.getStr("fileurl");
			
			String fileName = StringUtils.substringAfterLast(fileId, "/");
			
			apply.set("fileurl", fileName);
		}
		
		Map<String, Object> root = new HashMap<String, Object>();

		root.put("appList", appList);
		String fileProxy = getProperty().getFileProxyDomain();
		root.put("fileProxy", fileProxy);

		Configuration config = new Configuration();

		try {
			config.setDirectoryForTemplateLoading(new File(PathKit
					.getWebRootPath() + "/apk"));
		} catch (IOException e) {
			renderNull();
			return;
		}
		config.setTemplateUpdateDelay(0);
		config.setTemplateUpdateDelay(5);

		config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);

		config.setDefaultEncoding("UTF-8");
		config.setOutputEncoding("UTF-8");
		config.setLocale(Locale.CHINA /* Locale.CHINA */); // config.setLocale(Locale.US);
		config.setLocalizedLookup(false);

		// 去掉int型输出时的逗号, 例如: 123,456
		// config.setNumberFormat("#"); // config.setNumberFormat("0"); 也可以
		config.setNumberFormat("#0.#####");
		config.setDateFormat("yyyy-MM-dd");
		config.setTimeFormat("HH:mm:ss");
		config.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		Template temp = null;
		try {
			temp = config.getTemplate("index.html");
		} catch (IOException e) {
			e.printStackTrace();
			renderNull();
			return;
		}

		StringWriter sw = new StringWriter();

		try {
			temp.process(root, sw);
		} catch (Exception e) {
			renderNull();
			return;
		}

		renderText(sw.toString());
	}
	
	
	/**
	 * 
	 */
	public void getEqptssid() {

		// 路由器Mac地址
		String macAddr = getPara("mac_addr");

		if (StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}
		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);

		if (acInfo == null) {
			renderNull();
			return;
		}
		StringBuilder rst = new StringBuilder();
		rst.append(acInfo.get("eqptssid"));

		renderText(rst.toString());
	}
	
	
	/**
	 * 获取AC设备白名单
	 */
	public void getACMacRules() {

		// 路由器Mac地址
		String macAddr = getPara("mac_addr");

		if (StringUtils.isEmpty(macAddr)) {
			renderNull();
			return;
		}
		String acId = macAddr.replace(Constants.COLON, "").toUpperCase();

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
	
}
