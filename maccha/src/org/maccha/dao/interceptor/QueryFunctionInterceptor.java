package org.maccha.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 数据权限拦截器
 * @author myliag
 *
 */
public class QueryFunctionInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation methodinvocation) throws Throwable {
		return methodinvocation.proceed();
	}

}
