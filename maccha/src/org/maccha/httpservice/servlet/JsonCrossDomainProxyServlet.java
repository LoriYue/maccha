package org.maccha.httpservice.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.maccha.base.util.StringUtils;
import org.maccha.httpservice.controller.ControllerDispatcher;

public class JsonCrossDomainProxyServlet extends HttpServlet
{
  private static Logger log = Logger.getLogger(ControllerDispatcher.class);
  public static final int BUFFERSIZE = 2048;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
  {
    InputStream inputStream = null;
    OutputStream outputStream = null;
    try {
      System.out.println("::::::::::::::::::::request.getRequestURI() = " + request.getRequestURI());
      System.out.println("::::::::::::::::::::request.getQueryString() = " + request.getQueryString());
      System.out.println("::::::::::::::::::::proxy request.getContentLength() = " + request.getContentLength());
      String strRequestQueryString = request.getQueryString();

      String strProxyDomain = StringUtils.getURLParameter("?" + strRequestQueryString, "_proxy_domain");
      if (StringUtils.isNull(strProxyDomain)) { log.error("代理处理请求时没有发现目标服务器URL,请确认已经传入服务器URL参数!!!");
        return; }
      strProxyDomain = StringUtils.unescape(strProxyDomain);
      System.out.println("::::::::::::::::::::strProxyDomain = " + strProxyDomain);

      inputStream = request.getInputStream();
      outputStream = response.getOutputStream();

      byte[] requestData = dataDecompress(inputStream);

      byte[] responseData = sendClient(strProxyDomain + "/dataMessageService?format=json", requestData);

      bufferedWriteUncompressed(outputStream, responseData);
    }
    catch (Exception e) {
      log.error("代理处理请求时发生错误", e);
    }
    finally {
      clostStream(inputStream, outputStream);
    }
  }

  private byte[] sendClient(String urlStr, byte[] data)
  {
    OutputStream out = null;
    InputStream in = null;
    byte[] requestData = null;
    try {
      URL _url = new URL(urlStr);
      HttpURLConnection connection = (HttpURLConnection)_url.openConnection();
      connection.setUseCaches(false);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setDoInput(true);
      connection.setDoOutput(true);

      out = connection.getOutputStream();
      bufferedWriteUncompressed(out, data);

      in = connection.getInputStream();
      requestData = dataDecompress(in);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      clostStream(in, out);
    }
    return requestData;
  }
  private String stream2String(InputStream in) throws IOException {
    StringBuffer out = new StringBuffer();
    byte[] b = new byte[4096];
    int n;
    while ((n = in.read(b)) != -1) {
      out.append(new String(b, 0, n));
    }
    return out.toString();
  }
  public byte[] dataDecompress(InputStream inputData) throws IOException {
    int count = 0;
    byte[] b = new byte[2048];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      while ((count = inputData.read(b, 0, 2048)) != -1) {
        baos.write(b, 0, count);
      }
      byte[] arrayOfByte1 = baos.toByteArray();
      return arrayOfByte1;
    }
    catch (IOException ioe)
    {
      throw ioe;
    } finally {
      baos.close(); } 
  }

  private void bufferedWriteUncompressed(OutputStream outputStream, byte[] data) throws IOException {
    int position = 0;
    while (position < data.length) {
      int size = Math.min(2048, data.length - position);
      outputStream.write(data, position, size);
      position += size;
    }

    outputStream.flush();
  }

  private void clostStream(InputStream inputStream, OutputStream outputStream)
  {
    if (outputStream != null)
      try {
        outputStream.close();
      }
      catch (Exception e)
      {
      }
    if (inputStream != null)
      try {
        inputStream.close();
      }
      catch (Exception e)
      {
      }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

  public void destroy()
  {
  }
}
