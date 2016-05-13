package org.maccha.httpservice;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.TypeConvertorUtils;
import org.maccha.httpservice.util.JsonUtils;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class DataMessage implements Serializable{
	
	final String NDATA_MESSAGE = "dataMessage";
	final String NTYPE = "type";
	final String NPARAMETER = "parameter";
	final String NPARAMETER_SET = "parameterSet";
	final String NWEB_SERVICE_NAME = "webServiceName";
	final String NVERSION = "version";
	final String NDESC = "desc";
	final String NMESSAGE_TYPE = "messageType";
	final String NMESSAGE = "message";
	public String webServiceName = "";
	public String type = "";
	public String version = "1.0";
	public String messageType =SUCCESS_MESSAGE_TYPE;
	public String message =DEFAULT_SUCCESS_MESSAGE;
	private Map dataSetMap = new HashMap();
	private Map parameterMap = new HashMap();

	// 增加数据集
	public void addDataSet(DataSet dataSet) {
		dataSetMap.put(dataSet.dataSetName, dataSet);
	}

	public DataSet addDataSet(String dataSetName) {
		DataSet dataSet = new DataSet();
		dataSet.dataSetName = dataSetName;
		dataSetMap.put(dataSet.dataSetName, dataSet);
		return dataSet;
	}
	public void addDataSet(DataSet[] dataSet){
		for(DataSet t:dataSet){
			dataSetMap.put(t.dataSetName, t);
		}
	}
	public DataSet[] getDataSet() {
		DataSet[] dataSets = new DataSet[dataSetMap.size()] ;
		
		Iterator itr = dataSetMap.keySet().iterator();
		int i = 0 ;
		while(itr.hasNext()){
			String strName = (String)itr.next() ;
			dataSets[i] = (DataSet)dataSetMap.get(strName) ;
			i++ ;
		}
		return dataSets;
	}
	public DataSet getDataSet(String dataSetName) {
		return (DataSet) dataSetMap.get(dataSetName);
	}
	public void setParameter(Map parameterMap){
		this.parameterMap = parameterMap ;
	}
	// '设置参数数据
	public void setParameter(String parameterName, Object parameterValue) {
		parameterMap.put(parameterName, parameterValue);
		
	}
	public Map getParameter(){
		return this.parameterMap ;
	}
	public Object getParameter(String parameterName) {
		return this.parameterMap.get(parameterName);
	}
	public String getString(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (String)objValue ;
		return null ;
	}
	public Float getFloat(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (Float)TypeConvertorUtils.convert(objValue, Float.class);
		return Float.valueOf(0.0f) ;
	}
	public Integer getInteger(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (Integer)TypeConvertorUtils.convert(objValue, Integer.class);
		return Integer.valueOf(0) ;
	}
	public Double getDouble(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (Double)TypeConvertorUtils.convert(objValue, Double.class);
		return Double.valueOf(0.0f) ;
	}
	public Date getDate(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (Date)TypeConvertorUtils.convert(objValue, Date.class);
		return null ;
	}
	public Boolean getBoolean(String propertyName){
		Object objValue = parameterMap.get(propertyName);
		if(objValue != null) return (Boolean)TypeConvertorUtils.convert(objValue, Boolean.class);
		return null ;
	}	
	// '报文实例转换为xml文本
	public String toXml() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<?xml version='1.0' encoding='UTF-8'?><").append(NDATA_MESSAGE).append(">");
		xmlBuffer.append("<").append(NWEB_SERVICE_NAME).append(">").append(this.webServiceName).append("</").append(NWEB_SERVICE_NAME).append(">");
		xmlBuffer.append("<" ).append(NTYPE ).append( ">" ).append(this.type).append( "</").append( NTYPE ).append( ">");
		xmlBuffer.append("<" ).append(NVERSION ).append( ">" ).append(this.version).append( "</").append( NVERSION ).append( ">");
		xmlBuffer.append("<" ).append( NMESSAGE_TYPE ).append( ">" ).append(this.messageType ).append( "</" ).append( NMESSAGE_TYPE ).append( ">");
		xmlBuffer.append("<" ).append( NMESSAGE ).append( ">" ).append(this.message).append( "</" ).append(NMESSAGE ).append( ">");
		xmlBuffer.append(toParameterMapXml());
		xmlBuffer.append(toDataSetXml());
		xmlBuffer.append("</" ).append( NDATA_MESSAGE ).append( ">");
		return xmlBuffer.toString();
	}
	
	public String toJson(){
		
		StringBuffer buff = new StringBuffer("{");
		
		buff.append(JsonUtils.jsonNode(NWEB_SERVICE_NAME,this.webServiceName)).append(",\n");
		buff.append(JsonUtils.jsonNode(NTYPE ,this.type)).append(",\n");
		buff.append(JsonUtils.jsonNode(NVERSION,this.version)).append(",\n");
		buff.append(JsonUtils.jsonNode(NMESSAGE_TYPE,this.messageType)).append(",\n");
		
		buff.append(JsonUtils.jsonNode(NMESSAGE,this.message)).append(",\n");
		
		//解析parameter
		if(this.parameterMap != null && !this.parameterMap.isEmpty()){
			buff.append("\""+NPARAMETER_SET+"\"").append(":") ;
			String str = JsonUtils.parseMap2Json(this.parameterMap);
			if(str != null)buff.append(str);
		}else{
			buff.append("\""+NPARAMETER_SET+"\"").append(":{}") ;
		}
		buff.append(",\n");
		
		//解析DataSet
		buff.append("\""+DataSet.NDATASET+"\"").append(":[") ;
		Iterator itrDataSetMap = dataSetMap.keySet().iterator();
		String str = null ;
		int i = 0 ;
		while(itrDataSetMap.hasNext()){
		    if( i > 0 )buff.append( "," );
			Object objKey = itrDataSetMap.next();
			str = ((DataSet)dataSetMap.get(objKey)).toJson();
			if(str != null)buff.append(str) ;
			str = null ;
			i++ ;
		}
		buff.append("]}");
		//System.out.println(buff.toString());
		return buff.toString();
	}
	
	//参数信息转换为xml文本
	private String toParameterMapXml() {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<").append(NPARAMETER_SET).append(">");
		Object[] keys = parameterMap.keySet().toArray();
		for (int i = 0; i < parameterMap.size(); i++) {

			String strTemp = parameterMap.get(keys[i]) + "";
			if (StringUtils.isNull(strTemp))strTemp = "";
			xmlBuffer.append("<").append(NPARAMETER).append( " name='").append(keys[i]).append( "'>"
				).append(Xml2DataMessageUtilty.CDATA(strTemp)).append( "</").append( NPARAMETER ).append( ">");
			
		}
		xmlBuffer.append("</" ).append( NPARAMETER_SET ).append( ">");
		return xmlBuffer.toString();
	}
	//属性信息转换为xml文本
	private String toDataSetXml() {
		Object keys[] = dataSetMap.keySet().toArray();
		StringBuffer xmlBuffer = new StringBuffer(1024);
		for (int i = 0; i < dataSetMap.size(); i++) {
			xmlBuffer.append(((DataSet) dataSetMap.get(keys[i])).toXml());
		}
		return xmlBuffer.toString();
	}
	
	
	public static DataMessage getRequestDataMessage(String _webServiceName){
		DataMessage _requestDataMessage=new DataMessage();
		_requestDataMessage.webServiceName=_webServiceName;
		_requestDataMessage.type="request";
		return _requestDataMessage;
	}
	
	
	public static DataMessage getReponseDataMessage(){
		DataMessage _responseDataMessage=new DataMessage();
		_responseDataMessage.type="response";
		_responseDataMessage.messageType = SUCCESS_MESSAGE_TYPE;
		_responseDataMessage.message = DEFAULT_SUCCESS_MESSAGE;
		return _responseDataMessage;
	}
	@Override
	public String toString(){
		return toJson();
	}
	public static final String ERROR_MESSAGE_TYPE = "error";
	public static final String WARNING_MESSAGE_TYPE = "warning";
	public static final String CONFIRM_MESSAGE_TYPE = "confirm";
	public static final String SUCCESS_MESSAGE_TYPE = "success";
	public static final String DEFAULT_ERROR_MESSAGE = "操作失败";
	public static final String DEFAULT_WARNING_MESSAGE = "警告";
	public static final String DEFAULT_CONFIRM_MESSAGE = "确认信息";
	public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
	
	/**
	 * 以下针对返回消息报文 reponseMessage
	 **/
	public boolean isSuccess(){
		return SUCCESS_MESSAGE_TYPE.equals(this.messageType);
	}
	public boolean isError(){
		return ERROR_MESSAGE_TYPE.equals(this.messageType);
	}
	
	public boolean isClientError(){
		return ERROR_MESSAGE_TYPE.equals(this.messageType)&&"response".equals(this.type)&&StringUtils.isNull(webServiceName);
	}
	
	public boolean isWarning(){
		return WARNING_MESSAGE_TYPE.equals(this.messageType);
	}
	public boolean isConfirm(){
		return CONFIRM_MESSAGE_TYPE.equals(this.messageType);
	}
	
	public String getResult(){
		return this.messageType;
	}
	public String getMessage(){
		return this.message;
	}
	
	public void setSuccess(String _message){
		this.messageType = SUCCESS_MESSAGE_TYPE;
		this.message=_message;
	}
	
	public void setSuccess(){
		this.messageType = SUCCESS_MESSAGE_TYPE;
		this.message=this.DEFAULT_SUCCESS_MESSAGE;
	}
	
	public void setError(String _message){
		this.messageType = ERROR_MESSAGE_TYPE;
		this.message=_message;
	}
	
	public void setError(){
		this.messageType = ERROR_MESSAGE_TYPE;
		this.message=this.DEFAULT_ERROR_MESSAGE;
	}
	
	public void setWarning(String _message){
		this.messageType = WARNING_MESSAGE_TYPE;
		this.message=_message;
	}
	
	public void setWarning(){
		this.messageType = WARNING_MESSAGE_TYPE;
		this.message=this.DEFAULT_WARNING_MESSAGE;
	}
	
	public void setConfirm(String _message){
		this.messageType = CONFIRM_MESSAGE_TYPE;
		this.message=_message;
	}
	
	public void setConfirm(){
		this.messageType = CONFIRM_MESSAGE_TYPE;
		this.message=this.DEFAULT_CONFIRM_MESSAGE;
	}

}
