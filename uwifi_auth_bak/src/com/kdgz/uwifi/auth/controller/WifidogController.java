package com.kdgz.uwifi.auth.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.kdgz.uwifi.auth.bean.AcPingLog;
import com.kdgz.uwifi.auth.bean.AdLog;
import com.kdgz.uwifi.auth.bean.ClientInfo;
import com.kdgz.uwifi.auth.bean.LoginParam;
import com.kdgz.uwifi.auth.bean.WifidogLog;
import com.kdgz.uwifi.auth.constant.ActionType;
import com.kdgz.uwifi.auth.constant.Constants;
import com.kdgz.uwifi.auth.model.AcAuth;
import com.kdgz.uwifi.auth.model.AcConfig;
import com.kdgz.uwifi.auth.model.AcInfo;
import com.kdgz.uwifi.auth.model.AcStateTemp;
import com.kdgz.uwifi.auth.model.AuthSmsTemple;
import com.kdgz.uwifi.auth.model.AuthWeixinConfig;
import com.kdgz.uwifi.auth.model.BusinessInfo;
import com.kdgz.uwifi.auth.model.BusinessTemplet;
import com.kdgz.uwifi.auth.model.Multiauthconfig;
import com.kdgz.uwifi.auth.model.TempletInfo;
import com.kdgz.uwifi.auth.model.TempletPage;
import com.kdgz.uwifi.auth.model.UserSmsAuth;
import com.kdgz.uwifi.auth.util.MemCachedUtil;
import com.kdgz.uwifi.auth.util.SmsUtil;

/**
 * wifidog控制器
 * 
 * @author lanbo
 * 
 */
public class WifidogController extends BaseController {

