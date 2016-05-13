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

public class SecurityUtils {
	public final static String ENCRYPT_KEY = "!qaz@wsx" ;
	protected final static Log logger = LogFactory.getLog(SecurityUtils.class);
	private final static String DES = "DES";
	/**
	 * DES加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		//DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		//从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		//创建一个密匙工厂，然后用它把DESKeySpec转换成
		//一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		//Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		//用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		//现在，获取数据并加密
		//正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * @param key 加密密钥,密钥长度必须为8位
	 * @param plainText
	 * @return
	 */
	public final static String decrypt(String key, String plainText) {
		try {
			byte[] byteDate = plainText.getBytes();
			if ((byteDate.length % 2) != 0)return plainText;
			return new String(decrypt(hex2byte(byteDate), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 密码加密
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public final static String encrypt(String key, String encryptTxt) {
		try {
			return byte2hex(encrypt(encryptTxt.getBytes(), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/** 
     * md5或者sha-1加密 
     *  
     * @param inputText  要加密的内容 
     * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写 
     * @return 
     */  
    public static String md5Encrypt(String inputText) {  
        if (inputText == null || "".equals(inputText.trim())) {  
            throw new IllegalArgumentException("请输入要加密的内容");  
        }  
        String encryptText = null;  
        try {  
            MessageDigest m = MessageDigest.getInstance( "md5");  
            m.update(inputText.getBytes("UTF8"));  
            byte s[] = m.digest();  
            return byte2hex(s);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return encryptText;  
    }
	/**
	 * 二行制转字符串,返回十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)hs = hs + "0" + stmp;
			else hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
}
