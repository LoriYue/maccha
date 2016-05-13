package org.maccha.base.util;

import junit.framework.TestCase;

import org.junit.Test;

public class Base64UtilsTest extends TestCase{

	@Test
	public void testIsBase64String() {
		String isValidString1 = "bG9yaXl1ZQ==";
		String isValidString2 = "@ass";
		assertEquals(true, Base64Utils.isBase64(isValidString1));
		assertEquals(false, Base64Utils.isBase64(isValidString2));
	}

	@Test
	public void testIsBase64Byte() {
		byte octect1 = (byte)'@'; 
		byte octect2 = (byte)'1';
		assertEquals(false, Base64Utils.isBase64(octect1));
		assertEquals(true, Base64Utils.isBase64(octect2));
	}

	@Test
	public void testIsArrayByteBase64() {
		byte[] arrayOctect1 = {(byte)'1', (byte)'-'};
		byte[] arrayOctect2 = {(byte)'1', (byte)'@'};
		assertEquals(false, Base64Utils.isArrayByteBase64(arrayOctect2));
		assertEquals(true, Base64Utils.isArrayByteBase64(arrayOctect1));
	}

	@Test
	public void testDecodeByteArray() {
		fail("Not yet test");
	}

	@Test
	public void testDecodeString() {
		String srt = "5ZG15ZG1";
		assertEquals("呵呵", Base64Utils.decode(srt));
	}

	@Test
	public void testEncodeByteArray() {
		fail("Not yet test");
	}

	@Test
	public void testEncodeString() {
		String srt = "loriyue";
		assertEquals("bG9yaXl1ZQ==", Base64Utils.encode(srt));
	}

}
