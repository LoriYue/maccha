package org.maccha.httpservice.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.Base64Utils;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.DataSet;
import org.maccha.httpservice.Entity;
import org.maccha.httpservice.IDataMessageParser;
import org.maccha.httpservice.exception.WebServiceException;

public class JsonDataMessageParserImpl implements IDataMessageParser {
	/**
	 * 将Response解析成JSON字符串，格式如下:
			{
			 type:'response|response',
			 webServiceName:'服务名称',
			 messageType:'ERROR|WARNING|SUCCESS',
			 message:'返回信息',
			 parameterSet:[{gridPage:'页数',gridRowNumber:'每页条数',gridPageTotla:'总页数',gridRecodeNumber:'总记录数'}],
			 dataSet:[{
			 		name:对象名称,
			 		metadata:[{gridPropertyName:'属性名',gridColumnTitle:'列标题',gridColumnWidth:'列宽',gridColumnSortable:'true|false'}],
			 		entityList:[{属性名称:属性值,.....},{属性名称:属性值,.....},{属性名称:属性值,.....}]
			 }]
			}
	 * @param response
	 * @return RESPONSEMSG xml 字符串
	 */
	public String parseResponse(DataMessage response) throws WebServiceException{
		return response.toJson();
	}

	public DataMessage parseRequest(InputStream _inputStream)throws WebServiceException{
		DataMessage _requestMessage = new DataMessage();
		parseRequest(_inputStream,_requestMessage);
		return _requestMessage;
	}
	/**
	 * 将XML请求字符串流转换成Request
	 * @param inputStream
	 * @return
	 */	
	public void parseRequest(InputStream _inputStream,DataMessage _request) throws WebServiceException{
		String _jsonText = null;
		try{
		  _jsonText = stream2String(_inputStream);
		  if(Base64Utils.isBase64(_jsonText)){
			  _jsonText = StringUtils.deCompress(_jsonText);
		  }else{
			  _jsonText = StringUtils.unescape(_jsonText);
		  }
		}catch(Exception ex){
			ex.printStackTrace();			
			SysException.handleException(SysException.UNKNOWN, ex.getCause());
		}
		 parseRequest(_jsonText,_request);
		 return;
	}
	
	public DataMessage parseRequest(String _jsonText){
		DataMessage _requestMessage = new DataMessage();
		parseRequest(_jsonText,_requestMessage);
		return _requestMessage;
	}
	
