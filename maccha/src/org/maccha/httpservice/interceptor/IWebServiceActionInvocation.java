package org.maccha.httpservice.interceptor;

import java.util.Iterator;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IWebServiceAction;

public abstract interface IWebServiceActionInvocation
{
  public abstract DataMessage getRequestDataMessage();

  public abstract DataMessage getResponseDataMessage();

  public abstract Iterator getInterceptors();

  public abstract String getService();

  public abstract IWebServiceAction getServiceAction();

  public abstract boolean isExecuted();

  public abstract String getResultCode();

  public abstract void setResultCode(String paramString);

  public abstract String invoke()
    throws Exception;

  public abstract String invokeServiceOnly()
    throws Exception;
}
