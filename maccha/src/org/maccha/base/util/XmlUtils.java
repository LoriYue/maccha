package org.maccha.base.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>Title:XmlUtils.java </p>
 * <p>Description: xml操作通用工具</p>
 * <p>Copyright:  Copyright (c) 2001<br></p>
 * <p>Company: 北京东方信软信息技术有限公司<br></p>
 * @author
 * @version 1.0
 */
public class XmlUtils {
	public static Logger logger = Logger.getLogger(XmlUtils.class.getName());

	/**
	 * 
	 * @param strXml
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static Object parseXml2Obj(String strXml, String xmlEntityRef, Class cls) throws Exception {
		BeanReader reader = new BeanReader();
		try {
			StringReader stringReader = new StringReader(strXml);
			reader.registerBeanClass(xmlEntityRef, cls);
			reader.getBindingConfiguration().setMapIDs(false);
			return reader.parse(stringReader);
		} catch (Exception e) {
			throw e;
		} finally {
			reader.clear();
		}
	}
	/**
	 * 通过指定的XML文件路径,将该XML文件解析成对象,并返回.
	 * @param xmlPath
	 * @return Object
	 * @throws Exception
	 */	
	public static Object parseXmlFile2Obj(String xmlPath) throws Exception {
		FileInputStream input = null ;
		Object obj = null ;
		try {
			input = new FileInputStream(xmlPath) ;
			obj = parseXml2Obj(input);
		} catch (Exception ex) {
			throw ex ;
		}finally{
			if(input != null)input.close();
		}
		return obj;
	}
	/**
	 * 通过指定的XML字符串,将该XML字符串解析成对象,并返回.
	 * @param xmlPath
	 * @return Object
	 * @throws Exception
	 */	
	public static Object parseXml2Obj(String xmlStr) throws Exception {
		ByteArrayInputStream byteInput = null ;
		Object obj = null ;
		try {
			byteInput = new ByteArrayInputStream(xmlStr.getBytes());
			obj = parseXml2Obj(byteInput);
		} catch (Exception ex) {
			throw ex ;
		}finally{
			if(byteInput != null)byteInput.close();
		}
		return obj;
	}
	private static Object parseXml2Obj(InputStream inputStream) throws Exception {
		XMLDecoder decoder = null ;
		Object obj = null ;
		try {
			decoder = new XMLDecoder(inputStream);
			obj = decoder.readObject();
		} catch (Exception ex) {
			throw ex ;
		}finally{
			if(decoder != null)decoder.close() ;
		}
		return obj;
	}
    /**
     * 将对象系列化成对象字符串
     * @return
     */
    public static String parseObj2Xml(Object object)throws Exception {
    	XMLEncoder encoder = null ; 
    	String str = null ;
    	BufferedOutputStream bufOut = null ;
    	ByteArrayOutputStream out =null ;
    	try{
	        out = new ByteArrayOutputStream();
	        bufOut = new BufferedOutputStream(out);
	        encoder = new XMLEncoder(bufOut);
	        encoder.writeObject(object);
	        encoder.close();
	        str = new String(out.toByteArray()) ;	        
    	}catch(Exception ex){
    		throw ex ;
    	}finally{
    		if(out != null)out.close();
    		if(bufOut != null)bufOut.close() ;
		}        
        return str;
    }
    
    /**
     * 处理XML文件中的特殊字符，如果字符串中保护特殊字符，则自动添加上<![CDATA[ ]]>标记
     * @param strValue
     * @return
     */
    public static String parseXMLCData(String strValue) {
        if(strValue == null) return strValue ;
        //System.out.println("----" + strValue);
        if (strValue.indexOf('<') > -1 || strValue.indexOf('>') > -1
            || strValue.indexOf('\n') > -1 || strValue.indexOf('&') > -1
            || strValue.indexOf('\'') > -1 || strValue.indexOf('"') > -1) {
            StringBuffer sb = new StringBuffer();

            if (strValue.indexOf("]]>") > -1) {
                sb.append(parseEndXMLCData(strValue));
            } else {
                sb.append("<![CDATA[");
                sb.append(strValue);
                sb.append("]]>");
            }
            return sb.toString();
        } else {
            return strValue;
        }
    }
    private static String parseEndXMLCData(String strValue) {
        int pos = strValue.indexOf("]]>");
        StringBuffer sb = new StringBuffer();
        if (pos > -1) {
            sb.append("<![CDATA[");
            sb.append(strValue.substring(0, pos + 1));
            sb.append("]]>");
            sb.append(parseEndXMLCData(strValue.substring(pos + 1)));
        } else {
            sb.append("<![CDATA[");
            sb.append(strValue);
            sb.append("]]>");
        }
        return sb.toString();
    }    
    
