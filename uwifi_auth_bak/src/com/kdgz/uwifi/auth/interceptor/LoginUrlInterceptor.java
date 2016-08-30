package com.kdgz.uwifi.auth.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

/**
 * login请求拦截器
 * @author Lanbo
 *
 */
public class LoginUrlInterceptor implements Interceptor {

	@Override
	public void intercept(ActionInvocation ai) {
		
		// do sothing
		
		ai.invoke();
	}

}
