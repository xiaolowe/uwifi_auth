package com.kdgz.uwifi.auth.controller;

import org.apache.commons.lang3.StringUtils;

import com.kdgz.uwifi.auth.constant.Constants;
import com.kdgz.uwifi.auth.model.AcAuth;
import com.kdgz.uwifi.auth.model.BusinessInfo;

/**
 * 微信 微官网接口
 * 
 * @author Lanbo
 *
 */
public class PortalController extends BaseController {

	public void index() {
		
		String busId = getPara("busId");
		
		if(StringUtils.isEmpty(busId)) {
			
			render("/auth/online.html");
			
		} else {
			
			// 获取商家信息
			BusinessInfo busInfo = null;
			int businessid = 0;
			try {
				businessid = Integer.parseInt(busId);
				
				busInfo = BusinessInfo.dao.selectBusinessInfo(businessid);
			} catch (Exception e) {
				// nothing
			}
			
			if(busInfo != null) {
				
				// 跳转到Portal
				String redirectUrl = "";

				AcAuth acAuth = AcAuth.dao.selectAcAuth(businessid);
				if (acAuth == null) {
					redirectUrl = getProperty().getDefaultPortalUrl();
				} else {
					if (Constants.AFTERAUTH_PORTAL == acAuth.getInt("afterauth")) {
						redirectUrl = getProperty().getDefaultPortalUrl() + "?businessid=" + businessid;
					} else if (Constants.AUTHTYPE_WEBSITE == acAuth.getInt("afterauth")) {
						redirectUrl = acAuth.getStr("portalurl");
					}
				}
				redirect(redirectUrl);
				return;
			} else {
				render("/auth/online.html");
			}
		}
	}
}
