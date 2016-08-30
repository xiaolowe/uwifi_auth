package com.kdgz.uwifi.auth.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.jfinal.kit.PropKit;

/**
 * memcached工具类
 * @author Lanbo
 *
 */
public class MemCachedUtil {

	private static MemcachedClient memcachedClient;
	
	private static int CLIENT_TIME_OUT = 300;

	private MemCachedUtil() {
	}

	// 设置缓存对象
	public static void add(String key, int second, Object obj)
			throws InterruptedException, MemcachedException {

		MemcachedClient client = getMemcachedClient();
		if (client == null) {
			return;
		}

		memcachedClient.addWithNoReply(key, second, obj);
	}

	// 替换缓存对象
	public static void replace(String key, int second, Object obj)
			throws InterruptedException, MemcachedException, TimeoutException {

		MemcachedClient client = getMemcachedClient();
		if (client == null) {
			return;
		}
		
		Object tempObj = get(key);
		
		if(tempObj == null) {
			memcachedClient.addWithNoReply(key, second, obj);
		} else {
			memcachedClient.replaceWithNoReply(key, second, obj);
		}
	}

	// 读取缓存对象
	public static Object get(String key) throws TimeoutException,
			InterruptedException, MemcachedException {

		MemcachedClient client = getMemcachedClient();
		if (client == null) {
			return null;
		}

		return memcachedClient.get(key, CLIENT_TIME_OUT);
	}

	// 删除缓存对象
	public static void delete(String key) throws InterruptedException, MemcachedException {

		MemcachedClient client = getMemcachedClient();
		if (client == null) {
			return;
		}
		memcachedClient.deleteWithNoReply(key);

	}
	
	public static void flushAll() throws TimeoutException, InterruptedException, MemcachedException {
		
		MemcachedClient client = getMemcachedClient();
		if (client == null) {
			return;
		}
		memcachedClient.flushAll();
	}

	private static MemcachedClient getMemcachedClient() {

		if (memcachedClient == null) {

			MemcachedClientBuilder builder = new XMemcachedClientBuilder(
					AddrUtil.getAddresses(PropKit.use("config.properties").get(
							"memcached_url")));
			
			// 连接超时时间
			builder.setConnectTimeout(CLIENT_TIME_OUT);

			try {
				memcachedClient = builder.build();
			} catch (IOException e) {

				return null;
			}
			return memcachedClient;
		}

		return memcachedClient;
	}
	
	public static void main(String[] args) {
		try {
			flushAll();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
