package com.kdgz.uwifi.auth.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class AuthSmsTemple extends Model<AuthSmsTemple> {

	/**
	 * 
	 */
	public static final AuthSmsTemple dao = new AuthSmsTemple();

	/**
	 * 获取短信模板信息
	 * 
	 * @param acid
	 * @return
	 */
	public String selectSmsContent(final String businessid) {

		String templecontent = CacheKit.get(Constants.DATACACHE, "smsContent_"
				+ businessid, new IDataLoader() {
			public Object load() {
				Record record = Db
						.findFirst(
								"SELECT tpl.templecontent FROM authsmstemple tpl LEFT JOIN smsacconfig sms ON tpl.id = sms.smsid WHERE tpl.delflag = 0 AND sms.businessid = ?",
								new Object[] { businessid });

				if (record != null) {
					return record.getStr("templecontent");
				}
				return "";
			}
		});

		return templecontent;
	}

}
