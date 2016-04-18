package org.maccha.httpservice.interceptor;

public abstract interface IWebServiceIntercepter
{
  public abstract String intercept(IWebServiceActionInvocation paramIWebServiceActionInvocation)
    throws Exception;
}