    /**
     * Converts an XML file to an XSL-FO file using JAXP (XSLT).
     * @param xml the XML file
     * @param xslt the stylesheet file
     * @param fo the target XSL-FO file
     * @throws IOException In case of an I/O problem
     * @throws TransformerException In case of a XSL transformation problem
     */
    public static void convertXML2FO(InputStream xml, File xslt, File fo){
        //Setup output
        OutputStream out = null;
        try {
            out = new java.io.FileOutputStream(fo);
            //Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));

            //Setup input for XSLT transformation
            Source src = new StreamSource(xml);
            //Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new StreamResult(out);
            //Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        }catch(Exception ex){
            ex.printStackTrace();
        } finally {
            try{
                out.close();
            }catch(Exception e){
            }
        }
    }
    
    /**
     * 将xml通过XSL转换为HTML字符串
     * @param strXml String XML内容字符串
     * @param xslfile String xsl文件路径
     * @param nodename String 要翻译的xml节点名称
     * @return String 返回HTML字符串
     */
    public static String transXML2HTML(String strXml, String xslfile) {
        StringWriter sw = null;
        if (strXml == null || strXml.trim().length() == 0)
            return "<H1>无该满足条件的记录</H1>";
        try {
            String strOut = new String(strXml.getBytes("GBK"), "ISO8859-1");
            StringBufferInputStream sbis = new StringBufferInputStream(strOut);

            StreamSource sXML = new StreamSource();
            sXML.setInputStream(sbis);
            File file = new File(xslfile);
            StreamSource sSTL = new StreamSource(file);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(sSTL);

            sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.setOutputProperty("method", "html");
            transformer.transform(sXML, sr);
        } catch (Exception e) {
            System.out.println("TransXML2HTML   error:" + e);
        }
        return sw.getBuffer().toString();
    }

    /**
     * ??ê??ˉHTML±ê??￡???±ê???¤′|àí?a??ó|FOP±ê??
     * @param str String HTML×?·?′?
     * @return String ?¤′|àíoóμ?FOP×?·?′?
     */

    public static String formatHtml2Fop(String str) {
        String originPairTags[] = new String[] { "U", "SUB", "SUP"};
        String destinPairStartTags[] = new String[] {
            "&lt;fo:inline text-decoration=\"underline\"&gt;",
            "&lt;fo:inline text-decoration=\"sub\"&gt;",
            "&lt;fo:inline text-decoration=\"super\" font-size=\"75%\"&gt;"};
        String destinPairEndTags[] = new String[] {
            "&lt;/fo:inline&gt;",
            "&lt;/fo:inline&gt;",
            "&lt;/fo:inline&gt;"};
        String originSingleTags[] = new String[] {"BR","P"};
        String destinSingleTags[] = new String[] {"&lt;fo:block/&gt;","&lt;fo:block/&gt;"};
        for (int i = 0; i < originPairTags.length; i++) {
            str = pairTagFormat(str, originPairTags[i],destinPairStartTags[i], destinPairEndTags[i]);
        }
        for (int i = 0; i < originSingleTags.length; i++) {
            str = singleTagFormat(str, originSingleTags[i],destinSingleTags[i]);
        }
        str = str.replaceAll("&nbsp;", " ");
        str = str.replaceAll("&amp;nbsp;", " ");
        System.out.println("----------" + str);
       return str;
    }

