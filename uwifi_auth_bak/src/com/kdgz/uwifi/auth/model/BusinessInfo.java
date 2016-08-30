package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class BusinessInfo extends Model<BusinessInfo> {

	/**
	 * 
	 */
	public static final BusinessInfo dao = new BusinessInfo();

	/**
	 * 获取商家信息
	 * 
	 * @param acid
	 * @return
	 */
	public BusinessInfo selectBusinessInfo(final int busId) {

		BusinessInfo bus = CacheKit.get(Constants.DATACACHE,
				"selectBusinessInfo_" + busId, new IDataLoader() {
					public Object load() {
						return BusinessInfo.dao.findFirst(
								"select * from businessinfo where id = ?",
								new Object[] { busId });
					}
				});

		return bus;
	}

}
