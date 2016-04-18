package org.maccha.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class QueryParamerInterceptor
  implements MethodInterceptor
{
  public Object invoke(MethodInvocation methodinvocation)
    throws Throwable
  {
    return methodinvocation.proceed();
  }
}
