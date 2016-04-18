package org.maccha.httpservice.interceptor.impl;

import org.maccha.httpservice.interceptor.IWebServiceActionInvocation;
import org.maccha.httpservice.interceptor.IWebServiceIntercepter;

public abstract class AroundWebServiceInterceptor
  implements IWebServiceIntercepter
{
  public String intercept(IWebServiceActionInvocation invocation)
    throws Exception
  {
    String result = null;
    before(invocation);
    result = invocation.invoke();
    after(invocation, result);
    return result;
  }

  protected abstract void after(IWebServiceActionInvocation paramIWebServiceActionInvocation, String paramString)
    throws Exception;

  protected abstract void before(IWebServiceActionInvocation paramIWebServiceActionInvocation)
    throws Exception;
}