	/**
	 * login接口
	 */
	public void login() {

		String acId = getPara("gw_id");
		String url = getPara("url");
		String mac = getPara("mac");

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
		
		if (acInfo == null) {
			setAttr("title", "出错误了~");
			setAttr("errorMsg", "此路由器Id不存在");
			render("/error/error.html");
			logger.error("此路由器Id不存在");
			return;
		}
		int busId = acInfo.getInt("businessid");

		// 认证方式
		int authType = 0;
		AcAuth acAuth = AcAuth.dao.selectAcAuth(busId);
		if (acAuth != null) {
			authType = acAuth.getInt("authtype");
		}

		LoginParam param = new LoginParam();
		param.setGw_id(acId);
		param.setGw_address(getPara("gw_address"));
		param.setGw_port(getPara("gw_port"));
		param.setUrl(url);
		param.setMac(mac);
		param.setAuthType(authType);
		param.setBusinessId(busId);

		// 获取商家信息
		BusinessInfo busInfo = BusinessInfo.dao.selectBusinessInfo(busId);
		// 商家Id
		setAttr("busId", busId);
		setAttr("businessName", busInfo.get("busname"));
		// 认证类型
		setAttr("authtype", authType);

		AuthWeixinConfig wxConfig = null;
		if (Constants.AUTHTYPE_WEIXIN == authType
				|| Constants.AUTHTYPE_MULTI == authType) {
			wxConfig = AuthWeixinConfig.dao.selectAuthWeixinConfig(busId);

			if (wxConfig != null)
				setAttr("weixinNO", wxConfig.get("weixinname"));
			setAttr("weixin", wxConfig);
		}

		String token = UUID.randomUUID().toString();
		param.setToken(token);

		setSessionAttr("param", param);
		setSessionAttr("token", token);

		setAttr("param", param);

		// 将日志写入CSV日志文件
		WifidogLog log = new WifidogLog();
		log.setAction(ActionType.LOGIN);
		log.setActionResult(ActionType.ALLOWED);

		long time = System.currentTimeMillis();
		log.setActionTime(DateFormatUtils.format(time,
				Constants.DATETIME_FORMAT));
		log.setActionTimeLong(time);
		log.setAcId(acId);
		log.setClientMac(mac);
		log.setClientToken(token);
		log.setOriginalUrl(url);
		log.setUserAgent(getRequest().getHeader("user-agent"));
		log.setBusinessId(String.valueOf(busId));

		log.setAuthType(String.valueOf(authType));
		
		if (Constants.AUTHTYPE_WEIXIN == authType
				|| Constants.AUTHTYPE_MULTI == authType) {
			// 客户端上网信息
			Object temObj = null;
			try {
				temObj = MemCachedUtil.get("ClientInfo_" + mac);
			} catch (Exception e) {
				// nothing
			}

			AcConfig acconfig = AcConfig.dao.selectAcConfigInfo(acId);
			int clienttimeout = acconfig.getInt("clienttimeout");

			// 判断是否为iPhone手机，如果是iPhone手机则获取几分钟免费上网时间
			String ua = getRequest().getHeader("user-agent");

			// iphone客户端
			if (StringUtils.contains(ua, "CaptiveNetworkSupport")) {
				// 第一次认证的iPhone客户端
				if (temObj == null) {
					ClientInfo clientinfo = new ClientInfo();
					clientinfo.setAcid(acId);
					clientinfo.setBusinessid(busId);
					clientinfo.setMac(mac);
					clientinfo.setAuthType(authType);
					clientinfo.setIphoneFlag(true);
					try {
						MemCachedUtil.add("ClientInfo_" + mac,
								clienttimeout * 60, clientinfo);
					} catch (Exception e) {
						logger.error("MemCached add error", e);
					}

					String redirectUrl = "http://" + param.getGw_address()
							+ ":" + param.getGw_port() + "/wifidog/auth?"
							+ "token=" + param.getToken() + "&url="
							+ param.getUrl();

					// 将日志写入CSV日志文件
					this.writeWifidogLog(log);

					redirect(redirectUrl, true);
					return;
				} else {
					// iPhone客户端 第一次免费上网后 必须要认证才能继续上网
					ClientInfo clientInfo = (ClientInfo) temObj;
					long lastTime = clientInfo.getOnlineTime();

					if (lastTime < clienttimeout * 60) {
						String redirectUrl = "http://" + param.getGw_address()
								+ ":" + param.getGw_port() + "/wifidog/auth?"
								+ "token=" + param.getToken() + "&url="
								+ param.getUrl();

						// 将日志写入CSV日志文件
						this.writeWifidogLog(log);

						redirect(redirectUrl, true);
						return;
					}

				}

			} else {

				if (temObj == null) {
					ClientInfo clientinfo = new ClientInfo();
					clientinfo.setAcid(acId);
					clientinfo.setBusinessid(busId);
					clientinfo.setMac(mac);
					clientinfo.setAuthType(authType);
					try {
						MemCachedUtil.add("ClientInfo_" + mac,
								clienttimeout * 60, clientinfo);
					} catch (Exception e) {
						logger.error("MemCached add error", e);
					}
				}

			}
		}
				
		// 云认证 无缝切换 start
		
		
		
		
		// 云认证 无缝切换 end

		// 微信认证处理
		if (wxConfig != null) {
			if (StringUtils.startsWith(url.trim(), getProperty()
					.getWeixinUrl())) {

				if (authType == 3) {
					log.setMixType(1);
				}
				log.setDesc(wxConfig.getStr("weixinno"));

				String redirectUrl = "http://" + param.getGw_address() + ":"
						+ param.getGw_port() + "/wifidog/auth?" + "token="
						+ param.getToken() + "&url=" + param.getUrl();

				// 将日志写入CSV日志文件
				this.writeWifidogLog(log);

				redirect(redirectUrl, true);
				return;
			}

		}

		// 将日志写入CSV日志文件
		this.writeWifidogLog(log);

		// 认证页模板切换
		BusinessTemplet busTemplet = BusinessTemplet.dao
				.selectBusTemplate(busId);

		if (busTemplet != null) {

			int authId = busTemplet.getInt("authid");
			// 模板id
			setAttr("templetId", authId);

			TempletInfo templetInfo = TempletInfo.dao.selectBusTemplate(authId,
					Constants.TEMPTYPE_AUTH);

			if (templetInfo != null) {
				List<TempletPage> pageInfoLst = TempletPage.dao
						.selectTempletPageList(busId, Constants.TEMPTYPE_AUTH);

				setAttr("pageInfoLst", pageInfoLst);
				setSessionAttr("pageInfoLst", pageInfoLst);

				// 文件服务器地址
				setAttr("fileProxyDomain", getProperty().getFileProxyDomain());
				render(templetInfo.getStr("url"));
				return;
			}
		}
		render(getProperty().getDefaultAuthTpl());

	}

