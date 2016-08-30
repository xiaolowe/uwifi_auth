package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class Multiauthconfig extends Model<Multiauthconfig> {

	/**
	 * 
	 */
	public static final Multiauthconfig dao = new Multiauthconfig();

	/**
	 * 通过员工密码获取多种认证信息
	 * 
	 * @param employeepwd
	 * @return
	 */
	public Multiauthconfig selectMultiauthByPwd(final int businessid,
			final String employeepwd) {
		Multiauthconfig multiauthconfig = CacheKit.get(Constants.DATACACHE,
				"selectMultiauthByPwd_" + businessid, new IDataLoader() {
					public Object load() {

						StringBuffer sql = new StringBuffer();
						sql.append("select * from multiauthconfig where 1=1 ");
						if (businessid != 0) {
							sql.append(" and businessid = '" + businessid
									+ "' ");
						}
						sql.append(" and employeepwd = '" + employeepwd + "' ");
						Multiauthconfig multiauthconfig = dao.findFirst(sql
								.toString());
						return multiauthconfig;

					}
				});
		return multiauthconfig;

	}

	/**
	 * 通过acid获取多种认证信息
	 * 
	 * @param businessId
	 * @return
	 */
	public Multiauthconfig selectMultiauthByBusId(final int businessId) {

		Multiauthconfig multiauthconfig = CacheKit.get(Constants.DATACACHE,
				"selectMultiauthByBusId_" + businessId, new IDataLoader() {
					public Object load() {

						return dao
								.findFirst("select * from multiauthconfig where businessid = '"
										+ businessId + "' ");
					}
				});

		return multiauthconfig;
	}

}
