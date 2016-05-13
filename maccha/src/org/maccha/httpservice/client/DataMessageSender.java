package org.maccha.httpservice.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.util.JsonDataMessageParserImpl;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class DataMessageSender {
	public static Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public static void setConnectionTimeout(Integer connectionTimeout) {
		DataMessageSender.connectionTimeout = connectionTimeout;
	}

	public static Integer getConnectionRequestTime() {
		return connectionRequestTime;
	}

	public static void setConnectionRequestTime(Integer connectionRequestTime) {
		DataMessageSender.connectionRequestTime = connectionRequestTime;
	}

	public static Integer getSocketTimeout() {
		return socketTimeout;
	}

	public static void setSocketTimeout(Integer socketTimeout) {
		DataMessageSender.socketTimeout = socketTimeout;
	}

	public static Integer getMaxConnections() {
		return maxConnections;
	}

	public static void setMaxConnections(Integer maxConnections) {
		DataMessageSender.maxConnections = maxConnections;
	}

	public static Integer getPerrouteMaxConnections() {
		return perrouteMaxConnections;
	}

	public static void setPerrouteMaxConnections(Integer perrouteMaxConnections) {
		DataMessageSender.perrouteMaxConnections = perrouteMaxConnections;
	}

	private static Logger logger = Logger.getLogger(DataMessageSender.class);
	public  static String dataMessageServiceName = "/dataMessageService";
	private static String JSON_TYPE = "json";
	private static String XML_TYPE = "xml";

	private static CloseableHttpClient _httpClient;
	private static IdleConnectionMonitorThread _idleConnectionMonitorThread;
	private static PoolingHttpClientConnectionManager _conMgr;
	private static ListeningExecutorService _executorService ;
	
	private volatile static DataMessageSender instance = new DataMessageSender();
	//设置连接超时时间
	//设置连接超时10秒钟根据业务调整
	private static Integer connectionTimeout = 10 * 1000; 
	//设置等待数据超时时间60秒钟 根据业务调整
	private static Integer connectionRequestTime = 60 * 1000; 
	//设置从socket读取数据超时时间60秒钟 根据业务调整
	private static Integer socketTimeout = 60 * 1000; 
	//线程池最大连接数
	private static Integer maxConnections = 400 ;
	//每个网站的请求最大连接数
	private static Integer perrouteMaxConnections = 200;

	private static String CHARSET_ENCODING = "utf-8";
	private static String HTTP_POST = "POST" ;
	private static String HTTP_GET = "GET" ;

	private DataMessageSender() {
	}

	/**
	 * 初始化报文,使用默认配置
	 * 设置连接超时10秒钟根据业务调整
	 * 设置等待数据超时时间60秒钟 根据业务调整
	 * 设置从socket读取数据超时时间60秒钟 根据业务调整
	 * 线程池最大连接数 400
	 * 每个网站的请求最大连接数 200
	 */
	public static void init(){
		init(null,null,null,null,null);
	}
	/**
	 * 初始化报文连接
	 * @param _maxConnections
	 * @param _perrouteMaxConnections
	 * @param _connectionTimeout
	 * @param _connectionRequestTime
	 * @param _socketTimeout
	 */
	public static void init(Integer _maxConnections,Integer _perrouteMaxConnections,Integer _connectionTimeout,
						    Integer _connectionRequestTime,Integer _socketTimeout) {
		if(_connectionTimeout != null && _connectionTimeout.intValue() >0){
			connectionTimeout = _connectionTimeout; 
		}
		if(_connectionRequestTime != null && _connectionRequestTime.intValue() >0){
			//设置等待数据超时时间60秒钟 根据业务调整
			connectionRequestTime = _connectionRequestTime; 
		}
		if(_socketTimeout != null && _socketTimeout.intValue() >0){
			//设置从socket读取数据超时时间60秒钟 根据业务调整
			socketTimeout = _socketTimeout; 
		}
		if(_maxConnections != null && _maxConnections.intValue() >0){
			//线程池最大连接数
			maxConnections = _maxConnections ;
		}
		if(_perrouteMaxConnections != null && _perrouteMaxConnections.intValue() >0){
			//每个网站的请求最大连接数
			perrouteMaxConnections = _perrouteMaxConnections;
		}
        try {
            SSLContext sslContext = SSLContexts.custom().useTLS().build();
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } }, null);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext)).build();
            _conMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            _conMgr.setDefaultSocketConfig(socketConfig);
            MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints).build();
            _conMgr.setDefaultConnectionConfig(connectionConfig);
            _conMgr.setMaxTotal(maxConnections);
            _conMgr.setDefaultMaxPerRoute(perrouteMaxConnections);
            _httpClient = HttpClients.custom().setConnectionManager(_conMgr).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
            _idleConnectionMonitorThread = new IdleConnectionMonitorThread(_conMgr);
    		_idleConnectionMonitorThread.start();
    		//异步调用使用
    		_executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxConnections)) ;
    		logger.info("^^^^^^^DataMessageClient init ,parameter:maxConnections="+maxConnections+",connectionTimeout = "+connectionTimeout+",perrouteMaxConnections="+perrouteMaxConnections+",connectionRequestTime="+connectionRequestTime+",socketTimeout="+socketTimeout);
        } catch (KeyManagementException e) {
        	logger.error("报文请求初始化失败。", e.fillInStackTrace());
        } catch (NoSuchAlgorithmException e) {
        	logger.error("报文请求初始化失败。", e.fillInStackTrace());
        }
	}
	/**
     * 
     */
	public void shutdown() {
		try {
			_httpClient.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			_idleConnectionMonitorThread.shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//同步连接池管理
		try {
			_conMgr.shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//异步连接池
		try {
			_executorService.shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("^^^^^^^DataMessageClient shutdown !^^^^^^");
	}

	/**
	 * 每个网站的请求最大连接数
	 * 
	 * @param _routeConnections key 规则为，主机名/域名:端口号，端口号默认为80，value为该网站最大连接数
	 */
	public void setPerRouteMaxConnections(HashMap<String, Integer> _routeConnections) {
		if (_routeConnections != null && !_routeConnections.isEmpty()) {
			Iterator<String> itrKey = _routeConnections.keySet().iterator();
			HttpHost _httpHost = null;
			while (itrKey.hasNext()) {
				String _strKey = itrKey.next();
				Integer _intMaxConnections = _routeConnections.get(_strKey);
				if (_strKey.indexOf(":") != -1) {
					int intHostPort = 80;
					try {
						intHostPort = Integer.valueOf(_strKey.split(":")[1]).intValue();
					} catch (Exception ex) {
						intHostPort = 80;
					}
					_httpHost = new HttpHost(_strKey.split(":")[0], intHostPort);
				} else {
					_httpHost = new HttpHost(_strKey, 80);
				}
				_conMgr.setMaxPerRoute(new HttpRoute(_httpHost),_intMaxConnections);
			}
		}
	}
	public static DataMessageSender getInstance() {
		return instance;
	}
	/**
	 * 同步调用远程服务
	 * 
	 * @param _requestDataMessage
	 *            请求报文（DataMessage）
	 * @param _responseDataMessage
	 *            返回报文（DataMessage）
	 * @throws Exception
	 */
	public static DataMessage sendXML(String serviceUrl,DataMessage _requestDataMessage) throws Exception {
		DataMessageSender sender = DataMessageSender.getInstance();
		return sender.request(HTTP_POST,serviceUrl,_requestDataMessage.toXml(), null, null, XML_TYPE,null);
	}
	/**
	 * 同步调用远程服务
	 * 
	 * @param requestDataMessage 请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendXML(String serviceUrl,DataMessage _requestDataMessage,DataMessage _responseDataMessage)throws Exception {
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessage.toXml(), null, null,XML_TYPE,_responseDataMessage);
	}
	/**
	 * 同步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static DataMessage sendXML(String serviceUrl,Map<String, String> _params, DataMessage _requestDataMessage)  {
		return DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessage.toXml(), _params, null,XML_TYPE,null);
	}

	/**
	 * 同步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static DataMessage sendJSON(String serviceUrl,Map<String, String> _params, DataMessage _requestDataMessage){
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		DataMessage _responseDataMessage = DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessageJsonText, _params, null,JSON_TYPE,null);
		return _responseDataMessage;
	}

	/**
	 * 同步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static DataMessage sendJSON(String serviceUrl,DataMessage _requestDataMessage) {
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		return DataMessageSender.getInstance().request(HTTP_POST,serviceUrl,_requestDataMessageJsonText, null, null, JSON_TYPE,null);
	}
	/**
	 * 同步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendJSON(String serviceUrl,DataMessage _requestDataMessage,DataMessage _responseDataMessage) {
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl,_requestDataMessageJsonText, null, null, JSON_TYPE,_responseDataMessage);
	}
	
	/**
	 * 异步调用远程服务
	 * @param serviceUrl
	 * @param _requestDataMessage
	 * @param callableHandel 回调函数
	 * @throws Exception
	 */
	 
	public static void sendXML(String serviceUrl,DataMessage _requestDataMessage,final IResultProcessorHandle callableHandel) throws Exception {
		DataMessageSender sender = DataMessageSender.getInstance();
		sender.request(HTTP_POST,serviceUrl,_requestDataMessage.toXml(), null, null, XML_TYPE,_requestDataMessage,null,callableHandel);
	}
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendXML(String serviceUrl,DataMessage _requestDataMessage,DataMessage _responseDataMessage,final IResultProcessorHandle callableHandel)throws Exception {
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessage.toXml(), null, null,XML_TYPE,_requestDataMessage,_responseDataMessage,callableHandel);
	}
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendXML(String serviceUrl,Map<String, String> _params, DataMessage _requestDataMessage,final IResultProcessorHandle callableHandel)  {
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessage.toXml(), _params, null,XML_TYPE,_requestDataMessage,null,callableHandel);
	}	
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendJSON(String serviceUrl,Map<String, String> _params, DataMessage _requestDataMessage,final IResultProcessorHandle callableHandel){
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl, _requestDataMessageJsonText, _params, null,JSON_TYPE,_requestDataMessage,null,callableHandel);
	}

	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendJSON(String serviceUrl,DataMessage _requestDataMessage,final IResultProcessorHandle callableHandel) {
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl,_requestDataMessageJsonText, null, null, JSON_TYPE,_requestDataMessage,null,callableHandel);
	}
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void sendJSON(String serviceUrl,DataMessage _requestDataMessage,DataMessage _responseDataMessage,final IResultProcessorHandle callableHandel) {
		String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
		DataMessageSender.getInstance().request(HTTP_POST,serviceUrl,_requestDataMessageJsonText, null, null, JSON_TYPE,_requestDataMessage,_responseDataMessage,callableHandel);
	}	
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage
	 *            请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void getJSON(String serviceUrl,Map<String, String> _params, final IResultProcessorHandle callableHandel) {
		DataMessageSender.getInstance().request(HTTP_GET,serviceUrl,null, _params, null, JSON_TYPE,null,null,callableHandel);
	}
	/**
	 * 异步调用远程服务
	 * 
	 * @param requestDataMessage 请求报文（DataMessage）
	 * @return 返回responseDataMessage 报文实例（DataMessage)
	 */
	public static void getXML(String serviceUrl,Map<String, String> _params, final IResultProcessorHandle callableHandel) {
		DataMessageSender.getInstance().request(HTTP_GET,serviceUrl,null, _params, null, XML_TYPE,null,null,callableHandel);
	}
	/**
	 * 访问https的网站
	 * 
	 * @param httpclient
	 */
	private static void enableSSL(CloseableHttpClient httpclient) {
	}
	/**
	 * 异步调用请求
	 * @param _method
	 * @param _url
	 * @param _requestText
	 * @param _params
	 * @param _heads
	 * @param reportType
	 * @param _requestDataMessage
	 * @param _responseDataMessage
	 * @param callableHandel
	 */
	 
	private void request(final String _method, final String _url,
			final String _requestText, final Map<String, String> _params,
			final Map<String, String> _heads, final String reportType,final DataMessage _requestDataMessage,
			final DataMessage _responseDataMessage,final IResultProcessorHandle callableHandel ){
		final ListenableFuture<DataMessage> _listenableFuture = _executorService.submit(new Callable<DataMessage>() {
            @Override
            public DataMessage call() throws Exception {
                return request(_method,_url,_requestText, _params, _heads, reportType,_responseDataMessage);
            }
        });
        Futures.addCallback(_listenableFuture, new FutureCallback<DataMessage>() {
            @Override
            public void onSuccess(DataMessage _returnResponseDataMessage) {
            	if(_requestDataMessage != null ){
            		callableHandel.doHandle(_requestDataMessage, _returnResponseDataMessage);
            	}else{
            		DataMessage _requestDataMessage2 =  DataMessage.getRequestDataMessage("");
            		_requestDataMessage2.setParameter(_params);
            		callableHandel.doHandle(_requestDataMessage2, _returnResponseDataMessage);
            	}
            }
            @Override
            public void onFailure(Throwable t) {
            	DataMessage _responseDataMessage = DataMessage.getReponseDataMessage();
            	_responseDataMessage.setError(t.getMessage());
            	callableHandel.doHandle(_requestDataMessage, _responseDataMessage);
            }
        });
	}
	/**
	 * 
	 * @param _method
	 * @param _url
	 * @param _requestText
	 * @param _params
	 * @param _heads
	 * @param reportType
	 * @return
	 * @throws Exception
	 */
	private DataMessage request(String _method, String _url,
			String _requestText, Map<String, String> _params,
			Map<String, String> _heads, String reportType,DataMessage _responseDataMessage ){
		if(_responseDataMessage  == null){
			_responseDataMessage = DataMessage.getReponseDataMessage();
		}
		InputStream _responseStream = null;
		HttpPost httpPost = null ;
		HttpGet httpGet = null ;  
		try {
			//
			String formatParams = null;  
			if (_params != null && !_params.isEmpty()) {
				//将要POST的数据封包
				List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
				Iterator<String> itrKey = _params.keySet().iterator();
				while (itrKey.hasNext()) {
					String _strKey = itrKey.next();
					String _strValue = _params.get(_strKey);
					paramsList.add(new BasicNameValuePair(_strKey, _strValue));
				}
				// 将参数进行utf-8编码  
		        if (null != paramsList && paramsList.size() > 0) {  
		            formatParams = URLEncodedUtils.format(paramsList, CHARSET_ENCODING);  
		        }
			}
			
			//设置请求和传输超时时间
			//1，连接超时：connectionTimeout 指的是连接一个url的连接等待时间。
			//2，读取数据超时：ConnectionRequestTimeout  指的是连接上一个url，获取response的返回等待时间
			//3，SocketTimeout ：从socket读数据的超时时间，即从服务器取响应数据需要等待的时间
			
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(connectionTimeout)
					.setConnectionRequestTimeout(connectionRequestTime)
					.setSocketTimeout(socketTimeout).build();
			//RequestConfig requestConfig = RequestConfig.custom().build();
			CloseableHttpResponse response = null ;
			if (formatParams != null) {  
				_url = _url.indexOf("?") < 0 ? (_url + "?" + formatParams): (_url + "&" + formatParams);  
            }
			if(_url.indexOf("?") != -1){
				_url = _url+"&_format="+reportType;
			}else{
				_url = _url+"?_format="+reportType;
			}
			logger.info("::::::::::::::::::::request _url = " + _url);
			// 如果方法为POST
            if (HTTP_POST.equalsIgnoreCase(_method)) { 
            	httpPost = new HttpPost(_url);
            	if (_heads != null && !_heads.isEmpty()) {
    				Iterator<String> itrKey = _heads.keySet().iterator();
    				while (itrKey.hasNext()) {
    					String _strKey = itrKey.next();
    					String _strValue = _heads.get(_strKey);
    					httpPost.addHeader(_strKey, _strValue);
    				}
    			}
				httpPost.setConfig(requestConfig);
				StringEntity reqEntity = new StringEntity(_requestText, ContentType.create("text/xml", Consts.UTF_8));
				httpPost.setEntity(reqEntity);
				
				response = _httpClient.execute(httpPost);
            }else if (HTTP_GET.equalsIgnoreCase(_method)) {
            	httpGet = new HttpGet(_url);
            	if (_heads != null && !_heads.isEmpty()) {
    				Iterator<String> itrKey = _heads.keySet().iterator();
    				while (itrKey.hasNext()) {
    					String _strKey = itrKey.next();
    					String _strValue = _heads.get(_strKey);
    					httpGet.addHeader(_strKey, _strValue);
    				}
    			}
            	httpGet.setConfig(requestConfig);
				response = _httpClient.execute(httpGet);
            }
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity _entity = response.getEntity();
				try {
					if (_entity != null) {
						_responseStream = new ByteArrayInputStream(EntityUtils.toByteArray(_entity));
					}
				} finally {
					if (response != null) {
						// 会自动释放连接
						EntityUtils.consume(_entity);
					}
				}
			} else {
				_responseDataMessage.setError("服务器连接失败！错误编号：" + statusCode+ ",错误信息：" + response.getStatusLine());
			}
		} catch (ConnectionPoolTimeoutException e) {
			e.printStackTrace();
			_responseDataMessage.setError("服务器连接繁忙！" + e.getMessage());
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			_responseDataMessage.setError("web服务网络请求连接超时出错！" + e.getMessage());
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			_responseDataMessage.setError("服务网络请求繁忙出错！" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			_responseDataMessage.setError(e.getMessage());
		} finally {
			if(httpPost != null)httpPost.releaseConnection();
			if(httpGet != null)httpGet.releaseConnection();
		}
		if (_responseStream != null) {
			try {
				if (JSON_TYPE.equals(reportType)) {
					new JsonDataMessageParserImpl().parseRequest(_responseStream, _responseDataMessage);
				} else if (XML_TYPE.equals(reportType)) {
					_responseDataMessage = Xml2DataMessageUtilty.getDataMessage(_responseStream,_responseDataMessage);
				}
			} catch (Exception ex) {
				//ex.printStackTrace();
				_responseDataMessage.setError("解析报文出错:" + ex.getMessage());
			} finally {
				try {
					_responseStream.close();
				} catch (Exception ex) {
				}
			}
		}
		return _responseDataMessage;
	}

	/**
	 * 
	 * @author Mac Book
	 * 
	 */
	private static class IdleConnectionMonitorThread extends Thread {
		private final PoolingHttpClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
			super();
			this.setName("dataMessageSender-connection-monitor");
			this.setDaemon(true);
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(5000);
						// Close expired connections
						connMgr.closeExpiredConnections();
						// Optionally, close connections
						// that have been idle longer than 30 sec
						connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				// terminate
			}
		}

		public void shutdown() {
			synchronized (this) {
				shutdown = true;
				notifyAll();
			}
		}
	}
}