package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AcAuth extends Model<AcAuth> {

	/**
	 * 
	 */
	public static final AcAuth dao = new AcAuth();

	/**
	 * 获取AC认证方式信息
	 * 
	 * @param acid
	 * @return
	 */
	public List<AcAuth> selectAcAuthList(String acid) {

		List<AcAuth> list = AcAuth.dao.find(
				"select * from acauth where acid = ?", new Object[] { acid });
		return list;
	}

	/**
	 * 获取AC配置信息
	 * 
	 * @param acid
	 * @return
	 */
	public AcAuth selectAcAuth(final int businessid) {

		AcAuth acInfo = CacheKit.get(Constants.DATACACHE, "selectAcAuth_"
				+ businessid, new IDataLoader() {
			public Object load() {
				return AcAuth.dao.findFirst(
						"select * from acauth where businessid = ?",
						new Object[] { businessid });
			}
		});
		return acInfo;
	}

}
