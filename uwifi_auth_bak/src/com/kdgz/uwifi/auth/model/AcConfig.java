package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AcConfig extends Model<AcConfig> {

	/**
	 * 
	 */
	public static final AcConfig dao = new AcConfig();

	/**
	 * 获取AC配置信息
	 * 
	 * @param businessid
	 * @return
	 */
	public AcConfig selectAcConfigInfo(final String acid) {

		AcConfig acConfig = CacheKit.get(Constants.DATACACHE,
				"selectAcConfigInfo_" + acid, new IDataLoader() {
					public Object load() {
						return AcConfig.dao.findFirst(
								"select * from acconfig where acid = ?",
								new Object[] { acid });
					}
				});
		return acConfig;
	}
}
