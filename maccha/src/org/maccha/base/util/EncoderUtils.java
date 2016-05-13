package org.maccha.base.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 封装各种格式的编码解码工具类.
 * 1.Commons-Codec的 hex/base64 编码
 * 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape
 * 4.JDK提供的URLEncoder
 * @version 2013-01-15
 */

public class EncoderUtils {
	
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	/**
	 * Hex编码.
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}
	/**
	 * Hex解码.
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw  new RuntimeException(e);
		}
	}
	/**
	 * Base64编码.
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}
	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}
	/**
	 * Base64解码.
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}
	/**
	 * Base62编码。
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}
	/**
	 * Html 转码.
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}
	/**
	 * Xml 转码.
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}
	/**
	 * Xml 解码.
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	public static String urlEncode(String source, String encoding, boolean fallbackToDefaultEncoding) {
		if (source == null)
			return null;
		if (encoding == null)
			return null;
		String str = null;
		try {
			str = URLEncoder.encode(source, encoding);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return str;
	}
	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String source) {
		return urlEncode(source, DEFAULT_URL_ENCODING, true);
	}

	public static String urlDecode(String source, String encoding,boolean fallbackToDefaultDecoding) {
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
	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String source) {
		return urlDecode(source, DEFAULT_URL_ENCODING, true);
	}
	/**
	 * 字符串编码
	 * @param source
	 * @param encoding
	 * @return
	 */
	public static String escape(String source, String encoding) {
		StringBuffer ret = new StringBuffer();
		String enc = urlEncode(source, encoding, true);
		for (StringTokenizer t = new StringTokenizer(enc, "+"); t.hasMoreTokens();) {
			ret.append(t.nextToken());
			if (t.hasMoreTokens())ret.append("%20");
		}
		return ret.toString();
	}
	
	public static String escapeWBlanks(String source, String encoding) {
		if (source == null)return null;
		StringBuffer ret = new StringBuffer();
		String enc = urlEncode(source, encoding, true);
		for (int z = 0; z < enc.length(); z++)
			if (enc.charAt(z) == '+')
				ret.append("%20");
			else
				ret.append(enc.charAt(z));
		return ret.toString();
	}
	/**
	 * 字符串转为Ascii码
	 * @param source
	 * @return
	 */
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
				result.append((char) ch);
			}
		}
		return new String(result);
	}
	/**
	 * 字符串解码
	 * @param source
	 * @param encoding
	 * @return
	 */
	public static String unescape(String source, String encoding) {
		if (source == null)
			return null;
		int len = source.length();
		StringBuffer preparedSource = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			char c = source.charAt(i);
			if (c == '+')
				preparedSource.append("%20");
			else
				preparedSource.append(c);
		}
		return urlDecode(preparedSource.toString(), encoding, true);
	}
	/**
	 * 将字符串从原来的字符集转换成新的字符集
	 * @param input
	 * @param oldEncoding
	 * @param newEncoding
	 * @return
	 */
	public static byte[] changeEncoding(byte input[], String oldEncoding,String newEncoding) {
		if (oldEncoding == null || newEncoding == null)
			return input;
		if (oldEncoding.trim().equalsIgnoreCase(newEncoding.trim()))
			return input;
		byte result[] = input;
		try {
			result = (new String(input, oldEncoding)).getBytes(newEncoding);
		} catch (UnsupportedEncodingException e) {
		}
		return result;
	}

}
