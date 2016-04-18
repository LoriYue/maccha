package org.maccha.httpservice.interceptor.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.maccha.spring.SpringManager;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IWebServiceAction;
import org.maccha.httpservice.exception.WebServiceException;
import org.maccha.httpservice.interceptor.IWebServiceActionInvocation;
import org.maccha.httpservice.interceptor.IWebServiceIntercepter;
import org.springframework.stereotype.Component;

@Component("DefaultWebServiceActionInvocation")
public class DefaultWebServiceActionInvocation
  implements IWebServiceActionInvocation
{
  private boolean executed = false;
  private String resultCode;
  private String service;
  private IWebServiceAction serviceAction;
  private Iterator interceptors;
  private DataMessage requestDataMessage;
  private DataMessage responseDataMessage;

  public DefaultWebServiceActionInvocation(String service, DataMessage requestDataMessage, DataMessage responseDataMessage)
  {
    setService(service);
    setRequestDataMessage(requestDataMessage);
    setResponseDataMessage(responseDataMessage);
    initInterceptors();
  }

  public void initInterceptors() {
    List interList = new ArrayList();
    interList.add("ParametersWebServiceActionInvocation");
    String inters = (String)getRequestDataMessage().getParameter("interceptors");
    if (StringUtils.isNotNull(inters)) {
      String[] ints = inters.split("\\|");
      for (String str : ints) {
        if (!StringUtils.isNotNull(str)) continue; interList.add(str);
      }
    }
    setInterceptors(interList.iterator());
  }

  public DefaultWebServiceActionInvocation()
  {
  }

  public String getResultCode() {
    return this.resultCode;
  }

  public String getService() {
    return this.service;
  }

  public void setService(String service) {
    this.service = service;
    setServiceAction(service);
  }

  public String invoke()
    throws Exception
  {
    if (this.executed) {
      throw new IllegalStateException("Service has already executed");
    }
    if ((this.interceptors != null) && (this.interceptors.hasNext())) {
      IWebServiceIntercepter intercepter = null;
      String strName = (String)this.interceptors.next();
      try {
        intercepter = (IWebServiceIntercepter)SpringManager.getComponent(strName);
      }
      catch (Exception e) {
        throw new WebServiceException(strName + " not find in spring application*.xml!", e.getCause());
      }
      this.resultCode = intercepter.intercept(this);
    } else {
      this.resultCode = invokeServiceOnly();
    }

    if (!this.executed) {
      this.executed = true;
    }
    return this.resultCode;
  }

  public String invokeServiceOnly()
    throws Exception
  {
    this.serviceAction.execute(getRequestDataMessage(), getResponseDataMessage());
    return "success";
  }

  public boolean isExecuted() {
    return this.executed;
  }

  public void setResultCode(String resultCode) {
    this.resultCode = resultCode;
  }

  public void setExecuted(boolean executed) {
    this.executed = executed;
  }

  public void setInterceptors(Iterator interceptors) {
    this.interceptors = interceptors;
  }

  public Iterator getInterceptors() {
    return this.interceptors;
  }

  public DataMessage getRequestDataMessage() {
    return this.requestDataMessage;
  }

  public void setRequestDataMessage(DataMessage requestDataMessage) {
    this.requestDataMessage = requestDataMessage;
  }

  public DataMessage getResponseDataMessage() {
    return this.responseDataMessage;
  }

  public void setResponseDataMessage(DataMessage responseDataMessage) {
    this.responseDataMessage = responseDataMessage;
  }

  public IWebServiceAction getServiceAction() {
    return this.serviceAction;
  }

  public void setServiceAction(String Aservice) {
    this.serviceAction = ((IWebServiceAction)SpringManager.getComponent(Aservice));
  }
}
