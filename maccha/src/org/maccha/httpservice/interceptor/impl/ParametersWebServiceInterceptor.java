package org.maccha.httpservice.interceptor.impl;

import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.httpservice.interceptor.IWebServiceActionInvocation;
import org.springframework.stereotype.Component;

@Component("ParametersWebServiceActionInvocation")
public class ParametersWebServiceInterceptor extends AroundWebServiceInterceptor {

	@Override
	protected void after(IWebServiceActionInvocation dispatcher, String result)
			throws Exception {
	}

	@Override
	protected void before(IWebServiceActionInvocation invocation) throws Exception {
		Map paraMap = invocation.getRequestDataMessage().getParameter();
		Object obj = invocation.getServiceAction();
		List<String> methods = ObjectUtils.getMethods(obj.getClass());
		for(String methodName : methods){
			if(methodName.startsWith("set")){
				methodName = methodName.substring(3,methodName.length());
				methodName=methodName.substring(0,1).toLowerCase()+methodName.substring(1,methodName.length());
				try{
					BeanUtils.setProperty(obj, methodName, null);	
					if(paraMap.containsKey(methodName)){
						BeanUtils.setProperty(obj, methodName, paraMap.get(methodName));
					}
				}catch(Exception ex){					
				}
			}
		}
	}
}