    private static String singleTagFormat(String originStr, String originTag, String destinTag) {
        originTag = originTag.toUpperCase();
        String originLowTag = originTag.toLowerCase();
        String reg = "((&lt;)|<)\\s*(\\/)?\\s*((" + originTag + ")|(" + originLowTag +
            "))\\s*((\\\\)|(\\/))?\\s*(>|(&gt;))";
        String temp = originStr.replaceAll(reg, destinTag);
        return temp;
    }

    private static String pairTagFormat(String originStr, String originTag, String destinStartTag, String destinEndTag) {
        originTag = originTag.toUpperCase();
        String originLowTag = originTag.toLowerCase();
        String reg = "((&lt;)|<)\\s*((" + originTag + ")|(" + originLowTag +
            "))\\s*(>|(&gt;))";
        String temp = originStr.replaceAll(reg, destinStartTag);
        reg = "((&lt;)|<)\\s*(\\/)\\s*((" + originTag + ")|(" + originLowTag +
            "))\\s*(>|(&gt;))";
        temp = temp.replaceAll(reg, destinEndTag);
        return temp;
    }

    /**
     * Get Document from string of xml
     * @param xmlstr xml字符串
     * @return  Document
     */
    public static Document parseXmlString(String _xmlStr) throws Exception{
    	 return parseXmlString(_xmlStr,null);
    }
    
	/**
	 * Get Document from string of xml and verify by dtd
	 * @param _xmlStr xml字符串
	 * @param _dtd    dtd验证
	 * @return
	 * @throws SysException
	 */
    public static Document parseXmlString(String _xmlStr, String _dtd) throws Exception{
    	if(_dtd!=null)
	    	_xmlStr = "<?xml version='1.0' encoding='UTF-8'?>" + _dtd + _xmlStr;
        InputStream _inpuStre = new ByteArrayInputStream(_xmlStr.getBytes());
        Document _docu = parseInputStream(_inpuStre);
        return _docu;
    }
    