	public void parseRequest(String _jsonText,DataMessage _requestMessage) throws WebServiceException{
		Gson gson = new Gson();
		try{
		  StringUtils.replace(_jsonText, "__", ".");
		  if(_jsonText.indexOf("\"") == 0)_jsonText = _jsonText.substring(1, _jsonText.length() -1) ;
		  System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::"+_jsonText);
		  //设置报文头
		  Map<String,Object> _dataMessageJSONObject = gson.fromJson(_jsonText, HashMap.class);
		  if(_dataMessageJSONObject.containsKey("webServiceName")){
			  _requestMessage.webServiceName = (String) _dataMessageJSONObject.get("webServiceName");  
		  }
		  if(_dataMessageJSONObject.containsKey("type")){
			  _requestMessage.type = (String) _dataMessageJSONObject.get("type");  
		  }
		  if(_dataMessageJSONObject.containsKey("version")){
			  _requestMessage.version = (String) _dataMessageJSONObject.get("version");  
		  }
		  if(_dataMessageJSONObject.containsKey("messageType")){
			  _requestMessage.messageType = (String) _dataMessageJSONObject.get("messageType");
		  }
		  if(_dataMessageJSONObject.containsKey("message")){
			  _requestMessage.message = (String) _dataMessageJSONObject.get("message");
		  }
		  //设置请求参数
		  Map<String,String> _parameterMap = (Map)_dataMessageJSONObject.get("parameterSet");
		  _requestMessage.setParameter(_parameterMap);
		  //设置DateSet数据
		  List<Object> _dateSetJSONArray = null ;
		  if(_dataMessageJSONObject.containsKey("dataSet")){
			  _dateSetJSONArray = (List) _dataMessageJSONObject.get("dataSet");//gson.fromJson(_dataMessageJSONObject.get("dataSet").toString(),ArrayList.class);
		  }
		  if(_dateSetJSONArray != null && !_dateSetJSONArray.isEmpty()){	
			  for(int i= 0; i < _dateSetJSONArray.size(); i++){
				  Map<String,Object> _dataSetJSONObject = (Map<String, Object>) _dateSetJSONArray.get(i);//gson.fromJson(_dateSetJSONArray.get(i).toString(),HashMap.class);
				  String _dataSetName = (String) _dataSetJSONObject.get("name");
				  DataSet _dataSet = _requestMessage.addDataSet(_dataSetName);
				  //解析metadata
				  List<Object> _metadataJSONArray = null; 
				  if(_dataSetJSONObject.containsKey("metadata")){
					  _metadataJSONArray = (List<Object>) _dataSetJSONObject.get("metadata");
				  }
				  if(_metadataJSONArray == null)_metadataJSONArray = new ArrayList() ;
				  for(int k = 0 ; k < _metadataJSONArray.size(); k++){
					  Map _metadataMap = new HashMap();
					  Map<String,Object> _metadataJSONObject = (Map<String, Object>) _metadataJSONArray.get(k);//gson.fromJson(_metadataJSONArray.get(k).toString(),HashMap.class);
					  java.util.Iterator _metadataJSONObjectKeys = _metadataJSONObject.keySet().iterator();
					  while(_metadataJSONObjectKeys.hasNext()){
						  String _metadataName = (String)_metadataJSONObjectKeys.next();
						  Object _metadataValue = _metadataJSONObject.get(_metadataName);
						  String _valueText = "" ;
						  if(_metadataValue instanceof JsonObject){
							  JsonObject _valueJSONObject = (JsonObject)_metadataValue ;
							  _valueText = _valueJSONObject.toString() ;
						  }else{
							  _valueText = (String)_metadataValue ;
						  }
						  _metadataMap.put(_metadataName, _valueText);
					  }					  
					  _dataSet.addMetadata(k + "",_metadataMap);
				  }
				  //解析entity
				  List<Object> jsonArrayEntity = null; 
				  if(_dataSetJSONObject.containsKey("entityList")){
					  jsonArrayEntity = (List<Object>) _dataSetJSONObject.get("entityList");//gson.fromJson(_dataSetJSONObject.get("entityList").toString(),ArrayList.class);
				  }
				  if(jsonArrayEntity == null)jsonArrayEntity = new ArrayList<Object>();
				  for(int j = 0 ; j < jsonArrayEntity.size(); j++){
					  Entity entity = new Entity() ;
					  Map<String,Object> jsonObjectEntity = (Map<String, Object>) jsonArrayEntity.get(j);//gson.fromJson(jsonArrayEntity.get(j).toString(),HashMap.class);
					  java.util.Iterator itrEntityKey = jsonObjectEntity.keySet().iterator();
					  while(itrEntityKey.hasNext()){
						  String strName = (String)itrEntityKey.next();
						  String strValue = String.valueOf(jsonObjectEntity.get(strName));
						  if(entity.EDITSTATE.equalsIgnoreCase(strName))entity.setEditState(strValue);
						  entity.setPropertyValue(strName, strValue);
					  }
					  _dataSet.addEntity(entity);
				  }
			  }
		  }
		}catch(Exception ex){
			WebServiceException.handleMessageException("报文解析错误", ex.getCause());
		}finally{
			gson = null ;
		}
		return ;
	}
	
	private String stream2String (InputStream in) throws IOException {
		System.out.println(":::::::::::::::::::::::::::::::::::hello!");
		int i = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((i = in.read()) != -1) {
		    baos.write(i);
		}
		return baos.toString("utf-8");
	}

}