	/**
	 * 认证登录
	 */
	public void loginValidate() {

		LoginParam param = getSessionAttr("param");
		String yuangong = getPara("yuangong");
		WifidogLog log = new WifidogLog();
		if (param == null) {
			logger.error("会话(Session)过期了");
			setAttr("title", "出错误了~");
			setAttr("errorMsg", "会话(Session)过期了");
			render("/error/error.html");
			return;
		}
//		int busId = acInfo.getInt("businessid");
//		//通过mac地址设置黑名单
//		BwList bwlist = BwList.dao.selectBwListByMac(busId, Constants.WHTYPE_BLACK, param.getMac());
//		if(bwlist != null){
//			renderJavascript("您已经被限制上网,请联系当地商家!");
//			return;
//		}
		
		long time = System.currentTimeMillis();

		log.setActionTime(DateFormatUtils.format(time,
				Constants.DATETIME_FORMAT));
		log.setActionTimeLong(time);
		log.setAcId(param.getGw_id());
		log.setClientMac(param.getMac());
		log.setClientToken(param.getToken());
		log.setOriginalUrl(param.getUrl());
		log.setUserAgent(getRequest().getHeader("user-agent"));
		log.setBusinessId(String.valueOf(param.getBusinessId()));

		log.setAction(ActionType.LOGIN_VALIDATE);

		boolean agreed = false;
		if (getPara("agree") != null) {
			agreed = true;
		}

		log.setActionResult(agreed ? ActionType.ALLOWED : ActionType.DENIED);

		if (!agreed) {
			log.setDesc("用户未同意《免责声明条款》");
		}

		int authType = param.getAuthType();
		if (Constants.AUTHTYPE_SMS == authType) {

			log.setAuthType(String.valueOf(authType));
			log.setDesc(getPara("phoneNO", ""));

		}
		if (Constants.AUTHTYPE_MULTI == authType) {

			log.setAuthType(String.valueOf(authType));
			if (yuangong != null) {
				log.setMixType(3);
			} else if (getPara("phoneNO", null) != null) {
				log.setMixType(2);
				log.setDesc(getPara("phoneNO", ""));
			}
		}
		// 将日志写入CSV日志文件
		this.writeWifidogLog(log);

		if (agreed) {
			String redirectUrl = "http://" + param.getGw_address() + ":"
					+ param.getGw_port() + "/wifidog/auth?" + "token="
					+ param.getToken() + "&url=" + param.getUrl();

			redirect(redirectUrl, true);

		} else {
			// String redirect = getProperty().getDefaultAuthUrl();
			redirect("login?" + "gw_address=" + param.getGw_address()
					+ "&gw_port=" + param.getGw_port() + "&gw_id="
					+ param.getGw_id() + "&url=" + param.getUrl() + "&mac="
					+ param.getMac());
		}
	}

	/**
	 * 微信认证页面展示
	 */

	public void showWeixinPage() {

		LoginParam param = getSessionAttr("param");
		if (param != null) {
			Multiauthconfig multiauthconfig = Multiauthconfig.dao
					.selectMultiauthByBusId(param.getBusinessId());
			// AcInfo acInfo = AcInfo.dao.selectAcInfo(param.getGw_id());
			// setAttr("busId", multiauthconfig.getInt("businessid"));
			setAttr("multiauthconfig", multiauthconfig);
			render("/auth/weixin.html");
		}

	}

	/**
	 * 员工密码认证登录
	 */
	public void loginEmployee() {
		int busId = getParaToInt("busId");
		List<TempletPage> pageInfoLst = getSessionAttr("pageInfoLst");
		if (getRequest().getMethod().equals("POST")) {
			String employeepwd = getPara("employeepwd");
			Multiauthconfig multiauthconfig = Multiauthconfig.dao
					.selectMultiauthByPwd(busId, employeepwd);
			if (multiauthconfig == null) {
				renderText("-1");
			} else {
				renderText("1");
			}
		} else {
			setAttr("busId", busId);
			setAttr("pageInfoLst", pageInfoLst);
			// 文件服务器地址
			setAttr("fileProxyDomain", getProperty().getFileProxyDomain());
			render("/auth/yuangong.html");
		}

	}

