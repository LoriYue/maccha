package org.maccha.base.util;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EncoderUtilsTest extends TestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncodeHex() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "74657374312e7765696c696e6b65722e636f6d2f6c697665416374696f6e2e616374696f6e3f706c61794c69766556696577266c69766549643d3430323838326537353335393132313830313533353931623933643830303030266f70656e49643d534f765f764f53";
		assertEquals(_str, EncoderUtils.encodeHex(str.getBytes()));
	}

	@Test
	public void testDecodeHex() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "74657374312e7765696c696e6b65722e636f6d2f6c697665416374696f6e2e616374696f6e3f706c61794c69766556696577266c69766549643d3430323838326537353335393132313830313533353931623933643830303030266f70656e49643d534f765f764f53";
		assertEquals(str, new String(EncoderUtils.decodeHex(_str)));
	}

	@Test
	public void testEncodeBase64() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "dGVzdDEud2VpbGlua2VyLmNvbS9saXZlQWN0aW9uLmFjdGlvbj9wbGF5TGl2ZVZpZXcmbGl2ZUlkPTQwMjg4MmU3NTM1OTEyMTgwMTUzNTkxYjkzZDgwMDAwJm9wZW5JZD1TT3Zfdk9T";
		assertEquals(_str, EncoderUtils.encodeBase64(str.getBytes()));
	}

	@Test
	public void testEncodeUrlSafeBase64() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS+";
		String _str = "dGVzdDEud2VpbGlua2VyLmNvbS9saXZlQWN0aW9uLmFjdGlvbj9wbGF5TGl2ZVZpZXcmbGl2ZUlkPTQwMjg4MmU3NTM1OTEyMTgwMTUzNTkxYjkzZDgwMDAwJm9wZW5JZD1TT3Zfdk9TKw";
		assertEquals(_str, EncoderUtils.encodeUrlSafeBase64(str.getBytes()));
	}

	@Test
	public void testDecodeBase64() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "dGVzdDEud2VpbGlua2VyLmNvbS9saXZlQWN0aW9uLmFjdGlvbj9wbGF5TGl2ZVZpZXcmbGl2ZUlkPTQwMjg4MmU3NTM1OTEyMTgwMTUzNTkxYjkzZDgwMDAwJm9wZW5JZD1TT3Zfdk9T";
		assertEquals(str, new String(EncoderUtils.decodeBase64(_str)));
	}

	@Test
	public void testEncodeBase62() {
		String str = "test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "sdrsnkvdhkhmjdqkbnllkhud3bshnmkZbshnm1okZxEhudOhdvckhudBczqmouuodtrprvnonumnrprvnavpcummmmcnodmBczLHuXuHL";
		assertEquals(_str, EncoderUtils.encodeBase62(str.getBytes()));
	}

	@Test
	public void testEscapeHtml() {
		String str = "<span>呵呵</span>";
		String _str = "&lt;span&gt;呵呵&lt;/span&gt;";
		assertEquals(_str, EncoderUtils.escapeHtml(str));
	}

	@Test
	public void testUnescapeHtml() {
		String str = "<span>呵呵</span>";
		String _str = "&lt;span&gt;呵呵&lt;/span&gt;";
		assertEquals(str, EncoderUtils.unescapeHtml(_str));
	}

	@Test
	public void testEscapeXml() {
		String str = "<xml>呵呵</xml>";
		String _str = "&lt;xml&gt;呵呵&lt;/xml&gt;";
		assertEquals(_str, EncoderUtils.escapeXml(str));
	}

	@Test
	public void testUnescapeXml() {
		String str = "<xml>呵呵</xml>";
		String _str = "&lt;xml&gt;呵呵&lt;/xml&gt;";
		assertEquals(str, EncoderUtils.unescapeXml(_str));
	}

	@Test
	public void testUrlEncodeStringStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testUrlEncodeString() {
		String str = "http://test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "http%3A%2F%2Ftest1.weilinker.com%2FliveAction.action%3FplayLiveView%26liveId%3D402882e7535912180153591b93d80000%26openId%3DSOv_vOS";
		assertEquals(_str, EncoderUtils.urlEncode(str));
	}

	@Test
	public void testUrlDecodeStringStringBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testUrlDecodeString() {
		String str = "http://test1.weilinker.com/liveAction.action?playLiveView&liveId=402882e7535912180153591b93d80000&openId=SOv_vOS";
		String _str = "http%3A%2F%2Ftest1.weilinker.com%2FliveAction.action%3FplayLiveView%26liveId%3D402882e7535912180153591b93d80000%26openId%3DSOv_vOS";
		assertEquals(str, EncoderUtils.urlDecode(_str));
	}

	@Test
	public void testEscape() {
		String str = "呵呵";
		String _str = "%E5%91%B5%E5%91%B5";
		assertEquals(_str, EncoderUtils.escape(str, "UTF-8"));
	}

	@Test
	public void testEscapeWBlanks() {
		/*String str = "呵 呵";
		String _str = "%E5%91%B5%20%E5%91%B5";
		assertEquals(_str, EncoderUtils.escapeWBlanks(str, "UTF-8"));*/
		fail("Not yet implemented");
	}

	@Test
	public void testEscapeNonAscii() {
		String str = "呵 呵";
		String _str = "&#21621; &#21621;";
		assertEquals(_str, EncoderUtils.escapeNonAscii(str));
	}

	@Test
	public void testUnescape() {
		String str = "呵呵";
		String _str = "%E5%91%B5%E5%91%B5";
		assertEquals(str, EncoderUtils.unescape(_str, "UTF-8"));
	}

	@Test
	public void testChangeEncoding() throws UnsupportedEncodingException {
		String str1 = "呵 呵";
		assertEquals(str1, new String(EncoderUtils.changeEncoding(str1.getBytes("GBK"), "GBK", "UTF-8")));
	}

}
