package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class TempletInfo extends Model<TempletInfo> {

	/**
	 * 
	 */
	public static final TempletInfo dao = new TempletInfo();

	/**
	 * 获取模板信息
	 * 
	 * @param id
	 * @param tempType
	 * @return
	 */
	public TempletInfo selectBusTemplate(final int id, final int tempType) {

		TempletInfo templetInfo = CacheKit.get(Constants.DATACACHE,
				"selectTempletInfo_" + id, new IDataLoader() {
					public Object load() {
						return TempletInfo.dao
								.findFirst(
										"select * from templetinfo where id = ? and tempType = ? and delflag = 0",
										new Object[] { id, tempType });
					}
				});

		return templetInfo;
	}
}