	/**
	 * 发送短信认证验证码
	 */
	public void sendSmsCode() {

		String phoneNO = getPara("phoneNO");
		String busId = getPara("busId");
//		String acId = getPara("acId");

		if (!StringUtils.isEmpty(phoneNO)) {
			// 短信验证码
			String captcha = RandomStringUtils.randomNumeric(6);
			// 短信发送模板
			String smsContent = AuthSmsTemple.dao.selectSmsContent(busId);

			smsContent = StringUtils.replace(smsContent, "[验证码]", captcha);

			// 发送手机验证码短信
			int result = SmsUtil.sendSMS(phoneNO, smsContent, "", 5);

			if (result == 0) {
				UserSmsAuth sms = new UserSmsAuth();
				sms.set("businessid", busId);
				sms.set("phoneno", phoneNO);
				sms.set("captcha", captcha);
				sms.set("addtime", new Date());

				sms.save();
			}
		}
		renderNull();
	}

	/**
	 * 发送短信认证验证码
	 */
	public void checkSmsCode() {

		String phoneNO = getPara("phoneNO");
		String busId = getPara("busId");
		String captcha = getPara("captcha");

		if (StringUtils.isNotEmpty(phoneNO) && StringUtils.isNotEmpty(busId)
				&& StringUtils.isNotEmpty(captcha)) {
			UserSmsAuth smsAuth = UserSmsAuth.dao.selectUserSmsAuth(busId,
					phoneNO, captcha);

			if (smsAuth != null) {
				String realCaptcha = smsAuth.get("captcha");
				long addTime = smsAuth.getTimestamp("addtime").getTime();
				long now = System.currentTimeMillis();

				if (realCaptcha.equals(captcha) && now - addTime < 3600 * 1000) {
					renderText("1");
					return;
				}
			}

		}
		renderText("-1");
	}

