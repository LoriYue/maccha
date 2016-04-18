package org.maccha.httpservice.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.DataSet;
import org.maccha.httpservice.Entity;

public class Xml2DataMessageUtilty
{
  private Stack parentTagsStack = new Stack();

  private Stack tagsStack = new Stack();

  private Stack modelsStack = new Stack();
  private DataMessage dataMessage;
  private String tagValue = null;

  private String tagEntityPropName = null;

  public Xml2DataMessageUtilty(DataMessage _dataMessage)
  {
    this.dataMessage = _dataMessage;
  }

  private void processXML(XMLStreamReader xmlr)
    throws Exception
  {
    switch (xmlr.getEventType())
    {
    case 1:
      startParseElement(xmlr);
      processAttributes(xmlr);
      break;
    case 2:
      endParseElement(xmlr);
      break;
    case 6:
    case 12:
      this.tagValue = xmlr.getText();
    case 4:
      int start = xmlr.getTextStart();
      int length = xmlr.getTextLength();
      this.tagValue = new String(xmlr.getTextCharacters(), start, length);
    case 3:
    case 5:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    }
  }

  private void startParseElement(XMLStreamReader xmlr)
  {
    if (!xmlr.hasName())
      return;
    String localTagName = xmlr.getLocalName();

    if ("DATASET".equalsIgnoreCase(localTagName)) {
      DataSet dataSet = new DataSet();

      this.modelsStack.push(dataSet);
    }
    if ("METADATA".equalsIgnoreCase(localTagName))
    {
      this.parentTagsStack.push("METADATA");
    }
    if ("ENTITY".equalsIgnoreCase(localTagName))
    {
      this.parentTagsStack.push("ENTITY");

      this.modelsStack.push(new Entity());
    }
    this.tagsStack.push(localTagName);
  }

  private void endParseElement(XMLStreamReader xmlr)
  {
    String localTagName = (String)this.tagsStack.peek();
    String strParentTagName = null;

    if (("webServiceName".equalsIgnoreCase(localTagName)) && (this.tagValue != null)) {
      this.dataMessage.webServiceName = this.tagValue;
    }
    else if (("type".equalsIgnoreCase(localTagName)) && (this.tagValue != null))
    {
      this.dataMessage.type = this.tagValue;
    } else if (("messageType".equalsIgnoreCase(localTagName)) && (this.tagValue != null))
    {
      this.dataMessage.messageType = this.tagValue;
    } else if (("message".equalsIgnoreCase(localTagName)) && (this.tagValue != null))
    {
      this.dataMessage.message = this.tagValue;
    } else if (("version".equalsIgnoreCase(localTagName)) && (this.tagValue != null))
    {
      this.dataMessage.version = this.tagValue;
    }

    if ("PARAMETER".equalsIgnoreCase(localTagName)) {
      if (this.tagValue == null)
        this.tagValue = "";
      this.dataMessage.setParameter(this.tagEntityPropName, this.tagValue);
      this.tagEntityPropName = null;
    }
    if ("METADATA".equalsIgnoreCase(localTagName)) {
      this.parentTagsStack.pop();
    }
    try
    {
      strParentTagName = (String)this.parentTagsStack.peek();
    }
    catch (Exception ex) {
    }
    if (("ENTITY".equalsIgnoreCase(strParentTagName)) && ("PROPERTY".equalsIgnoreCase(localTagName)))
    {
      Entity entity = (Entity)this.modelsStack.peek();

      if (this.tagValue != null)
      {
        if ("editStatus".equalsIgnoreCase(this.tagEntityPropName)) entity.setEditState(this.tagValue);
        entity.setPropertyValue(this.tagEntityPropName, this.tagValue);
      }
      this.tagEntityPropName = null;
    }

    if ("ENTITY".equalsIgnoreCase(localTagName)) {
      Entity entity = (Entity)this.modelsStack.peek();

      this.parentTagsStack.pop();
      this.modelsStack.pop();
      DataSet dataSet = (DataSet)this.modelsStack.peek();
      dataSet.addEntity(entity);
    }

    if ("DATASET".equalsIgnoreCase(localTagName)) {
      DataSet dataSet = (DataSet)this.modelsStack.peek();
      this.dataMessage.addDataSet(dataSet);
      this.modelsStack.pop();
    }
    this.tagValue = null;
    this.tagsStack.pop();
  }

