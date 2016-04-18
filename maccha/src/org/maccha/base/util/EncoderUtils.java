package org.maccha.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

public class EncoderUtils
{
  private static final String DEFAULT_URL_ENCODING = "UTF-8";
  private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
  private static boolean C_NEW_ENCODING_SUPPORTED = true;
  private static boolean C_NEW_DECODING_SUPPORTED = true;

  public static String encodeHex(byte[] input)
  {
    return Hex.encodeHexString(input);
  }

  public static byte[] decodeHex(String input)
  {
    try
    {
      return Hex.decodeHex(input.toCharArray()); 
     } catch (DecoderException e) {
    	  throw new RuntimeException(e);
    }
    
  }

  public static String encodeBase64(byte[] input)
  {
    return Base64.encodeBase64String(input);
  }

  public static String encodeUrlSafeBase64(byte[] input)
  {
    return Base64.encodeBase64URLSafeString(input);
  }

  public static byte[] decodeBase64(String input)
  {
    return Base64.decodeBase64(input);
  }

  public static String encodeBase62(byte[] input)
  {
    char[] chars = new char[input.length];
    for (int i = 0; i < input.length; i++) {
      chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
    }
    return new String(chars);
  }

  public static String escapeHtml(String html)
  {
    return StringEscapeUtils.escapeHtml4(html);
  }

  public static String unescapeHtml(String htmlEscaped)
  {
    return StringEscapeUtils.unescapeHtml4(htmlEscaped);
  }

  public static String escapeXml(String xml)
  {
    return StringEscapeUtils.escapeXml(xml);
  }

  public static String unescapeXml(String xmlEscaped)
  {
    return StringEscapeUtils.unescapeXml(xmlEscaped);
  }

  public static String urlEncode(String source, String encoding, boolean fallbackToDefaultEncoding)
  {
    if (source == null)
      return null;
    if (encoding == null)
      return null;
    String str = null;
    try {
      str = URLEncoder.encode(source, encoding);
    } catch (Exception ex) {
    }
    return str;
  }

  public static String urlEncode(String source)
  {
    return urlEncode(source, "UTF-8", true);
  }

  public static String urlDecode(String source, String encoding, boolean fallbackToDefaultDecoding) {
    if (source == null)
      return null;
    if (encoding == null)
      return null;
    String str = null;
    try {
      str = URLDecoder.decode(source, encoding);
    } catch (Exception ex) {
    }
    return str;
  }

  public static String urlDecode(String source)
  {
    return urlDecode(source, "UTF-8", true);
  }

  public static String escape(String source, String encoding) {
    StringBuffer ret = new StringBuffer();
    String enc = urlEncode(source, encoding, true);
    for (StringTokenizer t = new StringTokenizer(enc, "+"); t.hasMoreTokens(); ) {
      ret.append(t.nextToken());
      if (!t.hasMoreTokens()) continue; ret.append("%20");
    }
    return ret.toString();
  }

  public static String escapeWBlanks(String source, String encoding) {
    if (source == null) return null;
    StringBuffer ret = new StringBuffer();
    String enc = urlEncode(source, encoding, true);
    for (int z = 0; z < enc.length(); z++) {
      if (enc.charAt(z) == '+')
        ret.append("%20");
      else
        ret.append(enc.charAt(z));
    }
    return ret.toString();
  }

  public static String escapeNonAscii(String source) {
    if (source == null)
      return null;
    StringBuffer result = new StringBuffer(source.length() * 2);
    for (int i = 0; i < source.length(); i++) {
      int ch = source.charAt(i);
      if (ch > 255) {
        result.append("&#");
        result.append(ch);
        result.append(";");
      } else {
        result.append((char)ch);
      }
    }

    return new String(result);
  }

  public static String unescape(String source, String encoding) {
    if (source == null)
      return null;
    int len = source.length();
    StringBuffer preparedSource = new StringBuffer(len);
    for (int i = 0; i < len; i++) {
      char c = source.charAt(i);
      if (c == '+')
        preparedSource.append("%20");
      else {
        preparedSource.append(c);
      }
    }
    return urlDecode(preparedSource.toString(), encoding, true);
  }

  public static byte[] changeEncoding(byte[] input, String oldEncoding, String newEncoding) {
    if ((oldEncoding == null) || (newEncoding == null))
      return input;
    if (oldEncoding.trim().equalsIgnoreCase(newEncoding.trim()))
      return input;
    byte[] result = input;
    try {
      result = new String(input, oldEncoding).getBytes(newEncoding);
    } catch (UnsupportedEncodingException e) {
    }
    return result;
  }
}