	/**
	 * auth接口
	 */
	public void auth() {

		String gw_id = getPara("gw_id");
		String token = getPara("token");
		String stage = getPara("stage");// login/couters/logout
		String ip = getPara("ip");
		String mac = getPara("mac");
		String incoming = getPara("incoming");
		String outgoing = getPara("outgoing");

		long time = System.currentTimeMillis();
		
		AcInfo acInfo = AcInfo.dao.selectAcInfo(gw_id);

		int busId = acInfo.getInt("businessid");

		/**
		 * 记录日志
		 */
		WifidogLog log = new WifidogLog();
		log.setAcId(gw_id);
		log.setClientMac(mac);
		log.setClientIp(ip);
		log.setClientToken(token);
		log.setActionResult(ActionType.ALLOWED);
		log.setAction(ActionType.AUTH);
		log.setStage(stage);
		log.setActionTime(DateFormatUtils.format(time,
				Constants.DATETIME_FORMAT));
		log.setActionTimeLong(time);
		log.setUserAgent(getRequest().getHeader("user-agent"));
		log.setIncoming(incoming);
		log.setOutgoing(outgoing);
		log.setBusinessId(String.valueOf(busId));

		// 将日志写入CSV日志文件
		this.writeWifidogLog(log);
		
		Object tempObj = null;
		try {
			tempObj = MemCachedUtil.get("ClientInfo_" + mac);
		} catch (Exception e) {
			logger.error("MemCached get error", e);
		}
		
		if(tempObj == null) {
			renderText("Auth: 1");
			return;
		}
		
		ClientInfo clientInfo = (ClientInfo)tempObj;
		if(Constants.AUTHTYPE_WEIXIN != clientInfo.getAuthType()
				&& Constants.AUTHTYPE_MULTI != clientInfo.getAuthType()) {
			renderText("Auth: 1");
			return;
		}
		
		AcConfig acconfig = AcConfig.dao.selectAcConfigInfo(gw_id);
		
		// 心跳时间 秒
		int checkinterval = 60;
		// 超时时间 分钟
		int clienttimeout = 5;
		if(acconfig != null) {
			checkinterval = acconfig.getInt("checkinterval");
			clienttimeout = acconfig.getInt("clienttimeout");
		}
		
		if(stage.equals("login")) {
			// iPhone客户端
			if(clientInfo.isIphoneFlag()) {
				// 秒
				long lasttime = clientInfo.getOnlineTime() + checkinterval;
				// 免费时间超时 踢下线后 第二次认证
				if(lasttime > clienttimeout * 60) {
					clientInfo.setLastLoginTime(System.currentTimeMillis());
					clientInfo.setAuthenticated(true);
				}
			} else {
				clientInfo.setLastLoginTime(System.currentTimeMillis());
				clientInfo.setAuthenticated(true);
			}
			
			try {
				MemCachedUtil.replace("ClientInfo_" + mac, clienttimeout * 60, clientInfo);
			} catch (Exception e) {
				logger.error("MemCached replace error", e);
			}
			
		} else if(stage.equals("counters")) {
			// 秒
			long lasttime = clientInfo.getOnlineTime() + checkinterval;
			clientInfo.setOnlineTime(lasttime);
			try {
				MemCachedUtil.replace("ClientInfo_" + mac, clienttimeout * 60, clientInfo);
			} catch (Exception e) {
				logger.error("MemCached replace error", e);
			}
			
			// iPhone客户端
			if(clientInfo.isIphoneFlag() && !clientInfo.isAuthenticated()) {
				// 免费时间超时 踢下线
				if(lasttime > clienttimeout * 60) {
					
					renderText("Auth: 0");
					return;
				}
			}
			
		} else if(stage.equals("logout")) {
			
//			try {
//				MemCachedUtil.delete("ClientInfo_" + mac);
//			} catch (Exception e) {
//				logger.error("MemCached delete error", e);
//			}
		}
		
		renderText("Auth: 1");
}

	
	/**
	 * portal接口
	 */
	public void portal() {

		String acId = getPara("gw_id");

		LoginParam param = getSessionAttr("param");

		long time = System.currentTimeMillis();

		/**
		 * 记录日志
		 */
		WifidogLog log = new WifidogLog();

		AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
		int businessid = acInfo.getInt("businessid");
		if (param != null) {
			log.setClientToken(param.getToken());
			log.setClientMac(param.getMac());
			log.setBusinessId(String.valueOf(businessid));
		} else {
			log.setBusinessId(String.valueOf(businessid));
		}

		log.setAcId(acId);
		log.setActionResult(ActionType.ALLOWED);
		log.setAction(ActionType.PORTAL);
		log.setActionTime(DateFormatUtils.format(time,
				Constants.DATETIME_FORMAT));
		log.setActionTimeLong(time);
		log.setUserAgent(getRequest().getHeader("user-agent"));

		// 将日志写入CSV日志文件
		this.writeWifidogLog(log);

		// 跳转到Portal
		String redirectUrl = "";

		AcAuth acAuth = AcAuth.dao.selectAcAuth(businessid);
		if (acAuth == null) {
			redirectUrl = getProperty().getDefaultPortalUrl();
		} else {
			if (Constants.AFTERAUTH_PORTAL == acAuth.getInt("afterauth")) {
				redirectUrl = getProperty().getDefaultPortalUrl();
			} else if (Constants.AUTHTYPE_WEBSITE == acAuth.getInt("afterauth")) {
				redirectUrl = acAuth.getStr("portalurl");
			}
		}

		// 如果Session丢失
		if (param == null) {
			if (StringUtils.contains(redirectUrl, getProperty()
					.getDefaultPortalUrl())) {
				redirect(redirectUrl + "?acid=" + acId + "&businessid="
						+ businessid, true);
			} else {
				redirect(redirectUrl, true);
			}
			return;
		}
		if (StringUtils.contains(redirectUrl, getProperty()
				.getDefaultPortalUrl())) {

			redirect(redirectUrl + "?acid=" + acId + "&mac=" + param.getMac()
					+ "&businessid=" + businessid);

		} else {

			redirect(redirectUrl);

		}

	}

