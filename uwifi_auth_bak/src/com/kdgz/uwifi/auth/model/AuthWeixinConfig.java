package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AuthWeixinConfig extends Model<AuthWeixinConfig> {

	/**
	 * 
	 */
	public static final AuthWeixinConfig dao = new AuthWeixinConfig();

	/**
	 * 获取微信认证配置信息
	 * 
	 * @param acid
	 * @return
	 */
	public AuthWeixinConfig selectAuthWeixinConfig(final int businessid) {

		AuthWeixinConfig entity = CacheKit.get(Constants.DATACACHE,
				"selectAuthWeixinConfig_" + businessid, new IDataLoader() {
					public Object load() {

						AuthWeixinConfig entity = AuthWeixinConfig.dao
								.findFirst(
										"select * from authweixinconfig where businessid = ? and delflag = 0",
										new Object[] { businessid });
						return entity;
					}
				});

		return entity;
	}

}
