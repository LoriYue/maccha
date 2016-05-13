package org.maccha.httpservice.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.DataSet;
import org.maccha.httpservice.Entity;

public class Xml2DataMessageUtilty {
	
	public Xml2DataMessageUtilty(DataMessage _dataMessage){
		dataMessage=_dataMessage;
	}
	// 记录XML标记
	private Stack parentTagsStack = new Stack();
	private Stack tagsStack = new Stack();
	private Stack modelsStack = new Stack();
	private DataMessage dataMessage;
	private String tagValue = null;
	private String tagEntityPropName = null;

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
		if (!xmlr.hasName())
			return;
		String localTagName = xmlr.getLocalName();
		// msg body
		if ("DATASET".equalsIgnoreCase(localTagName)) {
			DataSet dataSet = new DataSet();
			// 遇到dataSet新增数据集
			modelsStack.push(dataSet);
		}
		if ("METADATA".equalsIgnoreCase(localTagName)) {
			// METADATA/PROPERTY
			parentTagsStack.push("METADATA");
		}
		if ("ENTITY".equalsIgnoreCase(localTagName)) {
			// ENTITY/PROPERTY
			parentTagsStack.push("ENTITY");
			// 遇到entity 新增实体
			modelsStack.push(new Entity());
		}
		tagsStack.push(localTagName);
	}

	private void endParseElement(XMLStreamReader xmlr) {
		String localTagName = (String) tagsStack.peek();
		String strParentTagName = null;
		if ("webServiceName".equalsIgnoreCase(localTagName) && tagValue != null) {
			dataMessage.webServiceName = tagValue;
		} else if ("type".equalsIgnoreCase(localTagName)
				&& tagValue != null) {
			dataMessage.type = tagValue;
		} else if ("messageType".equalsIgnoreCase(localTagName)
				&& tagValue != null) {
			dataMessage.messageType = tagValue;
		} else if ("message".equalsIgnoreCase(localTagName)
				&& tagValue != null) {
			dataMessage.message = tagValue;
		} else if ("version".equalsIgnoreCase(localTagName)
				&& tagValue != null) {
			dataMessage.version = tagValue;
			
		}
		
		if ("PARAMETER".equalsIgnoreCase(localTagName)) {
			if (tagValue == null)
				tagValue = "";
			dataMessage.setParameter(tagEntityPropName, tagValue);
			tagEntityPropName = null;
		}
		if ("METADATA".equalsIgnoreCase(localTagName)) {
			parentTagsStack.pop();
		}
		// 处理Tag名称
		try {
			strParentTagName = (String) parentTagsStack.peek();
		} catch (Exception ex) {
		}

		if ("ENTITY".equalsIgnoreCase(strParentTagName) && "PROPERTY".equalsIgnoreCase(localTagName)) {
			Entity entity = (Entity) modelsStack.peek();
			if (tagValue != null) {
				//if (tagValue.length() > 20)System.out.print(tagValue);
				if(entity.EDITSTATE.equalsIgnoreCase(tagEntityPropName))entity.setEditState(tagValue);
				entity.setPropertyValue(tagEntityPropName, tagValue);
			}
			tagEntityPropName = null;
		}
		if ("ENTITY".equalsIgnoreCase(localTagName)) {
			Entity entity = (Entity) modelsStack.peek();
			// if(tagValue!=null)entity.setPropertyValue(tagEntityPropName,
			// tagValue);
			// tagEntityPropName = null;
			parentTagsStack.pop();
			modelsStack.pop();
			DataSet dataSet = (DataSet) modelsStack.peek();
			dataSet.addEntity(entity);
		}
		if ("DATASET".equalsIgnoreCase(localTagName)) {
			DataSet dataSet = (DataSet) modelsStack.peek();
			dataMessage.addDataSet(dataSet);
			modelsStack.pop();
		}
		tagValue = null;
		tagsStack.pop();
	}

	private void processAttributes(XMLStreamReader xmlr) {
		String localTagName = xmlr.getLocalName();// (String)
		// tagsStack.peek();
		String strParentTagName = null;
		try {
			strParentTagName = (String) parentTagsStack.peek();
		} catch (Exception e) {
			
		}
		// 处理MetaData
		if ("METADATA".equalsIgnoreCase(strParentTagName) && "PROPERTY".equalsIgnoreCase(localTagName)) {
			DataSet dataSet = (DataSet) modelsStack.peek();
			HashMap hashMetadata = new HashMap();
			String strMetadataName = null;
			for (int i = 0; i < xmlr.getAttributeCount(); i++) {
				String localAttrName = xmlr.getAttributeName(i).toString();
				String localAttrValue = xmlr.getAttributeValue(i);
				if ("name".equalsIgnoreCase(localAttrName))
					strMetadataName = localAttrValue;
				hashMetadata.put(localAttrName, localAttrValue);
			}
			if (strMetadataName == null)
				strMetadataName = (dataSet.getMetadataCount() + 1) + "";
			dataSet.addMetadata(strMetadataName, hashMetadata);

		} else if ("PARAMETER".equalsIgnoreCase(localTagName)) {
			String localAttrName = xmlr.getAttributeName(0).toString();
			String localAttrValue = xmlr.getAttributeValue(0);
			if ("name".equalsIgnoreCase(localAttrName)) {
				tagEntityPropName = localAttrValue;
			}

		} else {
			for (int i = 0; i < xmlr.getAttributeCount(); i++)
				processAttribute(xmlr, i);
		}
	}

	/**
	 * 处理节点属性
	 * 
	 * @param xmlr
	 *            XML Stream对象
	 * @param index
	 *            属性索引号
	 */
	private void processAttribute(XMLStreamReader xmlr, int index) {
		String localAttrName = xmlr.getAttributeName(index).toString();
		String localAttrValue = xmlr.getAttributeValue(index);
		String localTagName = (String) tagsStack.peek();
		String strParentTagName = null;
		try {
			strParentTagName = (String) parentTagsStack.peek();
		} catch (Exception e) {
		}
		// msg body
		if ("ENTITY".equalsIgnoreCase(strParentTagName)
				&& "PROPERTY".equalsIgnoreCase(localTagName)) {
			if ("name".equalsIgnoreCase(localAttrName)) {
				tagEntityPropName = localAttrValue;
			}
		}
		if ("DATASET".equalsIgnoreCase(localTagName)) {
			if ("name".equalsIgnoreCase(localAttrName)) {
				((DataSet) modelsStack.peek()).dataSetName = localAttrValue;
			}
		}
	}

	/**
	 * 用SATX解析器解析XML格式内容
	 * 
	 * @param xml
	 *            XML字符串
	 */
	private DataMessage parserXML(InputStream in) {
		try {
			// Use reference implementation
			System.setProperty("javax.xml.stream.XMLInputFactory",
					"com.bea.xml.stream.MXParserFactory");
			// Create an input factory
			XMLInputFactory xmlif = XMLInputFactory.newInstance();
			// Create an XML stream reader
			XMLStreamReader xmlr = xmlif.createXMLStreamReader(in, "utf-8");
			// Loop over XML input stream and process events
			while (xmlr.hasNext()) {
				processXML(xmlr);
				xmlr.next();
			}
			xmlr.close();
			parentTagsStack = null;
			tagsStack = null;
			modelsStack = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception ex) {
			}
		}
		return this.dataMessage;
	}

	public static DataMessage getDataMessage(InputStream in,DataMessage _dataMessage) {
		Xml2DataMessageUtilty dataMessageUtils = new Xml2DataMessageUtilty(_dataMessage);
		DataMessage dataMessage = dataMessageUtils.parserXML(in);
		return dataMessage;
	}
	
	public static DataMessage getDataMessage(InputStream in) {
		return getDataMessage(in,new DataMessage());
	}
	public static String CDATA(String str) {
		return "<![CDATA[" + str + "]]>";
	}
}
