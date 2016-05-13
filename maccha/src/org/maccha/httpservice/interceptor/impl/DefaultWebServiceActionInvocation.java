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
public class DefaultWebServiceActionInvocation implements IWebServiceActionInvocation {
	private boolean executed = false;
	private String resultCode;
	private String service;
	private IWebServiceAction serviceAction;
	private Iterator interceptors;
	private DataMessage requestDataMessage;
	private DataMessage responseDataMessage;

	public DefaultWebServiceActionInvocation(String service,
			DataMessage requestDataMessage, DataMessage responseDataMessage) {
		this.setService(service);
		this.setRequestDataMessage(requestDataMessage);
		this.setResponseDataMessage(responseDataMessage);
		this.initInterceptors();
	}

	public void initInterceptors() {
		List<String> interList = new ArrayList<String>();
		interList.add("ParametersWebServiceActionInvocation");
		String inters = (String) this.getRequestDataMessage().getParameter("interceptors");
		if (StringUtils.isNotNull(inters)) {
			String[] ints = inters.split("\\|");
			for (String str : ints) {
				if (StringUtils.isNotNull(str))	interList.add(str);
			}
		}
		this.setInterceptors(interList.iterator());
	}

	public DefaultWebServiceActionInvocation() {

	}

	public String getResultCode() {
		return resultCode;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
		this.setServiceAction(service);
	}

	/**
	 * 拦截器迭代
	 */
	public String invoke() throws Exception {
		if (executed) {
			throw new IllegalStateException("Service has already executed");
		}
		if (interceptors != null && interceptors.hasNext()) {
			IWebServiceIntercepter intercepter = null;
			String strName = (String) interceptors.next();
			try {
				intercepter = (IWebServiceIntercepter) SpringManager.getComponent(strName);
			} catch (Exception e) {
				//System.out.println(strName + ":拦截器未定义");
				throw new WebServiceException(strName + " not find in spring application*.xml!",e.getCause());
			}
			resultCode = intercepter.intercept(this);
		} else {
			resultCode = invokeServiceOnly();

		}
		if (!executed) {
			executed = true;
		}
		return resultCode;
	}

	/**
	 * Service执行
	 */
	public String invokeServiceOnly() throws Exception {
		serviceAction.execute(getRequestDataMessage(),getResponseDataMessage());
		return "success";
	}

	public boolean isExecuted() {
		return executed;
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
		return requestDataMessage;
	}

	public void setRequestDataMessage(DataMessage requestDataMessage) {
		this.requestDataMessage = requestDataMessage;
	}

	public DataMessage getResponseDataMessage() {
		return responseDataMessage;
	}

	public void setResponseDataMessage(DataMessage responseDataMessage) {
		this.responseDataMessage = responseDataMessage;
	}

	public IWebServiceAction getServiceAction() {
		return this.serviceAction;
	}

	public void setServiceAction(String Aservice) {
		this.serviceAction = (IWebServiceAction) SpringManager.getComponent(Aservice);
	}
}
