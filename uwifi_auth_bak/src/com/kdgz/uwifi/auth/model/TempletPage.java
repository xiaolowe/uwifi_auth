package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class TempletPage extends Model<TempletPage> {

	/**
	 * 
	 */
	public static final TempletPage dao = new TempletPage();

	public List<TempletPage> selectTempletPageList(int busId, int temptype) {

		List<TempletPage> pagesInfo = TempletPage.dao
				.findByCache(
						Constants.DATACACHE,
						"selectTempletPageList_" + busId,
						"select * from templetpage where businessid = ? and temptype = ? and delflag = 0 order by sort",
						new Object[] { busId, temptype });

		return pagesInfo;
	}
}
