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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.IWebServiceAction;
import org.maccha.httpservice.controller.ControllerDispatcher;
import org.maccha.httpservice.util.JsonDataMessageParserImpl;
import org.maccha.httpservice.util.XmlDataMessageParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataMessageServiceServlet extends HttpServlet {
	public static final int BUFFERSIZE = 2048;
	public static final String ENCODING = "UTF-8";
	public static final String LOGIN_ACTION = "100000";
	public static final String JSON_FORMAT = "json" ;
	public static final String XML_FORMAT = "xml" ;
	//请求、相应报文格式，xml，json
	public static final String FORMAT = "_format" ;
	//请求服务名
	public static final String WEBSERVICENAME = "_webserviceName" ;
	//请求服务版本号
	public static final String VERSION = "_version" ;
	private final static Logger logger = LoggerFactory.getLogger(DataMessageServiceServlet.class);
	/**
	 * Process the HTTP Get request
	 * 
	 * @param request HttpServletRequest请求对象
	 * @param response HttpServletRequest响应对象
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		InputStream _inputStream = null;
		OutputStream _outputStream = null;
		try {
			response.addHeader("Access-Control-Allow-Origin","*");
			request.setCharacterEncoding("UTF-8");
			// 请求内容长度
			int requestDataSize = request.getContentLength();
			// 请求输入流
			_inputStream = request.getInputStream();
			// 请求输出流
			_outputStream = response.getOutputStream();
			// 取请求机主机
			String _hostName = request.getRemoteHost();
			String _hostAddr = request.getRemoteAddr();
			String _format = request.getParameter(FORMAT);
			String _webserviceName = request.getParameter(WEBSERVICENAME);
			String _version = request.getParameter(VERSION);
			logger.info("hostName:" + _hostName + ",requestURI:"+ request.getRequestURI() + ",sessionId:"+ request.getSession().getId());
			byte[] _requestBytes = null ;
			DataMessage _requestDataMessage = null ;
			// 对于不符合协议的请求，返回找不到指定页
			if (requestDataSize <= 0) {
				_requestDataMessage = DataMessage.getRequestDataMessage(_webserviceName);
			}else{
				//将请求数据转换成字节数组
				_requestBytes = dataDecompress(_inputStream);
				// 初始化Spring对象
				logger.info(_hostName + "的请求开始处理.......");
				//解析请求报文,默认json
				if(StringUtils.isNotNull(_format)&&XML_FORMAT.equalsIgnoreCase(_format)){
					_requestDataMessage = new XmlDataMessageParserImpl().parseRequest(new ByteArrayInputStream(_requestBytes));
				}else{
					_requestDataMessage = new JsonDataMessageParserImpl().parseRequest(new ByteArrayInputStream(_requestBytes));
				}
			}
			//将URL中的参数初始化到报文里面，主要针对get请求
			String strRequestQueryString = request.getQueryString();
			if(StringUtils.isNotNull(strRequestQueryString) && strRequestQueryString.indexOf("&") !=-1){
				Map<String,String> mapParam = parseQuery(strRequestQueryString,true);
				Iterator<String> itrKey = mapParam.keySet().iterator();
				while(itrKey.hasNext()){
					String _strKey = itrKey.next();
					String _strValue = mapParam.get(_strKey);
					_requestDataMessage.setParameter(_strKey, _strValue);
				}
			}
			//处理post提交参数
			_requestDataMessage.setParameter(IWebServiceAction.WEBCONTEXT_PATH,request.getRealPath("/"));
			_requestDataMessage.setParameter(IWebServiceAction.WEBCONTEXT_REQUEST_HOST_NAME,_hostName);
			_requestDataMessage.setParameter(IWebServiceAction.WEBCONTEXT_REQUEST_HOST_IP,_hostAddr);
			_requestDataMessage.setParameter(IWebServiceAction.WEBCONTEXT_REQUEST_SESSION_ID,request.getSession().getId());
			logger.info("请求报文："+_requestDataMessage.toJson());
			//调用webService
			DataMessage _responseDataMessage = ControllerDispatcher.dispatch(_webserviceName,_requestDataMessage);
			_responseDataMessage.webServiceName=_requestDataMessage.webServiceName;
			//将响应结果写入输出流
			byte[] _responseBytes = null ;
			if(StringUtils.isNotNull(_format)&&XML_FORMAT.equalsIgnoreCase(_format)){
				_responseBytes = (_responseDataMessage.toXml()).getBytes(ENCODING);
			}else{
				_responseBytes = (_responseDataMessage.toJson()).getBytes(ENCODING);
			}
			bufferedWriteUncompressed(_outputStream, _responseBytes);
			//LogUtils.info("返回报文："+_responseDataMessage.toJson());
			// 记录请求处理结束
			logger.info(_hostName + "的请求处理结束！");
			// 变量清空
			_requestBytes = null;
			_responseBytes = null;
			_requestDataMessage=null;
			_responseDataMessage = null;
		} catch (Exception e) {
			logger.error("处理请求时发生错误", e);
		} finally {
			// 关闭输入输出流
			clostStream(_inputStream, _outputStream);
		}
	}

	/**
	 * Copies all request parameters to attributes.
	 */
	public static void copyParamsToAttributes(
			DataMessage _requestDataMessage,
			HttpServletRequest servletRequest,
			boolean trimParams,
			boolean treatEmptyParamsAsNull,
			boolean ignoreEmptyRequestParams) {
		Enumeration paramNames = servletRequest.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
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
	/**
	 * Prepares parameters for further processing.
	 * @param paramValues	string array of param values
	 * @param trimParams	trim parameters
	 * @param treatEmptyParamsAsNull	empty parameters should be treated as <code>null</code>
	 * @param ignoreEmptyRequestParams	if all parameters are empty, return <code>null</code>
	 */
	public static String[] prepareParameters(
			String[] paramValues,
			boolean trimParams,
			boolean treatEmptyParamsAsNull,
			boolean ignoreEmptyRequestParams) {
		if (trimParams || treatEmptyParamsAsNull || ignoreEmptyRequestParams) {
			int emptyCount = 0;
			int total = paramValues.length;
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				if (paramValue == null) {
					emptyCount++;
					continue;
				}
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
			if ((ignoreEmptyRequestParams == true) && (emptyCount == total)) {
				return null;
			}
		}
		return paramValues;
	}
	
	/**
	 * Parses query from give query string. Values are optionally decoded.
	 */
	public static Map<String,String> parseQuery(String query, boolean decode) {
		Map<String,String> queryMap = new HashMap<String,String>();
		int ndx, ndx2 = 0;
		while (true) {
			ndx = query.indexOf('=', ndx2);
			if (ndx == -1) {
				if (ndx2 < query.length()) {
					queryMap.put(query.substring(ndx2), null);
				}
				break;
			}
			String name = query.substring(ndx2, ndx);
			if (decode) {
				try {
					name = URLDecoder.decode(name,ENCODING);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
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
					value = URLDecoder.decode(value,ENCODING);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
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
			return _byteArrayOutputStream.toByteArray();
		} catch (IOException ex) {
			throw ex;
		} finally {
			_byteArrayOutputStream.close();
		}
	}

	private void bufferedWriteUncompressed(OutputStream _outputStream,
			byte[] _data) throws IOException {
		
		int _position = 0;

		while (_position < _data.length) {
			int _size = Math.min(BUFFERSIZE, _data.length - _position);
			_outputStream.write(_data, _position, _size);
			_position += _size;
		}
		_outputStream.flush();
	}

	/**
	 * 关闭输入输出流
	 * @param inputStream  请求输入流
	 * @param outputStream 响应输出流
	 */
	private void clostStream(InputStream _inputStream, OutputStream _outputStream) {
		if (_outputStream != null) {
			try {
				_outputStream.close();
			} catch (Exception e) {
			}
		}
		if (_inputStream != null) {
			try {
				_inputStream.close();
			} catch (Exception e) {
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Clean up resources
	 * 
	 */
	public void destroy() {
	}
}
