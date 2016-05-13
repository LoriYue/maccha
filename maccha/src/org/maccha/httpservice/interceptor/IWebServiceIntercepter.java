package org.maccha.httpservice.interceptor;


public abstract interface IWebServiceIntercepter {
	public String intercept(IWebServiceActionInvocation actionInvocation) throws Exception;
}
