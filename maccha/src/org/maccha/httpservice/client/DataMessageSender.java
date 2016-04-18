package org.maccha.httpservice.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
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
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.util.JsonDataMessageParserImpl;
import org.maccha.httpservice.util.Xml2DataMessageUtilty;

public class DataMessageSender
{
  private static Logger logger = Logger.getLogger(DataMessageSender.class);
  public static String dataMessageServiceName = "/dataMessageService";
  private static String JSON_TYPE = "json";
  private static String XML_TYPE = "xml";
  private static CloseableHttpClient _httpClient;
  private static IdleConnectionMonitorThread _idleConnectionMonitorThread;
  private static PoolingHttpClientConnectionManager _conMgr;
  private static ListeningExecutorService _executorService;
  private static volatile DataMessageSender instance = new DataMessageSender();

  private static Integer connectionTimeout = Integer.valueOf(10000);

  private static Integer connectionRequestTime = Integer.valueOf(60000);

  private static Integer socketTimeout = Integer.valueOf(60000);

  private static Integer maxConnections = Integer.valueOf(400);

  private static Integer perrouteMaxConnections = Integer.valueOf(200);

  private static String CHARSET_ENCODING = "utf-8";
  private static String HTTP_POST = "POST";
  private static String HTTP_GET = "GET";

  public static Integer getConnectionTimeout()
  {
    return connectionTimeout;
  }

  public static void setConnectionTimeout(Integer connectionTimeout) {
    connectionTimeout = connectionTimeout;
  }

  public static Integer getConnectionRequestTime() {
    return connectionRequestTime;
  }

  public static void setConnectionRequestTime(Integer connectionRequestTime) {
    connectionRequestTime = connectionRequestTime;
  }

  public static Integer getSocketTimeout() {
    return socketTimeout;
  }

  public static void setSocketTimeout(Integer socketTimeout) {
    socketTimeout = socketTimeout;
  }

  public static Integer getMaxConnections() {
    return maxConnections;
  }

  public static void setMaxConnections(Integer maxConnections) {
    maxConnections = maxConnections;
  }

  public static Integer getPerrouteMaxConnections() {
    return perrouteMaxConnections;
  }

  public static void setPerrouteMaxConnections(Integer perrouteMaxConnections) {
    perrouteMaxConnections = perrouteMaxConnections;
  }

  public static void init()
  {
    init(null, null, null, null, null);
  }

