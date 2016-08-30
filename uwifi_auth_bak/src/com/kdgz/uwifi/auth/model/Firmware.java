package com.kdgz.uwifi.auth.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.kdgz.uwifi.auth.constant.Constants;

@SuppressWarnings("serial")
public class Firmware extends Model<Firmware> {

	/**
	 * 
	 */
	public static final Firmware dao = new Firmware();
	
	
	/**
	 * 获取需要升级的版本
	 * 当状态为 1 的时候为需要升级的固件
	 * @return
	 */
	public Firmware selectFirmware(String osVersion){
		
		List<Firmware> firmwareList = dao.findByCache(Constants.DATACACHE, "selectFirmware", 
				"select * from firmware where  type = ?", new Object[] {osVersion});
		
		if(firmwareList.size() > 0) {
			return firmwareList.get(0);
		}
		return null;
		
	}

	

}
