package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class BusinessTemplet extends Model<BusinessTemplet> {

	/**
	 * 
	 */
	public static final BusinessTemplet dao = new BusinessTemplet();

	/**
	 * 获取认证页模块
	 * 
	 * @param businessid
	 * @return
	 */
	public BusinessTemplet selectBusTemplate(final int businessid) {

		BusinessTemplet busTemplet = CacheKit.get(Constants.DATACACHE,
				"selectBusTemplate_" + businessid, new IDataLoader() {
					public Object load() {

						return BusinessTemplet.dao
								.findFirst(
										"select * from businesstemplet where businessid = ?",
										new Object[] { businessid });
					}
				});
		return busTemplet;
	}

}