  public static void init(Integer _maxConnections, Integer _perrouteMaxConnections, Integer _connectionTimeout, Integer _connectionRequestTime, Integer _socketTimeout)
  {
    if ((_connectionTimeout != null) && (_connectionTimeout.intValue() > 0)) {
      connectionTimeout = _connectionTimeout;
    }
    if ((_connectionRequestTime != null) && (_connectionRequestTime.intValue() > 0))
    {
      connectionRequestTime = _connectionRequestTime;
    }
    if ((_socketTimeout != null) && (_socketTimeout.intValue() > 0))
    {
      socketTimeout = _socketTimeout;
    }
    if ((_maxConnections != null) && (_maxConnections.intValue() > 0))
    {
      maxConnections = _maxConnections;
    }
    if ((_perrouteMaxConnections != null) && (_perrouteMaxConnections.intValue() > 0))
    {
      perrouteMaxConnections = _perrouteMaxConnections;
    }
    try {
      SSLContext sslContext = SSLContexts.custom().useTLS().build();
      sslContext.init(null, new TrustManager[] { new X509TrustManager()
      {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      }
       }, null);

      Registry socketFactoryRegistry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", new SSLConnectionSocketFactory(sslContext)).build();

      _conMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

      SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

      _conMgr.setDefaultSocketConfig(socketConfig);

      MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(2000).build();

      ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();

      _conMgr.setDefaultConnectionConfig(connectionConfig);
      _conMgr.setMaxTotal(maxConnections.intValue());
      _conMgr.setDefaultMaxPerRoute(perrouteMaxConnections.intValue());
      _httpClient = HttpClients.custom().setConnectionManager(_conMgr).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
      _idleConnectionMonitorThread = new IdleConnectionMonitorThread(_conMgr);
      _idleConnectionMonitorThread.start();

      _executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxConnections.intValue()));
      logger.info("^^^^^^^DataMessageClient init ,parameter:maxConnections=" + maxConnections + ",connectionTimeout = " + connectionTimeout + ",perrouteMaxConnections=" + perrouteMaxConnections + ",connectionRequestTime=" + connectionRequestTime + ",socketTimeout=" + socketTimeout);
    } catch (KeyManagementException e) {
      logger.error("报文请求初始化失败。", e.fillInStackTrace());
    } catch (NoSuchAlgorithmException e) {
      logger.error("报文请求初始化失败。", e.fillInStackTrace());
    }
  }

  public void shutdown()
  {
    try
    {
      _httpClient.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    try {
      _idleConnectionMonitorThread.shutdown();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    try
    {
      _conMgr.shutdown();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    try
    {
      _executorService.shutdown();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    logger.info("^^^^^^^DataMessageClient shutdown !^^^^^^");
  }

  public void setPerRouteMaxConnections(HashMap<String, Integer> _routeConnections)
  {
    if ((_routeConnections != null) && (!_routeConnections.isEmpty())) {
      Iterator itrKey = _routeConnections.keySet().iterator();
      HttpHost _httpHost = null;
      while (itrKey.hasNext()) {
        String _strKey = (String)itrKey.next();
        Integer _intMaxConnections = (Integer)_routeConnections.get(_strKey);
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
        _conMgr.setMaxPerRoute(new HttpRoute(_httpHost), _intMaxConnections.intValue());
      }
    }
  }

  public static DataMessageSender getInstance() {
    return instance;
  }

  public static DataMessage sendXML(String serviceUrl, DataMessage _requestDataMessage)
    throws Exception
  {
    DataMessageSender sender = getInstance();
    return sender.request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), null, null, XML_TYPE, null);
  }

  public static void sendXML(String serviceUrl, DataMessage _requestDataMessage, DataMessage _responseDataMessage)
    throws Exception
  {
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), null, null, XML_TYPE, _responseDataMessage);
  }

  public static DataMessage sendXML(String serviceUrl, Map<String, String> _params, DataMessage _requestDataMessage)
  {
    return getInstance().request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), _params, null, XML_TYPE, null);
  }

  public static DataMessage sendJSON(String serviceUrl, Map<String, String> _params, DataMessage _requestDataMessage)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    DataMessage _responseDataMessage = getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, _params, null, JSON_TYPE, null);
    return _responseDataMessage;
  }

  public static DataMessage sendJSON(String serviceUrl, DataMessage _requestDataMessage)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    return getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, null, null, JSON_TYPE, null);
  }

  public static void sendJSON(String serviceUrl, DataMessage _requestDataMessage, DataMessage _responseDataMessage)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, null, null, JSON_TYPE, _responseDataMessage);
  }

  public static void sendXML(String serviceUrl, DataMessage _requestDataMessage, IResultProcessorHandle callableHandel)
    throws Exception
  {
    DataMessageSender sender = getInstance();
    sender.request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), null, null, XML_TYPE, _requestDataMessage, null, callableHandel);
  }

  public static void sendXML(String serviceUrl, DataMessage _requestDataMessage, DataMessage _responseDataMessage, IResultProcessorHandle callableHandel)
    throws Exception
  {
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), null, null, XML_TYPE, _requestDataMessage, _responseDataMessage, callableHandel);
  }

  public static void sendXML(String serviceUrl, Map<String, String> _params, DataMessage _requestDataMessage, IResultProcessorHandle callableHandel)
  {
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessage.toXml(), _params, null, XML_TYPE, _requestDataMessage, null, callableHandel);
  }

  public static void sendJSON(String serviceUrl, Map<String, String> _params, DataMessage _requestDataMessage, IResultProcessorHandle callableHandel)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, _params, null, JSON_TYPE, _requestDataMessage, null, callableHandel);
  }

  public static void sendJSON(String serviceUrl, DataMessage _requestDataMessage, IResultProcessorHandle callableHandel)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, null, null, JSON_TYPE, _requestDataMessage, null, callableHandel);
  }

  public static void sendJSON(String serviceUrl, DataMessage _requestDataMessage, DataMessage _responseDataMessage, IResultProcessorHandle callableHandel)
  {
    String _requestDataMessageJsonText = StringUtils.escape(_requestDataMessage.toJson());
    getInstance().request(HTTP_POST, serviceUrl, _requestDataMessageJsonText, null, null, JSON_TYPE, _requestDataMessage, _responseDataMessage, callableHandel);
  }

  public static void getJSON(String serviceUrl, Map<String, String> _params, IResultProcessorHandle callableHandel)
  {
    getInstance().request(HTTP_GET, serviceUrl, null, _params, null, JSON_TYPE, null, null, callableHandel);
  }

  public static void getXML(String serviceUrl, Map<String, String> _params, IResultProcessorHandle callableHandel)
  {
    getInstance().request(HTTP_GET, serviceUrl, null, _params, null, XML_TYPE, null, null, callableHandel);
  }

  private static void enableSSL(CloseableHttpClient httpclient)
  {
  }

  private void request(String _method, String _url, String _requestText, Map<String, String> _params, Map<String, String> _heads, String reportType, DataMessage _requestDataMessage, DataMessage _responseDataMessage, IResultProcessorHandle callableHandel)
  {
    ListenableFuture _listenableFuture = _executorService.submit(new Callable(_method, _url, _requestText, _params, _heads, reportType, _responseDataMessage)
    {
      public DataMessage call() throws Exception {
        return DataMessageSender.this.request(this.val$_method, this.val$_url, this.val$_requestText, this.val$_params, this.val$_heads, this.val$reportType, this.val$_responseDataMessage);
      }
    });
    Futures.addCallback(_listenableFuture, new FutureCallback(_requestDataMessage, callableHandel, _params)
    {
      public void onSuccess(DataMessage _returnResponseDataMessage) {
        if (this.val$_requestDataMessage != null) {
          this.val$callableHandel.doHandle(this.val$_requestDataMessage, _returnResponseDataMessage);
        } else {
          DataMessage _requestDataMessage2 = DataMessage.getRequestDataMessage("");
          _requestDataMessage2.setParameter(this.val$_params);
          this.val$callableHandel.doHandle(_requestDataMessage2, _returnResponseDataMessage);
        }
      }

      public void onFailure(Throwable t) {
        DataMessage _responseDataMessage = DataMessage.getReponseDataMessage();
        _responseDataMessage.setError(t.getMessage());
        this.val$callableHandel.doHandle(this.val$_requestDataMessage, _responseDataMessage);
      }
    });
  }

  private DataMessage request(String _method, String _url, String _requestText, Map<String, String> _params, Map<String, String> _heads, String reportType, DataMessage _responseDataMessage)
  {
    if (_responseDataMessage == null) {
      _responseDataMessage = DataMessage.getReponseDataMessage();
    }
    InputStream _responseStream = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    try
    {
      String formatParams = null;
      if ((_params != null) && (!_params.isEmpty()))
      {
        List paramsList = new ArrayList();
        Iterator itrKey = _params.keySet().iterator();
        while (itrKey.hasNext()) {
          String _strKey = (String)itrKey.next();
          String _strValue = (String)_params.get(_strKey);
          paramsList.add(new BasicNameValuePair(_strKey, _strValue));
        }

        if ((null != paramsList) && (paramsList.size() > 0)) {
          formatParams = URLEncodedUtils.format(paramsList, CHARSET_ENCODING);
        }

      }

      RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout.intValue()).setConnectionRequestTimeout(connectionRequestTime.intValue()).setSocketTimeout(socketTimeout.intValue()).build();

      CloseableHttpResponse response = null;
      if (formatParams != null) {
        _url = _url + "&" + formatParams;
      }
      if (_url.indexOf("?") != -1)
        _url = _url + "&_format=" + reportType;
      else {
        _url = _url + "?_format=" + reportType;
      }
      logger.info("::::::::::::::::::::request _url = " + _url);

      if (HTTP_POST.equalsIgnoreCase(_method)) {
        httpPost = new HttpPost(_url);
        if ((_heads != null) && (!_heads.isEmpty())) {
          Iterator itrKey = _heads.keySet().iterator();
          while (itrKey.hasNext()) {
            String _strKey = (String)itrKey.next();
            String _strValue = (String)_heads.get(_strKey);
            httpPost.addHeader(_strKey, _strValue);
          }
        }
        httpPost.setConfig(requestConfig);
        StringEntity reqEntity = new StringEntity(_requestText, ContentType.create("text/xml", Consts.UTF_8));
        httpPost.setEntity(reqEntity);

        response = _httpClient.execute(httpPost);
      } else if (HTTP_GET.equalsIgnoreCase(_method)) {
        httpGet = new HttpGet(_url);
        if ((_heads != null) && (!_heads.isEmpty())) {
          Iterator itrKey = _heads.keySet().iterator();
          while (itrKey.hasNext()) {
            String _strKey = (String)itrKey.next();
            String _strValue = (String)_heads.get(_strKey);
            httpGet.addHeader(_strKey, _strValue);
          }
        }
        httpGet.setConfig(requestConfig);
        response = _httpClient.execute(httpGet);
      }
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == 200) {
        HttpEntity _entity = response.getEntity();
        try {
          if (_entity != null)
            _responseStream = new ByteArrayInputStream(EntityUtils.toByteArray(_entity));
        }
        finally {
          if (response != null)
          {
            EntityUtils.consume(_entity);
          }
        }
      } else {
        _responseDataMessage.setError("服务器连接失败！错误编号：" + statusCode + ",错误信息：" + response.getStatusLine());
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
      if (httpPost != null) httpPost.releaseConnection();
      if (httpGet != null) httpGet.releaseConnection();
    }
    if (_responseStream != null)
      try {
        if (JSON_TYPE.equals(reportType))
          new JsonDataMessageParserImpl().parseRequest(_responseStream, _responseDataMessage);
        else if (XML_TYPE.equals(reportType))
          _responseDataMessage = Xml2DataMessageUtilty.getDataMessage(_responseStream, _responseDataMessage);
      }
      catch (Exception ex)
      {
        _responseDataMessage.setError("解析报文出错:" + ex.getMessage());
      } finally {
        try {
          _responseStream.close();
        }
        catch (Exception ex) {
        }
      }
    return _responseDataMessage;
  }

  private static class IdleConnectionMonitorThread extends Thread
  {
    private final PoolingHttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr)
    {
      setName("dataMessageSender-connection-monitor");
      setDaemon(true);
      this.connMgr = connMgr;
    }

    public void run()
    {
      try {
        while (!this.shutdown)
          synchronized (this) {
            wait(5000L);

            this.connMgr.closeExpiredConnections();

            this.connMgr.closeIdleConnections(60L, TimeUnit.SECONDS);
          }
      }
      catch (InterruptedException ex)
      {
      }
    }

    public void shutdown() {
      synchronized (this) {
        this.shutdown = true;
        notifyAll();
      }
    }
  }
}