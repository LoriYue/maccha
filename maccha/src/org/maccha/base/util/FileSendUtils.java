package org.maccha.base.util;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileSendUtils
{
  public static void main(String[] args)
  {
    sendFile("d:\\000000001412c0ae011412e05d5e0003.zip", "http://localhost:8085/test/testZipUpload.jsp");
  }

  public static boolean sendFile(String fileName, String fileURL) {
    boolean isSucess = false;
    URL urlfile = null;
    HttpURLConnection httpUrl = null;
    OutputStream bos = null;
    try {
      FileInputStream fileOut = new FileInputStream(fileName);

      urlfile = new URL(fileURL);
      HttpURLConnection conn = (HttpURLConnection)urlfile.openConnection();
      conn.setDoOutput(true);
      conn.setRequestMethod("POST");

      bos = conn.getOutputStream();

      int length = 0;
      byte[] buffer = new byte[1024];

      while ((length = fileOut.read(buffer)) != -1) {
        bos.write(buffer, 0, length);
      }
      bos.flush();
      int code = conn.getResponseCode();
      System.out.println(":::::::::::::::" + code);
      if (202 == code)
        isSucess = true;
    }
    catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bos != null) bos.close();
        if (httpUrl != null) httpUrl.disconnect(); 
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    return isSucess;
  }
}