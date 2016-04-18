package org.maccha.httpservice.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.controller.ControllerDispatcher;
import org.maccha.httpservice.util.JsonDataMessageParserImpl;
import org.maccha.httpservice.util.XmlDataMessageParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMessageServiceServlet extends HttpServlet
{
  public static final int BUFFERSIZE = 2048;
  public static final String ENCODING = "UTF-8";
  public static final String LOGIN_ACTION = "100000";
  public static final String JSON_FORMAT = "json";
  public static final String XML_FORMAT = "xml";
  public static final String FORMAT = "_format";
  public static final String WEBSERVICENAME = "_webserviceName";
  public static final String VERSION = "_version";
  private static final Logger logger = LoggerFactory.getLogger(DataMessageServiceServlet.class);

  public void doGet(HttpServletRequest request, HttpServletResponse response)
  {
    InputStream _inputStream = null;
    OutputStream _outputStream = null;
    try {
      response.addHeader("Access-Control-Allow-Origin", "*");

      request.setCharacterEncoding("UTF-8");

      int requestDataSize = request.getContentLength();

      _inputStream = request.getInputStream();

      _outputStream = response.getOutputStream();

      String _hostName = request.getRemoteHost();
      String _hostAddr = request.getRemoteAddr();

      String _format = request.getParameter("_format");
      String _webserviceName = request.getParameter("_webserviceName");
      String _version = request.getParameter("_version");

      logger.info("hostName:" + _hostName + ",requestURI:" + request.getRequestURI() + ",sessionId:" + request.getSession().getId());
      byte[] _requestBytes = null;
      DataMessage _requestDataMessage = null;

      if (requestDataSize <= 0) {
        _requestDataMessage = DataMessage.getRequestDataMessage(_webserviceName);
      }
      else {
        _requestBytes = dataDecompress(_inputStream);

        logger.info(_hostName + "的请求开始处理.......");

        if ((StringUtils.isNotNull(_format)) && ("xml".equalsIgnoreCase(_format)))
          _requestDataMessage = new XmlDataMessageParserImpl().parseRequest(new ByteArrayInputStream(_requestBytes));
        else {
          _requestDataMessage = new JsonDataMessageParserImpl().parseRequest(new ByteArrayInputStream(_requestBytes));
        }
      }

      String strRequestQueryString = request.getQueryString();
      if ((StringUtils.isNotNull(strRequestQueryString)) && (strRequestQueryString.indexOf("&") != -1)) {
        Map mapParam = parseQuery(strRequestQueryString, true);

        Iterator itrKey = mapParam.keySet().iterator();
        while (itrKey.hasNext()) {
          String _strKey = (String)itrKey.next();
          String _strValue = (String)mapParam.get(_strKey);
          _requestDataMessage.setParameter(_strKey, _strValue);
        }

      }

      _requestDataMessage.setParameter("WEBCONTEXT_PATH", request.getRealPath("/"));
      _requestDataMessage.setParameter("WEBCONTEXT_REQUEST_HOST_NAME", _hostName);
      _requestDataMessage.setParameter("WEBCONTEXT_REQUEST_HOST_IP", _hostAddr);
      _requestDataMessage.setParameter("WEBCONTEXT_REQUEST_SESSION_ID", request.getSession().getId());
      logger.info("请求报文：" + _requestDataMessage.toJson());

      DataMessage _responseDataMessage = ControllerDispatcher.dispatch(_webserviceName, _requestDataMessage);
      _responseDataMessage.webServiceName = _requestDataMessage.webServiceName;

      byte[] _responseBytes = null;
      if ((StringUtils.isNotNull(_format)) && ("xml".equalsIgnoreCase(_format)))
        _responseBytes = _responseDataMessage.toXml().getBytes("UTF-8");
      else {
        _responseBytes = _responseDataMessage.toJson().getBytes("UTF-8");
      }
      bufferedWriteUncompressed(_outputStream, _responseBytes);

      logger.info(_hostName + "的请求处理结束！");

      _requestBytes = null;
      _responseBytes = null;
      _requestDataMessage = null;
      _responseDataMessage = null;
    } catch (Exception e) {
      logger.error("处理请求时发生错误", e);
    }
    finally {
      clostStream(_inputStream, _outputStream);
    }
  }

  public static void copyParamsToAttributes(DataMessage _requestDataMessage, HttpServletRequest servletRequest, boolean trimParams, boolean treatEmptyParamsAsNull, boolean ignoreEmptyRequestParams)
  {
    Enumeration paramNames = servletRequest.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = (String)paramNames.nextElement();
      if (_requestDataMessage.getString(paramName) != null) {
        continue;
      }
      String[] paramValues = servletRequest.getParameterValues(paramName);
      paramValues = prepareParameters(paramValues, trimParams, treatEmptyParamsAsNull, ignoreEmptyRequestParams);
      if (paramValues == null) {
        continue;
      }
      _requestDataMessage.setParameter(paramName, paramValues.length == 1 ? paramValues[0] : paramValues);
    }
  }

  public static String[] prepareParameters(String[] paramValues, boolean trimParams, boolean treatEmptyParamsAsNull, boolean ignoreEmptyRequestParams)
  {
    if ((trimParams) || (treatEmptyParamsAsNull) || (ignoreEmptyRequestParams)) {
      int emptyCount = 0;
      int total = paramValues.length;
      for (int i = 0; i < paramValues.length; i++) {
        String paramValue = paramValues[i];
        if (paramValue == null) {
          emptyCount++;
        }
        else {
          if (trimParams) {
            paramValue = paramValue.trim();
          }
          if (paramValue.length() == 0) {
            emptyCount++;
            if (treatEmptyParamsAsNull) {
              paramValue = null;
            }
          }
          paramValues[i] = paramValue;
        }
      }
      if ((ignoreEmptyRequestParams == true) && (emptyCount == total)) {
        return null;
      }
    }
    return paramValues;
  }

  public static Map<String, String> parseQuery(String query, boolean decode)
  {
    Map queryMap = new HashMap();

    int ndx2 = 0;
    while (true) {
      int ndx = query.indexOf('=', ndx2);
      if (ndx == -1) {
        if (ndx2 >= query.length()) break;
        queryMap.put(query.substring(ndx2), null); break;
      }

      String name = query.substring(ndx2, ndx);
      if (decode) {
        try {
          name = URLDecoder.decode(name, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

      ndx2 = ndx + 1;

      ndx = query.indexOf('&', ndx2);

      if (ndx == -1) {
        ndx = query.length();
      }

      String value = query.substring(ndx2, ndx);

      if (decode) {
        try {
          value = URLDecoder.decode(value, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
      }

      queryMap.put(name, value);

      ndx2 = ndx + 1;
    }

    return queryMap;
  }
  public byte[] dataDecompress(InputStream _inputStream) throws IOException {
    int _count = 0;
    byte[] _b = new byte[4096];
    ByteArrayOutputStream _byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      while ((_count = _inputStream.read(_b, 0, 4096)) != -1) {
        _byteArrayOutputStream.write(_b, 0, _count);
      }
      byte[] arrayOfByte1 = _byteArrayOutputStream.toByteArray();
      return arrayOfByte1;
    }
    catch (IOException ex)
    {
      throw ex;
    } finally {
      _byteArrayOutputStream.close(); } 
  }

  private void bufferedWriteUncompressed(OutputStream _outputStream, byte[] _data)
    throws IOException
  {
    int _position = 0;

    while (_position < _data.length) {
      int _size = Math.min(2048, _data.length - _position);
      _outputStream.write(_data, _position, _size);
      _position += _size;
    }
    _outputStream.flush();
  }

  private void clostStream(InputStream _inputStream, OutputStream _outputStream)
  {
    if (_outputStream != null)
      try {
        _outputStream.close();
      }
      catch (Exception e)
      {
      }
    if (_inputStream != null)
      try {
        _inputStream.close();
      }
      catch (Exception e) {
      }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

  public void destroy()
  {
  }
}
