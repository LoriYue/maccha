package org.maccha.httpservice;

import java.io.InputStream;
import org.maccha.httpservice.exception.WebServiceException;

public abstract interface IDataMessageParser {
	/**
	 * 将Response解析成字符串
	 * @param response
	 * @return RESPONSEMSG xml 字符串
	 */
	public String parseResponse(DataMessage response) throws WebServiceException;
	/**
	 * 将XML或者JSON字符串流请求字符串流转换成Request
	 * @param inputStream
	 * @return
	 */	
	public DataMessage parseRequest(InputStream inputStream) throws WebServiceException;
}
