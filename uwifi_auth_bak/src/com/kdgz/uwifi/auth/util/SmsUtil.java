package com.kdgz.uwifi.auth.util;

import cn.b2m.eucp.sdkhttp.cilent.SingletonClient;

/**
 * 短信发送工具类
 * 
 * @author lanbo
 * 
 */
public class SmsUtil {

	/**
	 * 发送短信、可以发送定时和即时短信 sendSMS(String[] mobiles,String smsContent, String
	 * addSerial, int smsPriority) 1、mobiles 手机数组长度不能超过1000 2、smsContent
	 * 最多500个汉字或1000个纯英文
	 * 、请客户不要自行拆分短信内容以免造成混乱、亿美短信平台会根据实际通道自动拆分、计费以实际拆分条数为准、亿美推荐短信长度70字以内
	 * 3、addSerial 附加码(长度小于15的字符串) 用户可通过附加码自定义短信类别,或添加自定义主叫号码( 联系亿美索取主叫号码列表)
	 * 4、优先级范围1~5，数值越高优先级越高(相对于同一序列号) 5、其它短信发送请参考使用手册自己尝试使用
	 */
	public static int sendSMS(String mobile, String smsContent,
			String addSerial, int smsPriority) {
		int i = -1;
		try {
			i = SingletonClient.getClient().sendSMS(new String[] { mobile }, smsContent,
					addSerial, smsPriority);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return i;
	}
	
	public static void main(String[] args) {
		System.out.println(sendSMS("18009691019", "测试测试测试【合肥城市云】", "",5));
	}

}
