package org.maccha.httpservice;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.maccha.base.util.StringUtils;
import org.maccha.base.util.TypeConvertorUtils;
import org.maccha.httpservice.util.JsonUtils;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class DataMessage
  implements Serializable
{
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
  public String messageType = "success";
  public String message = "操作成功";

  private Map dataSetMap = new HashMap();

  private Map parameterMap = new HashMap();
  public static final String ERROR_MESSAGE_TYPE = "error";
  public static final String WARNING_MESSAGE_TYPE = "warning";
  public static final String CONFIRM_MESSAGE_TYPE = "confirm";
  public static final String SUCCESS_MESSAGE_TYPE = "success";
  public static final String DEFAULT_ERROR_MESSAGE = "操作失败";
  public static final String DEFAULT_WARNING_MESSAGE = "警告";
  public static final String DEFAULT_CONFIRM_MESSAGE = "确认信息";
  public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";

  public void addDataSet(DataSet dataSet)
  {
    this.dataSetMap.put(dataSet.dataSetName, dataSet);
  }

  public DataSet addDataSet(String dataSetName) {
    DataSet dataSet = new DataSet();
    dataSet.dataSetName = dataSetName;
    this.dataSetMap.put(dataSet.dataSetName, dataSet);
    return dataSet;
  }
  public void addDataSet(DataSet[] dataSet) {
    for (DataSet t : dataSet)
      this.dataSetMap.put(t.dataSetName, t);
  }

  public DataSet[] getDataSet()
  {
    DataSet[] dataSets = new DataSet[this.dataSetMap.size()];

    Iterator itr = this.dataSetMap.keySet().iterator();
    int i = 0;
    while (itr.hasNext()) {
      String strName = (String)itr.next();
      dataSets[i] = ((DataSet)this.dataSetMap.get(strName));
      i++;
    }
    return dataSets;
  }
  public DataSet getDataSet(String dataSetName) {
    return (DataSet)this.dataSetMap.get(dataSetName);
  }
  public void setParameter(Map parameterMap) {
    this.parameterMap = parameterMap;
  }

  public void setParameter(String parameterName, Object parameterValue) {
    this.parameterMap.put(parameterName, parameterValue);
  }

  public Map getParameter() {
    return this.parameterMap;
  }
  public Object getParameter(String parameterName) {
    return this.parameterMap.get(parameterName);
  }
  public String getString(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (String)objValue;
    return null;
  }
  public Float getFloat(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (Float)TypeConvertorUtils.convert(objValue, Float.class);
    return Float.valueOf(0.0F);
  }
  public Integer getInteger(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (Integer)TypeConvertorUtils.convert(objValue, Integer.class);
    return Integer.valueOf(0);
  }
  public Double getDouble(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (Double)TypeConvertorUtils.convert(objValue, Double.class);
    return Double.valueOf(0.0D);
  }
  public Date getDate(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (Date)TypeConvertorUtils.convert(objValue, Date.class);
    return null;
  }
  public Boolean getBoolean(String propertyName) {
    Object objValue = this.parameterMap.get(propertyName);
    if (objValue != null) return (Boolean)TypeConvertorUtils.convert(objValue, Boolean.class);
    return null;
  }

  public String toXml()
  {
    StringBuffer xmlBuffer = new StringBuffer();
    xmlBuffer.append("<?xml version='1.0' encoding='UTF-8'?><").append("dataMessage").append(">");
    xmlBuffer.append("<").append("webServiceName").append(">").append(this.webServiceName).append("</").append("webServiceName").append(">");

    xmlBuffer.append("<").append("type").append(">").append(this.type).append("</").append("type").append(">");

    xmlBuffer.append("<").append("version").append(">").append(this.version).append("</").append("version").append(">");

    xmlBuffer.append("<").append("messageType").append(">").append(this.messageType).append("</").append("messageType").append(">");

    xmlBuffer.append("<").append("message").append(">").append(this.message).append("</").append("message").append(">");

    xmlBuffer.append(toParameterMapXml());
    xmlBuffer.append(toDataSetXml());
    xmlBuffer.append("</").append("dataMessage").append(">");
    return xmlBuffer.toString();
  }

  public String toJson()
  {
    StringBuffer buff = new StringBuffer("{");

    buff.append(JsonUtils.jsonNode("webServiceName", this.webServiceName)).append(",\n");
    buff.append(JsonUtils.jsonNode("type", this.type)).append(",\n");
    buff.append(JsonUtils.jsonNode("version", this.version)).append(",\n");
    buff.append(JsonUtils.jsonNode("messageType", this.messageType)).append(",\n");

    buff.append(JsonUtils.jsonNode("message", this.message)).append(",\n");

    if ((this.parameterMap != null) && (!this.parameterMap.isEmpty())) {
      buff.append("\"parameterSet\"").append(":");
      String str = JsonUtils.parseMap2Json(this.parameterMap);
      if (str != null) buff.append(str); 
    }
    else {
      buff.append("\"parameterSet\"").append(":{}");
    }
    buff.append(",\n");

    buff.append("\"dataSet\"").append(":[");
    Iterator itrDataSetMap = this.dataSetMap.keySet().iterator();
    String str = null;
    int i = 0;
    while (itrDataSetMap.hasNext()) {
      if (i > 0) buff.append(",");
      Object objKey = itrDataSetMap.next();
      str = ((DataSet)this.dataSetMap.get(objKey)).toJson();
      if (str != null) buff.append(str);
      str = null;
      i++;
    }
    buff.append("]}");

    return buff.toString();
  }

  private String toParameterMapXml()
  {
    StringBuffer xmlBuffer = new StringBuffer();
    xmlBuffer.append("<").append("parameterSet").append(">");
    Object[] keys = this.parameterMap.keySet().toArray();
    for (int i = 0; i < this.parameterMap.size(); i++)
    {
      String strTemp = this.parameterMap.get(keys[i]) + "";
      if (StringUtils.isNull(strTemp)) strTemp = "";
      xmlBuffer.append("<").append("parameter").append(" name='").append(keys[i]).append("'>").append(Xml2DataMessageUtilty.CDATA(strTemp)).append("</").append("parameter").append(">");
    }

    xmlBuffer.append("</").append("parameterSet").append(">");
    return xmlBuffer.toString();
  }

  private String toDataSetXml() {
    Object[] keys = this.dataSetMap.keySet().toArray();
    StringBuffer xmlBuffer = new StringBuffer(1024);
    for (int i = 0; i < this.dataSetMap.size(); i++) {
      xmlBuffer.append(((DataSet)this.dataSetMap.get(keys[i])).toXml());
    }
    return xmlBuffer.toString();
  }

  public static DataMessage getRequestDataMessage(String _webServiceName)
  {
    DataMessage _requestDataMessage = new DataMessage();
    _requestDataMessage.webServiceName = _webServiceName;
    _requestDataMessage.type = "request";
    return _requestDataMessage;
  }

  public static DataMessage getReponseDataMessage()
  {
    DataMessage _responseDataMessage = new DataMessage();
    _responseDataMessage.type = "response";
    _responseDataMessage.messageType = "success";
    _responseDataMessage.message = "操作成功";
    return _responseDataMessage;
  }

  public String toString() {
    return toJson();
  }

  public boolean isSuccess()
  {
    return "success".equals(this.messageType);
  }
  public boolean isError() {
    return "error".equals(this.messageType);
  }

  public boolean isClientError() {
    return ("error".equals(this.messageType)) && ("response".equals(this.type)) && (StringUtils.isNull(this.webServiceName));
  }

  public boolean isWarning() {
    return "warning".equals(this.messageType);
  }
  public boolean isConfirm() {
    return "confirm".equals(this.messageType);
  }

  public String getResult() {
    return this.messageType;
  }
  public String getMessage() {
    return this.message;
  }

  public void setSuccess(String _message)
  {
    this.messageType = "success";
    this.message = _message;
  }

  public void setSuccess() {
    this.messageType = "success";
    this.message = "操作成功";
  }

  public void setError(String _message) {
    this.messageType = "error";
    this.message = _message;
  }

  public void setError() {
    this.messageType = "error";
    this.message = "操作失败";
  }

  public void setWarning(String _message) {
    this.messageType = "warning";
    this.message = _message;
  }

  public void setWarning() {
    this.messageType = "warning";
    this.message = "警告";
  }

  public void setConfirm(String _message) {
    this.messageType = "confirm";
    this.message = _message;
  }

  public void setConfirm() {
    this.messageType = "confirm";
    this.message = "确认信息";
  }

}