  private void processAttributes(XMLStreamReader xmlr) {
    String localTagName = xmlr.getLocalName();

    String strParentTagName = null;
    try {
      strParentTagName = (String)this.parentTagsStack.peek();
    }
    catch (Exception e)
    {
    }
    if (("METADATA".equalsIgnoreCase(strParentTagName)) && ("PROPERTY".equalsIgnoreCase(localTagName)))
    {
      DataSet dataSet = (DataSet)this.modelsStack.peek();
      HashMap hashMetadata = new HashMap();
      String strMetadataName = null;

      for (int i = 0; i < xmlr.getAttributeCount(); i++) {
        String localAttrName = xmlr.getAttributeName(i).toString();
        String localAttrValue = xmlr.getAttributeValue(i);
        if ("name".equalsIgnoreCase(localAttrName)) {
          strMetadataName = localAttrValue;
        }
        hashMetadata.put(localAttrName, localAttrValue);
      }
      if (strMetadataName == null)
        strMetadataName = dataSet.getMetadataCount() + 1 + "";
      dataSet.addMetadata(strMetadataName, hashMetadata);
    }
    else if ("PARAMETER".equalsIgnoreCase(localTagName)) {
      String localAttrName = xmlr.getAttributeName(0).toString();
      String localAttrValue = xmlr.getAttributeValue(0);
      if ("name".equalsIgnoreCase(localAttrName))
        this.tagEntityPropName = localAttrValue;
    }
    else
    {
      for (int i = 0; i < xmlr.getAttributeCount(); i++)
        processAttribute(xmlr, i);
    }
  }

  private void processAttribute(XMLStreamReader xmlr, int index)
  {
    String localAttrName = xmlr.getAttributeName(index).toString();
    String localAttrValue = xmlr.getAttributeValue(index);
    String localTagName = (String)this.tagsStack.peek();
    String strParentTagName = null;
    try {
      strParentTagName = (String)this.parentTagsStack.peek();
    }
    catch (Exception e)
    {
    }

    if (("ENTITY".equalsIgnoreCase(strParentTagName)) && ("PROPERTY".equalsIgnoreCase(localTagName)))
    {
      if ("name".equalsIgnoreCase(localAttrName)) {
        this.tagEntityPropName = localAttrValue;
      }
    }
    if (("DATASET".equalsIgnoreCase(localTagName)) && 
      ("name".equalsIgnoreCase(localAttrName)))
      ((DataSet)this.modelsStack.peek()).dataSetName = localAttrValue;
  }

  private DataMessage parserXML(InputStream in)
  {
    try
    {
      System.setProperty("javax.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");

      XMLInputFactory xmlif = XMLInputFactory.newInstance();

      XMLStreamReader xmlr = xmlif.createXMLStreamReader(in, "utf-8");

      while (xmlr.hasNext()) {
        processXML(xmlr);
        xmlr.next();
      }
      xmlr.close();
      this.parentTagsStack = null;
      this.tagsStack = null;
      this.modelsStack = null;
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

  public static DataMessage getDataMessage(InputStream in, DataMessage _dataMessage) {
    Xml2DataMessageUtilty dataMessageUtils = new Xml2DataMessageUtilty(_dataMessage);
    DataMessage dataMessage = dataMessageUtils.parserXML(in);
    return dataMessage;
  }

  public static DataMessage getDataMessage(InputStream in) {
    return getDataMessage(in, new DataMessage());
  }
  public static String CDATA(String str) {
    return "<![CDATA[" + str + "]]>";
  }
}
