package org.maccha.base.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityUtils
{
  public static final String ENCRYPT_KEY = "!qaz@wsx";
  protected static final Log logger = LogFactory.getLog(SecurityUtils.class);
  private static final String DES = "DES";

  private static byte[] encrypt(byte[] src, byte[] key)
    throws Exception
  {
    SecureRandom sr = new SecureRandom();

    DESKeySpec dks = new DESKeySpec(key);

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey securekey = keyFactory.generateSecret(dks);

    Cipher cipher = Cipher.getInstance("DES");

    cipher.init(1, securekey, sr);

    return cipher.doFinal(src);
  }

  private static byte[] decrypt(byte[] src, byte[] key)
    throws Exception
  {
    SecureRandom sr = new SecureRandom();

    DESKeySpec dks = new DESKeySpec(key);

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey securekey = keyFactory.generateSecret(dks);

    Cipher cipher = Cipher.getInstance("DES");

    cipher.init(2, securekey, sr);

    return cipher.doFinal(src);
  }

  public static final String decrypt(String key, String plainText)
  {
    try
    {
      byte[] byteDate = plainText.getBytes();
      if (byteDate.length % 2 != 0) return plainText;
      return new String(decrypt(hex2byte(byteDate), key.getBytes()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static final String encrypt(String key, String encryptTxt)
  {
    try
    {
      return byte2hex(encrypt(encryptTxt.getBytes(), key.getBytes()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String md5Encrypt(String inputText)
  {
    if ((inputText == null) || ("".equals(inputText.trim()))) {
      throw new IllegalArgumentException("请输入要加密的内容");
    }
    String encryptText = null;
    try {
      MessageDigest m = MessageDigest.getInstance("md5");
      m.update(inputText.getBytes("UTF8"));
      byte[] s = m.digest();
      return byte2hex(s);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return encryptText;
  }

  private static String byte2hex(byte[] b)
  {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1) hs = hs + "0" + stmp; else
        hs = hs + stmp;
    }
    return hs.toUpperCase();
  }

  private static byte[] hex2byte(byte[] b) {
    if (b.length % 2 != 0) throw new IllegalArgumentException("长度不是偶数");
    byte[] b2 = new byte[b.length / 2];
    for (int n = 0; n < b.length; n += 2) {
      String item = new String(b, n, 2);
      b2[(n / 2)] = (byte)Integer.parseInt(item, 16);
    }
    return b2;
  }
}
