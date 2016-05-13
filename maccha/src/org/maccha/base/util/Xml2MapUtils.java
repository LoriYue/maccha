package org.maccha.base.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;
import org.maccha.base.exception.SysException;
/**
 * <config>
 *		<property name="sqlDebugable" value="false"/>
 *		<workflow>
 *		<webInterface>
 *	   		<property name="workflowWebapp" value="http://localhost:8090/workflow"/>
 *	   		<property name="workflowWebappInternet" value="http://localhost:8090/workflow"/>
 *	   		<property name="bizWebapp" value="http://localhost:8090/superframe"/>
 *	   		<property name="bizWebappInternet" value="http://localhost:8090/superframe"/>
 *		</webInterface>
 *      </workflow>
 *      <sql-query name="com.simpro.activity.analyse.activitytrack.query2section">
 *		<![CDATA[
 *			select 1 from dual
 *		]]>
 *  	</sql-query>
 *      <property name="testable" value="true">false</property>
 * </config>
 * 转换成：{/config/property[@name=sqlDebugable]=false,
 * 		 /config/workflow/webInterface/property[@name=workflowWebapp]=http://localhost:8090/workflow,
 *       /config/workflow/webInterface/property[@name=workflowWebappInternet]=http://localhost:8090/workflow,
 *       /config/workflow/webInterface/property[@name=bizWebapp]=http://localhost:8090/superframe,
 *       /config/workflow/webInterface/property[@name=bizWebappInternet]=http://localhost:8090/superframe,
 *       /config/sql-query/com.simpro.activity.analyse.activitytrack.query2section=select 1 from dual,
 *       /config/property/testable=false,
 *       /config/property[@name=testable]=true
 *       }
 * 键值对对象,hashMap结构：节点名称和最后一个节点“name”属性用"/"连接作为hash key值，最后一个节点的”value“属性值或者“CDATA”值或节点值作为hash value
 * 配置文档
 */
public class Xml2MapUtils {
	private static Logger log = Logger.getLogger(Xml2MapUtils.class);
	public Xml2MapUtils(){
	}
	//记录XML标记
	private Stack<String> parentPathTagsStack = new Stack<String>();
	private Stack<String> tagsStack = new Stack<String>();
	private Map<String,String> xmlMap ;
	private String tagValue = null;

	/**
	 * 处理XML标记类型
	 * 
	 * @param xmlr
	 * @throws java.lang.Exception
	 */
	private void processXML(XMLStreamReader xmlr) throws Exception {
		switch (xmlr.getEventType()) {
			case XMLStreamConstants.START_ELEMENT:
				// 标记开始
				startParseElement(xmlr);
				processAttributes(xmlr);
				break;
			case XMLStreamConstants.END_ELEMENT:
				// 标记结束
				endParseElement(xmlr);
				break;
			case XMLStreamConstants.SPACE:
			case XMLStreamConstants.CDATA:
				tagValue = xmlr.getText();
			case XMLStreamConstants.CHARACTERS:
				int start = xmlr.getTextStart();
				int length = xmlr.getTextLength();
				tagValue = new String(xmlr.getTextCharacters(), start, length);
				//tagValue = xmlr.getText();
			case XMLStreamConstants.COMMENT:
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
		}
	}
	/**
	 * 处理标记名称
	 * 
	 * @param xmlr
	 * @param tag
	 */

	private void startParseElement(XMLStreamReader xmlr) {
		if (!xmlr.hasName())return;
		String localTagName = xmlr.getLocalName();
		if(parentPathTagsStack.isEmpty()){
			parentPathTagsStack.push("/"+localTagName);
		}else{
			parentPathTagsStack.push(parentPathTagsStack.peek()+"/"+localTagName);
		}
	}

	private void endParseElement(XMLStreamReader xmlr) {
		String localTagName = xmlr.getLocalName();
		String tagEntityPropName = null ;
		if(!tagsStack.isEmpty())	tagEntityPropName = tagsStack.peek();
		log.debug("tagEntityPropName="+tagEntityPropName+",localTagName="+localTagName+",tagValue =" + tagValue +"::");
		if(StringUtils.isNotNull(tagEntityPropName) && StringUtils.isNotNull(tagValue.replaceAll("\n", "").replaceAll("	", "").replaceAll(" ", ""))){
			xmlMap.put(tagEntityPropName, tagValue);
		}
		tagValue = null;
		parentPathTagsStack.pop();
		if(!tagsStack.isEmpty())	tagsStack.pop();
	}
	/**
	 * 处理节点属性
	 * 
	 * @param xmlr XML Stream对象
	 * @param index 属性索引号
	 */
	private void processAttributes(XMLStreamReader xmlr) {
		String strParentTagName = parentPathTagsStack.peek();
		String _name = null ;
		String _value = null ;
		for (int i = 0; i < xmlr.getAttributeCount(); i++) {
			String localAttrName = xmlr.getAttributeName(i).toString();
			String localAttrValue = xmlr.getAttributeValue(i);
			//System.out.println("localTagName = "+localTagName+",localAttrName = " + localAttrName +",localAttrValue="+localAttrValue);
			if ("name".equalsIgnoreCase(localAttrName)){
				_name = localAttrValue ;
			}else if ("value".equalsIgnoreCase(localAttrName)){
				_value = localAttrValue ;
			}
		}
		if(StringUtils.isNotNull(_name)){
			if(StringUtils.isNotNull(_value))xmlMap.put(strParentTagName+"[@name="+_name+"]", _value);
			tagsStack.push(strParentTagName+"/"+_name);
		}else{
			tagsStack.push("");
		}
	}

	/**
	 * 用SATX解析器解析XML格式内容
	 * 
	 * @param xml
	 *            XML字符串
	 */
	private Map<String,String> parserXML(InputStream in,Map _xmlMap) {
		if(_xmlMap == null)xmlMap=new HashMap<String,String>();
		else xmlMap = _xmlMap ;
		XMLStreamReader xmlr = null ;
		try {
			// Use reference implementation
			System.setProperty("javax.xml.stream.XMLInputFactory","com.bea.xml.stream.MXParserFactory");
			// Create an input factory
			XMLInputFactory xmlif = XMLInputFactory.newInstance();
			// Create an XML stream reader
			xmlr = xmlif.createXMLStreamReader(in, "utf-8");
			// Loop over XML input stream and process events
			while (xmlr.hasNext()) {
				processXML(xmlr);
				xmlr.next();
			}
			parentPathTagsStack = null;
			tagsStack = null;
		} catch (Throwable t) {
			SysException.handleMessageException(t.getMessage(), t);
			//ex.printStackTrace();
		} finally {
			try {
				if(xmlr != null)xmlr.close();
				if(in != null)in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return this.xmlMap;
	}
	public static void parserXML2Map(InputStream in,Map _xmlMap){
		Xml2MapUtils xml2MapUtils = new Xml2MapUtils();
		xml2MapUtils.parserXML(in,_xmlMap) ;
	}
	public static Map<String,String> parserXML2Map(InputStream in){
		Xml2MapUtils xml2MapUtils = new Xml2MapUtils();
		return xml2MapUtils.parserXML(in,null) ;
	}
}