	/**
	 * ping接口
	 */
	public void ping() {

		String acId = getPara("gw_id");
		String sysUptime = getPara("sys_uptime");
		String sysMemfree = getPara("sys_memfree");
		String sysLoad = getPara("sys_load");
		String wifidogUptime = getPara("wifidog_uptime");

		long time = System.currentTimeMillis();
		Timestamp now = new Timestamp(time);
		AcPingLog log = new AcPingLog();

		log.setAcId(acId);
		log.setSysUptime(sysUptime);
		log.setSysMemfree(sysMemfree);
		log.setSysLoad(sysLoad);
		log.setWifidogUptime(wifidogUptime);
		log.setPingTime(DateFormatUtils.format(time, Constants.DATETIME_FORMAT));
		log.setPingTimeLong(time);

		// 将日志写入CSV日志文件
		this.writeAcPingLog(log);

		// 一期临时将AC状态更新到AC状态临时表里 FIXME
		AcStateTemp acState = AcStateTemp.dao.selectAcStateTemp(acId);

		if (acState == null) {
			AcStateTemp temp = new AcStateTemp();
			temp.set("acid", acId);
			temp.set("lastpingtime", now);
			temp.save();
		} else {

			acState.set("lastpingtime", now);
			try {
				acState.update();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		// 一期临时将AC状态更新到AC状态临时表里 FIXME

		renderText("Pong");
	}

	/**
	 * 显示免责声明
	 */
	public void showClaim() {
		render("/auth/desc.html");
	}

	/**
	 * 显示微信帮助
	 */
	public void showWxHelp() {

		// 获取商家信息
		BusinessInfo busInfo = BusinessInfo.dao
				.selectBusinessInfo(getParaToInt(0, 0));

		if (busInfo != null) {
			setAttr("businessName", busInfo.get("busname"));
			AuthWeixinConfig wxConfig = AuthWeixinConfig.dao
					.selectAuthWeixinConfig(busInfo.getInt("id"));

			if (wxConfig != null) {
				setAttr("weixinNO", wxConfig.get("weixinname"));
			} else {
				setAttr("weixinNO", "");
			}
		} else {
			setAttr("businessName", "");
		}
		render("/auth/help.html");
	}

	/**
	 * 插入wifidog日志
	 */
	public void addWifidogLog() {

		String actionType = getPara("actionType");
		String acId = getPara("acid");
		int busId = getParaToInt("busId");

		if (actionType != null && acId != null) {

			// 将日志写入CSV日志文件
			WifidogLog log = new WifidogLog();
			log.setAction(actionType);
			log.setActionResult(ActionType.ALLOWED);

			long time = System.currentTimeMillis();
			log.setActionTime(DateFormatUtils.format(time,
					Constants.DATETIME_FORMAT));
			log.setActionTimeLong(time);
			log.setAcId(acId);
			log.setClientMac(getPara("mac"));
			log.setClientToken(getPara("token"));
			log.setOriginalUrl(getPara("url"));
			log.setUserAgent(getRequest().getHeader("user-agent"));

			AcInfo acInfo = AcInfo.dao.selectAcInfo(acId);
			if (acInfo != null) {
				log.setBusinessId(String.valueOf(acInfo.getInt("businessid")));
			}

			AcAuth acAuth = AcAuth.dao.selectAcAuth(busId);
			if (acAuth != null) {
				log.setAuthType(String.valueOf(acAuth.getInt("authtype")));
			}

			// 将日志写入CSV日志文件
			this.writeWifidogLog(log);
		}
		renderNull();
	}

	/**
	 * 插入点击广告的日志
	 */
	public void addAdLog() {

		AdLog log = new AdLog();

		log.setAcId(getPara("acId"));
		log.setMac(getPara("mac"));
		log.setTempletId(getPara("templetId"));
		log.setAdId(getPara("adId"));
		log.setUrl(getPara("url"));
		log.setSort(getPara("sort"));
		log.setBusId(getPara("busId"));
		log.setAuthType(getPara("authType"));

		long time = System.currentTimeMillis();
		log.setActionTime(DateFormatUtils.format(time,
				Constants.DATETIME_FORMAT));
		log.setActionTimeLong(time);

		this.writeAdLog(log);

		renderNull();
	}
}
