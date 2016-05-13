package org.maccha.httpservice.util;

import java.io.InputStream;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IDataMessageParser;
import org.maccha.httpservice.exception.WebServiceException;

public class XmlDataMessageParserImpl implements IDataMessageParser {
	/**
	 * 将XML请求字符串流转换成Request
	 * @param inputStream
	 * @return
	 */
	public String parseResponse(DataMessage response) throws WebServiceException{
		return response.toXml() ;
	}
	/**
	 * 将Response解析成XML字符串
	 * @param response
	 * @return RESPONSEMSG xml 字符串
	 */
	public DataMessage parseRequest(InputStream inputStream) throws WebServiceException{
		return Xml2DataMessageUtilty.getDataMessage(inputStream) ;
	}


	public static void main(String[] args){
		try{
			java.io.FileInputStream in = new java.io.FileInputStream(new java.io.File("d:/person.xml"));
			XmlDataMessageParserImpl entityUtility = new XmlDataMessageParserImpl();
			System.out.println(entityUtility.parseRequest(in).toXml());
		}catch(Exception ex){
			ex.printStackTrace();			
		}		
	}
}
