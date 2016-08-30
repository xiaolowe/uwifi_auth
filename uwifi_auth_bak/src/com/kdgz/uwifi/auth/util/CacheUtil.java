package com.kdgz.uwifi.auth.util;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.constant.Constants;
import com.kdgz.uwifi.auth.model.AcAuth;

/**
 * 缓存工具类
 * @author Lanbo
 *
 */
public class CacheUtil {
	
	public static long getIphoneLoginTimeStamp(final String mac) {
		
		long time = CacheKit.get(Constants.DATACACHE, "iphoneLoginTimeStamp_"
				+ mac, new IDataLoader() {
			public Object load() {
				return 1L;
			}
		});
		return time;
	}

	public static void main(String[] args) {
		
		PropKit.use("config.properties");
		System.out.println(PropKit.use("config.properties").get(
				"memcached_url"));
		
	}
}
