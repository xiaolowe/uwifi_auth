package com.kdgz.uwifi.auth.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author lanbo
 * 
 */
public class DateUtil {

	private static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";

	private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

	public static String toStr(Date date, String format) {

		SimpleDateFormat sf = new SimpleDateFormat(format);

		return sf.format(date);
	}

	public static String toYYYMMStr(Date date) {

		return toStr(date, DATE_FORMAT_YYYY_MM);
	}

	public static String toYYYMMDDStr(Date date) {

		return toStr(date, DATE_FORMAT_YYYY_MM_DD);
	}

}
