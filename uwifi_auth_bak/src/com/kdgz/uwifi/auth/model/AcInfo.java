package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AcInfo extends Model<AcInfo> {

	/**
	 * 
	 */
	public static final AcInfo dao = new AcInfo();

	/**
	 * 获取AC信息
	 * 
	 * @param acid
	 * @return
	 */
	public AcInfo selectAcInfo(final String acid) {

		AcInfo acInfo = CacheKit.get(Constants.DATACACHE, "selectAcInfo_"
				+ acid, new IDataLoader() {
			public Object load() {

				return AcInfo.dao.findFirst(
						"select * from acinfo where acid = ?",
						new Object[] { acid });
			}
		});
		return acInfo;
	}

	/**
	 * 检索AC信息列表
	 * 
	 * @param businessid
	 * @return
	 */
	public List<AcInfo> selectAcInfoList(Integer businessid) {

		List<AcInfo> list = AcInfo.dao.findByCache(Constants.DATACACHE,
				"selectAcInfoList_" + businessid,
				"select * from acinfo where businessid = ?",
				new Object[] { businessid });

		return list;
	}

}
