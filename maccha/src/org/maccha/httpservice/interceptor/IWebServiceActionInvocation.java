package org.maccha.httpservice.interceptor;

import java.util.Iterator;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IWebServiceAction;

public interface IWebServiceActionInvocation {

	DataMessage getRequestDataMessage();

	DataMessage getResponseDataMessage();

	Iterator getInterceptors();

	String getService();
	
	IWebServiceAction getServiceAction();

	boolean isExecuted();

	String getResultCode();

	void setResultCode(String resultCode);

	String invoke() throws Exception;

	String invokeServiceOnly() throws Exception;
}
