package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AcStateTemp extends Model<AcStateTemp> {

	/**
	 * 
	 */
	public static final AcStateTemp dao = new AcStateTemp();

	/**
	 * 获取AC状态
	 * 
	 * @param acid
	 * @return
	 */
	public AcStateTemp selectAcStateTemp(final String acid) {

		AcStateTemp temp = CacheKit.get(Constants.DATACACHE,
				"selectAcStateTemp_" + acid, new IDataLoader() {
					public Object load() {
						AcStateTemp temp = AcStateTemp.dao.findFirst(
								"select * from acstatetemp where acid = ?",
								new Object[] { acid });
						return temp;
					}
				});

		return temp;
	}

}
