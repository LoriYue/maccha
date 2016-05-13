package org.maccha.httpservice.controller;

import java.io.ByteArrayInputStream;
import org.apache.log4j.Logger;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.exception.WebServiceException;
import org.maccha.httpservice.interceptor.IWebServiceActionInvocation;
import org.maccha.httpservice.interceptor.impl.DefaultWebServiceActionInvocation;

public class ControllerDispatcher {
	private static Logger logger = Logger.getLogger(ControllerDispatcher.class);

	public static final String LOGIN_ACTION = "100000";
	
	/**
	 * webservice 分发
	 * @param _dataMessageRequest
	 * @return
	 */
	public static DataMessage  dispatch(String webServiceName,DataMessage _dataMessageRequest) {
		ByteArrayInputStream _inputStream = null;
		//生成空response对象
		DataMessage _dataMessageResponse = DataMessage.getReponseDataMessage();
		
		String _webServiceName = null ;
		if(StringUtils.isNull(webServiceName)){
			_dataMessageResponse.webServiceName = _dataMessageRequest.webServiceName;
			_webServiceName = _dataMessageRequest.webServiceName;
		}else{
			_dataMessageResponse.webServiceName = webServiceName;
			_webServiceName = webServiceName;
		}
		try {
			//获得服务对象
			logger.info(":::::::::::::::::::::::ControllerDispatcher _webServiceName = " + _webServiceName);
			IWebServiceActionInvocation _webServiceActionInvocation = new DefaultWebServiceActionInvocation(_webServiceName,_dataMessageRequest,_dataMessageResponse);
			if (_webServiceActionInvocation==null||_webServiceActionInvocation.getServiceAction() == null) {
				WebServiceException.handleMessageException("不能获得 " + _webServiceName + " 的服务信息");
			}
			//业务处理
			_webServiceActionInvocation.invoke();
		} catch (WebServiceException ex) {
			//ex.printStackTrace();
			_dataMessageResponse.setError("业务逻辑错误,错误信息为：【"+ex.getMessage()+"】");
			logger.error("进行服务调用时发生业务逻辑错误", ex);
		} catch (Exception ex) {
			//ex.printStackTrace();
			_dataMessageResponse.setError("未知错误,错误信息为：【"+ex.getMessage()+"】");
			logger.error("进行服务调用时发生未知错误", ex);
		} finally {
			// 清空对象
			if (_dataMessageRequest != null) {
				_dataMessageRequest = null ;
			}
			if (_inputStream != null) {
				try{
					_inputStream.close();
				}catch(java.io.IOException ioEx){}
			}
		}
		//返回response对象
		return _dataMessageResponse;
	}	
}
