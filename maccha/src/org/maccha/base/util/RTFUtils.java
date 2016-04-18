package org.maccha.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RTFUtils
{
  public String strToRtf(String content)
    throws Exception
  {
    char[] digital = "0123456789ABCDEF".toCharArray();
    StringBuffer sb = new StringBuffer("");
    byte[] bs = content.getBytes("GBK");

    for (int i = 0; i < bs.length; i++) {
      int bit = (bs[i] & 0xF0) >> 4;
      sb.append("\\'");
      sb.append(digital[bit]);
      bit = bs[i] & 0xF;
      sb.append(digital[bit]);
    }
    return sb.toString();
  }

  public String replaceRTF(String content, String markersign, String replacecontent)
  {
    String target = "";
    try {
      String rc = strToRtf(replacecontent);
      System.out.println(":::::::::::::::::::::::::::::::::::rc = " + rc);

      target = StringUtils.replace(content, "$" + markersign + "$", rc);
    } catch (Exception e) {
    }
    return target;
  }

  public String createSavePath(String path)
  {
    File fDirecotry = new File(path);
    if (!fDirecotry.exists()) {
      fDirecotry.mkdirs();
    }
    return path;
  }

  public String ToSBC(String input)
  {
    char[] c = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == ' ') {
        c[i] = '　';
      }
      else if (c[i] < '') {
        c[i] = (char)(c[i] + 65248);
      }
    }
    return new String(c);
  }

  public String rtf(String tempfilePath, String tagFilepath, HashMap markersignData)
  {
    String targetFilename = RandomGUIDUtil.getUniqueId() + ".rtf";

    String sourcecontent = "";
    InputStream ins = null;
    try {
      ins = new FileInputStream(tempfilePath);
      byte[] b = new byte[1638400];
      if (ins == null) {
        System.out.println("源模板文件不存在");
      }
      int bytesRead = 0;
      while (true)
      {
        bytesRead = ins.read(b, 0, 1638400);

        if (bytesRead == -1) {
          System.out.println("读取模板文件结束");
          break;
        }

        sourcecontent = sourcecontent + new String(b, 0, bytesRead);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        ins.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    String targetcontent = "";
    Iterator keys = markersignData.keySet().iterator();
    String oldText = "";

    int keysfirst = 0;
    while (keys.hasNext()) {
      oldText = (String)keys.next();
      Object newValue = markersignData.get(oldText);
      if (newValue == null)
        newValue = " ";
      String newText = (String)newValue;
      if (keysfirst == 0)
        targetcontent = replaceRTF(sourcecontent, oldText, newText);
      else
        targetcontent = replaceRTF(targetcontent, oldText, newText);
      keysfirst++;
    }

    FileWriter fw = null;
    PrintWriter out = null;
    String strTargetFilePath = createSavePath(tagFilepath) + File.separator + targetFilename;
    try
    {
      fw = new FileWriter(strTargetFilePath, true);
      out = new PrintWriter(fw);
      if ((targetcontent.equals("")) || (targetcontent == ""))
        out.println(sourcecontent);
      else {
        out.println(targetcontent);
      }
      System.out.println("生成文件：" + strTargetFilePath + "成功！");
    } catch (IOException e) {
      e.printStackTrace();
      Object localObject2 = null;
      return localObject2 + "";
    }
    finally
    {
      try
      {
        out.close();
        fw.close();
      } catch (Exception ex) {
      }
    }
    return targetFilename;
  }
}