	/**
	 * 解析xml输入流
	 * @param _inpuStre
	 * @return
	 */
    public static Document parseInputStream(InputStream _inpuStre) throws Exception{
        Document _docu = null;	
	    try{
	        System.setProperty("javax.xml.parsers.DocumentBuilderFactory","org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
	        DocumentBuilderFactory _docuBuilFact = DocumentBuilderFactory.newInstance();
	        DocumentBuilder _docuBuil = _docuBuilFact.newDocumentBuilder();
	        _docu=_docuBuil.parse(_inpuStre);
	    } finally {
	       if(_inpuStre!=null) {
	     	   try{
	     		  _inpuStre.close();
	     	   } catch(Throwable t) {
	     		  logger.warn(t);
	     	   }
	        }
	     }
	     return _docu;
    }
    /**
     * 解析xml URL资源
     * @param _url
     * @return
     */
    public static Document parseURL(URL _url)throws Exception{
    	InputStream _inpuStre=null;
    	Document _docu = null;
    	try{
    	   _inpuStre=_url.openStream();
    	   _docu=parseInputStream(_inpuStre);
	    } finally {
	       if(_inpuStre!=null) {
	     	   try{
	     		  _inpuStre.close();
	     	   } catch(Throwable t) {
	     		  logger.warn(t);
	     	   }
	        }
	     }
	     return _docu;
    }
  
	/**
	 * 解析xml文件
	 * @param _xmlFile
	 * @return
	 */
    public static Document parseXmlFile(String _xmlFile) throws Exception{
    	InputStream _inpuStre=null;
    	Document _docu = null;
    	try{
    	   _inpuStre=new FileInputStream(_xmlFile);
    	   _docu=parseInputStream(_inpuStre);
	    } finally {
	       if(_inpuStre!=null) {
	     	   try {
	     		  _inpuStre.close();
	     	   } catch(Throwable t) {
	     		  logger.warn(t);
	     	   }
	        }
	     }
	     return _docu;
    }
    
    /**
     * 打印节点
     * @param _node
     */
    public static void printlnXml(Node _node) {
        try {
            OutputFormat format = new OutputFormat();
            format.setEncoding("GB2312");
            XMLSerializer xs = new XMLSerializer(System.out, format);
            if (_node instanceof Document) {
                xs.serialize( (Document) _node);
            } else {
                xs.serialize( (Element) _node);
            }
        } catch (Throwable t) {
        	logger.warn(t);
        }
    }
    
    public static String getXmlSting(Node _node) throws Exception {
    	return getXmlSting(_node,"GB2312");
    }
    /**
     * 获得节点xml串
     * @param _node
     * @param _enco
     */
    public static String getXmlSting(Node _node,String _enco)throws Exception {
        OutputFormat _outpFormat = new OutputFormat();
        _outpFormat.setEncoding(_enco);
        ByteArrayOutputStream _byteOutpStre = new ByteArrayOutputStream();
        XMLSerializer _xmlSeri = new XMLSerializer(_byteOutpStre, _outpFormat);
        if (_node instanceof Document) {
        	_xmlSeri.serialize((Document)_node);
        } else {
        	_xmlSeri.serialize((Element)_node);
        }
        return new String(_byteOutpStre.toByteArray());
    }
    
    /**
     *   是否转化过
     *   @return
     */
    public static boolean checkEncoding(String str, String encoding) throws
        Exception {
        String strRE = "";
        try {
            strRE = new String(str.getBytes(), encoding);
        } catch (Exception ex) {
            throw new Exception("测试转化失败！");
        }
        if (strRE.equals(str)) {
            //非汉字
            return true;
        } else {
            return false;
        }
    }

    /**
     * 编码对解析的xml字符串进行编码解决中文问题
     * @param xmlstr
     * @return  String
     */
    public static String EncodingXmlString(String xmlstr) {
        String os = System.getProperties().getProperty("os.name");
        if (!"Linux".equals(os)) {
            return xmlstr;
        }
        Document node = null;
        try {
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                               "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlparse = factory.newDocumentBuilder();
            InputStream in = new ByteArrayInputStream(xmlstr.getBytes());
            InputSource ins = new InputSource(in);
            node = xmlparse.parse(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        try {
            OutputFormat format = new OutputFormat();
            format.setEncoding("GB2312");
            XMLSerializer xs = new XMLSerializer(byteout, format);
            if (node instanceof Document) {
                xs.serialize( (Document) node);
            } else {
                xs.serialize( (Element) node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteout.toString();
    }

    /**
     * 在xql路径指定的节点新增节点
     * @param startNode 开始节点
     * @param name      节点名字
     * @param xql       xql路径
     * @return          (新增的节点)Node
     * @throws Exception
     */
    public static Node appendNode(Node startNode, String name, String xql) throws Exception {
        Node targetNode = XPathAPI.selectSingleNode(startNode, xql);
        Document doc = targetNode.getOwnerDocument();
        Element newElement = doc.createElement(name);
        targetNode.appendChild( (Node) newElement);
        return ( (Node) newElement);
    }
    /**
     *设置由xql路径指定的节点的Text值
     * @param startnode  开始节点
     * @param value       Text值
     * @param xql        xql路径
     * @return           返回 Node
     * @throws Exception
     */
    public static Node setValue(Node startNode, String value, String xql) throws Exception {
        Node targetNode = XPathAPI.selectSingleNode(startNode, xql);
        NodeList children = targetNode.getChildNodes();
        int index = 0;
        int length = children.getLength();
        // Remove all of the current contents
        for (index = 0; index < length; index++) {
            targetNode.removeChild(children.item(index));
        }
        // Add in the new value
        Document doc = targetNode.getOwnerDocument();
        Text text = doc.createTextNode(value);
        targetNode.appendChild(text);
        return targetNode;
    }

    /**
     * 获得指定节点的text值
     * @param node 处理节点
     * @return     String
     */
    public static String getValue(Node node) {
        NodeList childNodes;
        StringBuffer contents = new StringBuffer();
        childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                contents.append(childNodes.item(i).getNodeValue());
            }
        }
        return contents.toString();

    }

    /**
     * 获得由xql指定的节点的text值
     * @param node  开始处理节点
     * @param xql   xql路径
     * @return      String
     * @throws Exception
     */
    public static String getValue(Node node, String xql) throws Exception {
        if ( (xql == null) || (xql.length() == 0)) {
            throw new Exception("findValue called with empty xql statement");
        }
        if (node == null) {
            throw new Exception("findValue called with null node");
        }
        return getValue(XPathAPI.selectSingleNode(node, xql));
    }

    /**
     *设置由xql路径指定的节点的属性
     * @param startnode  开始节点
     * @param prop       属性值
     * @param xql        xql路径
     * @return           返回 Param
     * @throws Exception
     */
    public static Map setAttribute(Node startnode, Map map, String xql) throws
        Exception {
        Node node = XPathAPI.selectSingleNode(startnode, xql);
        if (! (node instanceof Element)) {
            throw new Exception(
                "The type of the node is not Element ,can not have attibute !!!!");
        }
        String[] keys = (String[]) map.keySet().toArray();
        for (int i = 0; keys != null && i < keys.length; i++) {
            String name = keys[i];
            if (name == null) {
                continue;
            }
            String value = (String) map.get(name);
            if (value == null) {
                value = "";
            }
            ( (Element) node).setAttribute(name, value);
        }

        Map attr = getAttribute(node);
        return attr;
    }

    /**
     * 获得由xql路径指定的节点的属性
     * @param startnode  开始节点
     * @param xql        xql路径
     * @return           Param
     * @throws Exception
     */
    public static Map getAttribute(Node startnode, String xql) throws Exception {
        Map attr = null;
        Node node = XPathAPI.selectSingleNode(startnode, xql);
        attr = getAttribute(node);
        return attr;
    }

    /**
     * 获得由xql路径指定的节点的属性
     * @param startnode  开始节点
     * @param xql        xql路径
     * @return           Map[]
     * @throws Exception
     */
    public static Map[] getAttributeList(Node startnode, String xql) throws Exception {
        NodeList nodelist = XPathAPI.selectNodeList(startnode, xql);
        int length = nodelist.getLength();
        Map[] attrs = new Map[length];
        for (int i = 0; i < length; i++) {
            attrs[i] = getAttribute(nodelist.item(i));
        }
        return attrs;
    }

    /**
     * 获得指定节点的属性
     * @param node  处理节点
     * @return  Map
     */
    public static Map getAttribute(Node _node) {
        Map _attrMap = new HashMap();
        if(_node==null)return _attrMap;
        NamedNodeMap _namedNodeMap = _node.getAttributes();
        for (int i = 0; _namedNodeMap != null && i < _namedNodeMap.getLength(); i++) {
            String _name = _namedNodeMap.item(i).getNodeName();
            String _value = _namedNodeMap.item(i).getNodeValue();
            if (_name == null){
                continue;
            }
            if (_value == null){
            	_value = "";
            }
            _attrMap.put(_name, _value);
        }
        return _attrMap;
    }
    
    /**
     * 获得指定xql路径的节点数组
     * @param _node 处理节点
     * @param _xql  路径
     * @throws Exception
     */
    public static NodeList getNodeList(Node _node, String _xql) throws Exception{
        NodeList _nodeList = XPathAPI.selectNodeList(_node, _xql);
        return _nodeList;
    }
}

/**
 * <p>Title: ErrorChecker.java</p>
 * <p>Description: dtd验证出来类</p>
 * <p>Copyright:  Copyright (c) 2001<br></p>
 * @author 杨耀峰
 * @version 1.0
 */
class ErrorChecker
    extends DefaultHandler {
    public ErrorChecker() {
    	
    }

    /**
     * 一般出错时处理动作
     * @param e
     */
    public void error(SAXParseException e) {
        //System.out.println("Parsing error:  "+e.getMessage());
        //System.out.println("Cannot continue.");
        //System.exit(1);
    }

    /**
     * 警告时处理动作
     * @param e
     */
    public void warning(SAXParseException e) {
        //System.out.println("Parsing problem:  "+e.getMessage());
    }

    /**
     * 致命出错时处理动作
     * @param e
     */
    public void fatalError(SAXParseException e) {
        //System.out.println("Parsing error:  "+e.getMessage());
        //System.out.println("Cannot continue.");
        //System.exit(1);
    }
}
