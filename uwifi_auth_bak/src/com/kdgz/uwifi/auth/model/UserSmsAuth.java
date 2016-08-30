package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class UserSmsAuth extends Model<UserSmsAuth> {

	/**
	 * 
	 */
	public static final UserSmsAuth dao = new UserSmsAuth();

	/**
	 * 获取短信认证 信息
	 * 
	 * @param acid
	 * @return
	 */
	public UserSmsAuth selectUserSmsAuth(final String businessid,
			final String phoneno, final String captcha) {

		UserSmsAuth entity = CacheKit.get(Constants.DATACACHE,
				"selectMultiauthByBusId_" + businessid, new IDataLoader() {
					public Object load() {
						UserSmsAuth entity = UserSmsAuth.dao
								.findFirst(
										"select * from usersmsauth where businessid  = ? and phoneno = ? and captcha = ? order by id desc",
										new Object[] { businessid, phoneno,
												captcha });
						return entity;
					}
				});

		return entity;
	}

}
