package com.kdgz.uwifi.auth.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.kdgz.uwifi.auth.bean.AcPingLog;
import com.kdgz.uwifi.auth.bean.AdLog;
import com.kdgz.uwifi.auth.bean.AppDownLoadLog;
import com.kdgz.uwifi.auth.bean.Property;
import com.kdgz.uwifi.auth.bean.RouterLog;
import com.kdgz.uwifi.auth.bean.WifidogLog;
import com.kdgz.uwifi.auth.util.CommonUtil;
import com.kdgz.uwifi.auth.util.DateUtil;

/**
 * Controller父类
 * 
 * @author lanbo
 * 
 */
public class BaseController extends Controller {

	private Property property;
	
	public static final Logger logger = Logger.getLogger(BaseController.class);

	public BaseController() {
		super();

		property = CacheKit.get("propertyCache", "property", new IDataLoader() {

			@Override
			public Object load() {
				ResourceBundle bundle = ResourceBundle.getBundle("common");

				Property newProperty = new Property();
				newProperty.setCsvLogPath(bundle.getString("csv_log_path"));
				newProperty.setDefaultAuthTpl(bundle
						.getString("default_auth_tpl"));
				newProperty.setDefaultPortalUrl(bundle
						.getString("default_portal_url"));
				newProperty.setFileProxyDomain(bundle
						.getString("file_proxy_domain"));
				newProperty.setFileProxyIp(bundle.getString("file_proxy_ip"));
				newProperty.setWeixinUrl(bundle.getString("weixin_url"));

				CacheKit.put("propertyCache", "property", newProperty);

				return newProperty;
			}
		});
	}

	/**
	 * 写入wifidog日志文件
	 * 
	 * @param log
	 */
	protected void writeWifidogLog(Object log) {
		String logFileName = "wifidog_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		String csvStr = CommonUtil.extractBeanToCsvStr(log, WifidogLog.getColumnArray());
		try {
			FileUtils.writeStringToFile(file, csvStr, "utf-8", true);
		} catch (IOException e) {
			logger.error("写入日志文件失败！", e);
		}

//		CsvOperateUtil.writeBeanToCsv(file, log, WifidogLog.getColumnArray(),
//				getWifiDogLogProcessors());
	}
	
	/**
	 * 写入wifidog日志文件
	 * 
	 * @param log
	 */
	protected void writeRouterLog(Object log) {
		String logFileName = "router_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		String csvStr = CommonUtil.extractBeanToCsvStr(log, RouterLog.getColumnArray());
		try {
			FileUtils.writeStringToFile(file, csvStr, "utf-8", true);
		} catch (IOException e) {
			logger.error("写入wifidog日志文件失败！", e);
		}
//		CsvOperateUtil.writeBeanToCsv(file, log, WifidogLog.getColumnArray(),
//				getRouterLogProcessors());

	}

	/**
	 * 写入acping日志文件
	 * 
	 * @param log
	 */
	protected void writeAcPingLog(Object log) {
		String logFileName = "ap_ping_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		String csvStr = CommonUtil.extractBeanToCsvStr(log, AcPingLog.getColumnArray());
		try {
			FileUtils.writeStringToFile(file, csvStr, "utf-8", true);
		} catch (IOException e) {
			logger.error("写入日志文件失败！", e);
		}

//		CsvOperateUtil.writeBeanToCsv(file, log, AcPingLog.getColumnArray(),
//				getAcPingLogProcessors());
	}
	
	/**
	 * 写入portal日志文件
	 * 
	 * @param log
	 */
	protected void writeAdLog(Object log) {
		String logFileName = "auth_ad_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		String csvStr = CommonUtil.extractBeanToCsvStr(log, AdLog.getColumnArray());
		try {
			FileUtils.writeStringToFile(file, csvStr, "utf-8", true);
		} catch (IOException e) {
			logger.error("写入日志文件失败！", e);
		}

//		CsvOperateUtil.writeBeanToCsv(file, log, AdLog.getColumnArray(),
//				getAdLogProcessors());
	}
	
	/**
	 * 写入APK下载日志文件
	 * 
	 * @param log
	 */
	protected void writeAppDownloadLog(Object log) {
		String logFileName = "app_download_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		String csvStr = CommonUtil.extractBeanToCsvStr(log, AppDownLoadLog.getColumnArray());
		try {
			FileUtils.writeStringToFile(file, csvStr, "utf-8", true);
		} catch (IOException e) {
			logger.error("写入APK下载日志文件失败！", e);
		}

	}
	
	/**
	 * 批量写入APK下载日志文件
	 * 
	 * @param log
	 */
	protected void writeAppDownloadLog(List log) {
		String logFileName = "app_download_log." + DateUtil.toYYYMMDDStr(new Date())
				+ ".log";
		File file = FileUtils.getFile(property.getCsvLogPath(), logFileName);
		
		if(log.size() > 0) {
			
			StringBuilder sb = new StringBuilder();
			for (Object object : log) {
				
				sb.append(CommonUtil.extractBeanToCsvStr(log, AppDownLoadLog.getColumnArray()));
			}
			
			try {
				FileUtils.writeStringToFile(file, sb.toString(), "utf-8", true);
			} catch (IOException e) {
				logger.error("批量写入APK下载日志文件失败！", e);
			}
		}
		

	}
	
	protected CellProcessor[] getAdLogProcessors() {

		return new CellProcessor[] { new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo("")
				 };

	}

	protected CellProcessor[] getWifiDogLogProcessors() {

		return new CellProcessor[] { new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo("") };

	}

	protected CellProcessor[] getAcPingLogProcessors() {

		return new CellProcessor[] { new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo(""),
				new ConvertNullTo(""), new ConvertNullTo("") };

	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

}
