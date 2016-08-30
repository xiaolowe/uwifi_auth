package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class Apply extends Model<Apply> {

	/**
	 * 
	 */
	public static final Apply dao = new Apply();

	/**
	 * 查询应用列表里所有的应用
	 * 
	 * @return
	 */
	public List<Apply> selectAppList() {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT app.id,app.appname,app.appversion,app.imgurl,app.fileurl,app.applicationname, app.appsize ");
		sb.append("FROM ");
		sb.append("apply as app where app.delflag = 0");

		List<Apply> list = Apply.dao
				.findByCache(Constants.DATACACHE, "selectAppList" ,
						sb.toString());

		return list;
	}

}
