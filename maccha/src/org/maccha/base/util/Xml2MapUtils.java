package org.maccha.base.util;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.apache.log4j.Logger;
import org.maccha.base.exception.SysException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class Xml2MapUtils
{
  private static Logger log = Logger.getLogger(Xml2MapUtils.class);

  private Stack<String> parentPathTagsStack = new Stack();
  private Stack<String> tagsStack = new Stack();
  private Map<String, String> xmlMap;
  private String tagValue = null;

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
    if (!xmlr.hasName()) return;
    String localTagName = xmlr.getLocalName();
    if (this.parentPathTagsStack.isEmpty())
      this.parentPathTagsStack.push("/" + localTagName);
    else
      this.parentPathTagsStack.push((String)this.parentPathTagsStack.peek() + "/" + localTagName);
  }

  private void endParseElement(XMLStreamReader xmlr)
  {
    String localTagName = xmlr.getLocalName();
    String tagEntityPropName = null;
    if (!this.tagsStack.isEmpty()) tagEntityPropName = (String)this.tagsStack.peek();
    log.debug("tagEntityPropName=" + tagEntityPropName + ",localTagName=" + localTagName + ",tagValue =" + this.tagValue + "::");
    if ((StringUtils.isNotNull(tagEntityPropName)) && (StringUtils.isNotNull(this.tagValue.replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "")))) {
      this.xmlMap.put(tagEntityPropName, this.tagValue);
    }
    this.tagValue = null;
    this.parentPathTagsStack.pop();
    if (!this.tagsStack.isEmpty()) this.tagsStack.pop();
  }

  private void processAttributes(XMLStreamReader xmlr)
  {
    String strParentTagName = (String)this.parentPathTagsStack.peek();
    String _name = null;
    String _value = null;
    for (int i = 0; i < xmlr.getAttributeCount(); i++) {
      String localAttrName = xmlr.getAttributeName(i).toString();
      String localAttrValue = xmlr.getAttributeValue(i);

      if ("name".equalsIgnoreCase(localAttrName))
        _name = localAttrValue;
      else if ("value".equalsIgnoreCase(localAttrName)) {
        _value = localAttrValue;
      }
    }
    if (StringUtils.isNotNull(_name)) {
      if (StringUtils.isNotNull(_value)) this.xmlMap.put(strParentTagName + "[@name=" + _name + "]", _value);
      this.tagsStack.push(strParentTagName + "/" + _name);
    } else {
      this.tagsStack.push("");
    }
  }

  private Map<String, String> parserXML(InputStream in, Map _xmlMap)
  {
    if (_xmlMap == null) this.xmlMap = new HashMap(); else {
      this.xmlMap = _xmlMap;
    }
    XMLStreamReader xmlr = null;
    try
    {
      System.setProperty("javax.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");

      XMLInputFactory xmlif = XMLInputFactory.newInstance();

      xmlr = xmlif.createXMLStreamReader(in, "utf-8");

      while (xmlr.hasNext()) {
        processXML(xmlr);
        xmlr.next();
      }
      this.parentPathTagsStack = null;
      this.tagsStack = null;
    } catch (Throwable t) {
      SysException.handleMessageException(t.getMessage(), t);
    }
    finally {
      try {
        if (xmlr != null) xmlr.close();
        if (in != null) in.close(); 
      }
      catch (Exception ex) {
      }
    }
    return this.xmlMap;
  }
  public static void parserXML2Map(InputStream in, Map _xmlMap) {
    Xml2MapUtils xml2MapUtils = new Xml2MapUtils();
    xml2MapUtils.parserXML(in, _xmlMap);
  }
  public static Map<String, String> parserXML2Map(InputStream in) {
    Xml2MapUtils xml2MapUtils = new Xml2MapUtils();
    return xml2MapUtils.parserXML(in, null);
  }
}
