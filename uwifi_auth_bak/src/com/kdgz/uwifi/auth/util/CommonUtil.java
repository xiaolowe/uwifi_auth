package com.kdgz.uwifi.auth.util;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.util.MethodCache;

/**
 * 共通工具类
 * 
 * @author lanbo
 * 
 */
public class CommonUtil {

	/**
	 * 生成加密密码串
	 * 
	 * @param pass
	 * @param salt
	 * @return
	 */
	public static String generateSaltPassword(String pass, String salt) {

		String returnStr = EncryptUtil.md5(EncryptUtil.md5(pass).substring(8)
				+ salt);

		return returnStr;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> getUrlParams(String url) {
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;

		URL clientUrl = null;
		try {
			clientUrl = new URL(url);
		} catch (MalformedURLException e) {
		}
		String strUrlParam = clientUrl.getQuery();
		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}
	
	/**
	 * 解析出url中的host部分
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求host部分
	 */
	public static String getUrlHost(String url) {
		
		URL clientUrl = null;
		try {
			clientUrl = new URL(url);
		} catch (MalformedURLException e) {
			return "";
		}
		String urlWithNoParam = clientUrl.getProtocol() + "://" +  clientUrl.getHost();
		
		return urlWithNoParam;
	}

	/**
	 * 将Bean转换成CSV行
	 * 
	 * @return
	 */
	public static String extractBeanToCsvStr(Object bean, String[] columns) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < columns.length; ++i) {

			String fieldName = columns[i];

			MethodCache cache = new MethodCache();

			if (fieldName == null) {
				sb.append("");
			} else {
				Method getMethod = cache.getGetMethod(bean, fieldName);
				try {
					Object filedObj = getMethod.invoke(bean, new Object[0]);
					
					String filed = filedObj != null ? String.valueOf(filedObj) : "";
					
					// 处理网址这样的特殊URL 带，或者"
					if(StringUtils.contains(filed, "\"") || StringUtils.contains(filed, ",")) {
						filed = StringUtils.replace(filed, "\"", "\"\"");
						
						StringBuilder tmpFiled = new StringBuilder();
						tmpFiled.append("\"");
						tmpFiled.append(filed);
						tmpFiled.append("\"");
						
						sb.append(tmpFiled.toString());
					} else {
						sb.append(filed);
					}
					
					if (i < columns.length - 1) {
						
						sb.append(",");
					}

				} catch (Exception e) {
					throw new SuperCsvReflectionException(String.format(
							"error extracting bean value for field %s",
							new Object[] { fieldName }), e);
				}
			}

		}
		sb.append(IOUtils.LINE_SEPARATOR_UNIX);

		return sb.toString();
	}
}
