package org.maccha.httpservice.interceptor.impl;

import org.maccha.httpservice.interceptor.IWebServiceActionInvocation;
import org.maccha.httpservice.interceptor.IWebServiceIntercepter;

public abstract class AroundWebServiceInterceptor implements IWebServiceIntercepter {
	public String intercept(IWebServiceActionInvocation invocation)throws Exception {
		String result = null;
		before(invocation);
		result = invocation.invoke();
		after(invocation, result);
		return result;
	}

	/**
	 * Called after the invocation has been executed.
	 * 
	 * @param result
	 *            the result value returned by the invocation
	 */
	protected abstract void after(IWebServiceActionInvocation dispatcher,
			String result) throws Exception;

	/**
	 * Called before the invocation has been executed.
	 */
	protected abstract void before(IWebServiceActionInvocation invocation)
			throws Exception;
}
