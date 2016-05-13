package org.maccha.base.util;

import java.lang.reflect.Field;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigUtilsTest extends TestCase {
	static {
		ConfigUtils.initConfig();
	}
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
	public void testInitConfig() throws IllegalArgumentException, IllegalAccessException {
		ConfigUtils configUtils = new ConfigUtils();
		Field field = ClassUtils.getAccessibleField(configUtils, "configMap");
		Map<String,String> configMap = (Map<String,String>)field.get(configUtils);
		assertTrue(configMap.containsValue("123456"));
	}

	@Test
	public void testGetConfigFileClassPath() {
		String configFileClassPath = ConfigUtils.getConfigFileClassPath();
		assertEquals("config.xml", configFileClassPath);
	}

	@Test
	public void testGetValueString() {
		assertEquals("123456", ConfigUtils.getValue("testGetValueString"));
	}

	@Test
	public void testGetBooleanValue() {
		assertEquals(true, ConfigUtils.getBooleanValue("sqlDebugable"));
	}

	@Test
	public void testGetIntValue() {
		assertEquals(120, ConfigUtils.getIntValue("testGetIntValue"));
	}

	@Test
	public void testGetFloatValue() {
		assertEquals(0.12F, ConfigUtils.getFloatValue("testGetFloatValue"));
	}

	@Test
	public void testGetValueStringString() {
		assertEquals("aaa", ConfigUtils.getValue("testGetValueStringString", "testGetValueStringString1"));
	}

	@Test
	public void testGetTextString() {
		assertEquals("test", ConfigUtils.getText("testGetTextString"));
	}

	@Test
	public void testGetTextStringString() {
		assertEquals("test", ConfigUtils.getText("testGetTextStringString", "testGetTextStringString1"));
	}

	@Test
	public void testGetConfigUtils() throws IllegalArgumentException, IllegalAccessException {
		ConfigUtils configUtils = ConfigUtils.getConfigUtils("testGetConfigUtils");
		Field field = ClassUtils.getAccessibleField(configUtils, "catalogName");
		assertEquals("testGetConfigUtils", (String)field.get(configUtils));
	}

	@Test
	public void testGetSubValueStringString() {
		ConfigUtils configUtils = ConfigUtils.getConfigUtils("testGetConfigUtils");
		assertEquals("aaa", configUtils.getSubValue("testGetSubValueStringString", "testGetSubValueStringString1"));
	}

	@Test
	public void testGetSubValueString() {
		ConfigUtils configUtils = ConfigUtils.getConfigUtils("testGetConfigUtils");
		assertEquals("bbb", configUtils.getSubValue("testGetSubValueString"));
	}

	@Test
	public void testGetSubTextStringString() {
		ConfigUtils configUtils = ConfigUtils.getConfigUtils("testGetConfigUtils");
		assertEquals("test1", configUtils.getSubText("testGetSubTextStringString", "testGetSubTextStringString1"));
	}

	@Test
	public void testGetSubTextString() {
		ConfigUtils configUtils = ConfigUtils.getConfigUtils("testGetConfigUtils");
		assertEquals("test2", configUtils.getSubText("testGetSubTextString"));
	}

}
