package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings("serial")
public class SmsAcConfig extends Model<SmsAcConfig> {

	/**
	 * 
	 */
	public static final SmsAcConfig dao = new SmsAcConfig();

	/**
	 * 获取AC短信模板配置信息
	 * 
	 * @param acid
	 * @return
	 */
	public SmsAcConfig selectSmsAcConfig(String acId) {

		SmsAcConfig acInfo = SmsAcConfig.dao.findFirst(
				"select * from smsacconfig where acid = ?",
				new Object[] { acId });
		return acInfo;
	}

}