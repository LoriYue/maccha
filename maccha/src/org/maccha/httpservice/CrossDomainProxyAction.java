package org.maccha.httpservice;

import org.maccha.base.exception.BaseException;
import org.maccha.base.exception.SysException;
import org.maccha.httpservice.client.DataMessageSender;
import org.springframework.stereotype.Component;

@Component("CROSSDOMAINACTION")
public class CrossDomainProxyAction implements IWebServiceAction {
	public void execute(DataMessage _requestDataMessage, DataMessage _responseDataMessage) throws BaseException {
		String _url = _requestDataMessage.getString("url");
		String _serverAction = _requestDataMessage.getString("serverAction");
		_requestDataMessage.webServiceName = _serverAction;
		_requestDataMessage.getParameter().remove("url");
		_requestDataMessage.getParameter().remove("serverAction");
		try {
			DataMessage _resultDataMessage = DataMessageSender.sendXML(_url+DataMessageSender.dataMessageServiceName,_requestDataMessage);
			_responseDataMessage.addDataSet(_resultDataMessage.getDataSet());
			_responseDataMessage.setParameter(_resultDataMessage.getParameter());
			_responseDataMessage.messageType = _resultDataMessage.messageType;
			_responseDataMessage.message = _resultDataMessage.message;
			
		} catch (Exception e) {
			e.printStackTrace();
			SysException.handleException(BaseException.UNKNOWN, e);
		}
	}
}
