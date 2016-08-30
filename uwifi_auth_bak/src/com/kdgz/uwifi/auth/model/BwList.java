package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class BwList extends Model<BwList> {

	/**
	 * 
	 */
	public static final BwList dao = new BwList();

	/**
	 * 获取黑白名单信息
	 * 
	 * @param acid
	 * @return
	 */
	public List<BwList> selectBwList(int businessid, int whtype, int ctltype) {

		List<BwList> list = BwList.dao
				.findByCache(
						Constants.DATACACHE,
						"selectBwList_" + businessid,
						"select * from bwlist where businessid = ? and whtype = ? and controltype = ?",
						new Object[] { businessid, whtype, ctltype });
		return list;
	}

}
