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
		ConfigUtils.initConfig();
		ConfigUtils configUtils = new ConfigUtils();
		Field field = ClassUtils.getAccessibleField(configUtils, "configMap");
		Map<String,String> configMap = (Map<String,String>)field.get(configUtils);
		assertEquals("true", configMap.get("sqlDebugable"));
	}

	@Test
	public void testDestroy() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConfigFileClassPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetConfigFileClassPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValueString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBooleanValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIntValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFloatValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValueStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTextString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTextStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConfigUtils() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubValueStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubValueString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubTextStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSubTextString() {
		fail("Not yet implemented");
	}

}
