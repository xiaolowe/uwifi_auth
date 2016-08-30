package com.kdgz.uwifi.auth.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.kdgz.uwifi.auth.controller.ACScriptController;
import com.kdgz.uwifi.auth.controller.PortalController;
import com.kdgz.uwifi.auth.controller.RouterController;
import com.kdgz.uwifi.auth.controller.WifidogController;
import com.kdgz.uwifi.auth.model.AcAuth;
import com.kdgz.uwifi.auth.model.AcConfig;
import com.kdgz.uwifi.auth.model.AcInfo;
import com.kdgz.uwifi.auth.model.AcStateTemp;
import com.kdgz.uwifi.auth.model.Actypeinfo;
import com.kdgz.uwifi.auth.model.Apply;
import com.kdgz.uwifi.auth.model.AuthSmsTemple;
import com.kdgz.uwifi.auth.model.AuthWeixinConfig;
import com.kdgz.uwifi.auth.model.BusinessInfo;
import com.kdgz.uwifi.auth.model.BusinessTemplet;
import com.kdgz.uwifi.auth.model.BwList;
import com.kdgz.uwifi.auth.model.Firmware;
import com.kdgz.uwifi.auth.model.Multiauthconfig;
import com.kdgz.uwifi.auth.model.SmsAcConfig;
import com.kdgz.uwifi.auth.model.TempletInfo;
import com.kdgz.uwifi.auth.model.TempletPage;
import com.kdgz.uwifi.auth.model.UserSmsAuth;

/**
 * JFinal配置
 * 
 * @author lanbo
 * 
 */
public class FinalConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {

		loadPropertyFile("config.properties");
		me.setDevMode(getPropertyToBoolean("devMode"));
		me.setViewType(ViewType.FREE_MARKER);
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("CPATH"));
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// me.add(new CommonInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {
		DruidPlugin cp = new DruidPlugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password"));
		// Druid连接池配置
		// 初始连接池大小、最小空闲连接数、最大活跃连接数
		cp.set(getPropertyToInt("initialSize", 1),
				getPropertyToInt("minIdle", 1),
				getPropertyToInt("maxActive", 10));
		// 配置获取连接等待超时的时间
		cp.setMaxWait(60000L);

		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		cp.setTimeBetweenEvictionRunsMillis(60000L);
		// 配置连接在池中最小生存的时间
		cp.setMinEvictableIdleTimeMillis(300000);
		// 配置发生错误时多久重连
		cp.setTimeBetweenConnectErrorMillis(30000L);

		// 是否打开连接泄露自动检测
		// cp.setRemoveAbandoned(true);
		// 连接长时间没有使用，被认为发生泄露时长
		// cp.setRemoveAbandonedTimeoutMillis(1800L);
		// cp.setLogAbandoned(true);

		// 是否缓存preparedStatement，即PSCache，对支持游标的数据库性能提升巨大，如 oracle、mysql 5.5
		// 及以上版本
		// cp.setMaxPoolPreparedStatementPerConnectionSize(20);

		me.add(cp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		me.add(arp);

		arp.addMapping("acconfig", AcConfig.class);
		arp.addMapping("acinfo", AcInfo.class);
		arp.addMapping("businesstemplet", BusinessTemplet.class);
		arp.addMapping("bwlist", BwList.class);
		arp.addMapping("templetinfo", TempletInfo.class);
		arp.addMapping("templetpage", TempletPage.class);
		arp.addMapping("acconfig", AcConfig.class);
		arp.addMapping("acstatetemp", AcStateTemp.class);
		arp.addMapping("acauth", AcAuth.class);
		arp.addMapping("authsmstemple", AuthSmsTemple.class);
		arp.addMapping("authweixinconfig", AuthWeixinConfig.class);
		arp.addMapping("businessinfo", BusinessInfo.class);
		arp.addMapping("authweixinconfig", AuthWeixinConfig.class);
		arp.addMapping("authweixinconfig", SmsAcConfig.class);
		arp.addMapping("usersmsauth", UserSmsAuth.class);
		arp.addMapping("multiauthconfig", Multiauthconfig.class);
		arp.addMapping("firmware", Firmware.class);
		arp.addMapping("apply", Apply.class);
		arp.addMapping("actypeinfo", Actypeinfo.class);
		me.add(new EhCachePlugin());
	}

	/**
	 * 路由配置
	 */
	@Override
	public void configRoute(Routes me) {
		me.add("/wifidog", WifidogController.class);
		me.add("/acScript", ACScriptController.class);
		me.add("/portal", PortalController.class);
		me.add("/router", RouterController.class);
	}

}
