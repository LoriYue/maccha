package org.maccha.base.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArrayUtilsTest extends TestCase{
	private ArrayUtils arrayUtils;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		arrayUtils = new ArrayUtils();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddString() {
		arrayUtils.addString("testAddString");
		arrayUtils.addString("testAddString");
		arrayUtils.addString("testGetStringArray");
		String[] strArr = arrayUtils.getStringArray();
		assertEquals(3, strArr.length);
		assertEquals("testAddString", strArr[0]);
		assertEquals("testGetStringArray", strArr[2]);
	}

	@Test
	public void testAddStringArray() {
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		String[] strArr = arrayUtils.getStringArray();
		assertEquals(2, strArr.length);
		assertEquals("testAddString", strArr[0]);
		assertEquals("testGetStringArray", strArr[1]);
	}

	@Test
	public void testGetStringArray() {
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		String[] strArr = arrayUtils.getStringArray();
		assertEquals(2, strArr.length);
		assertEquals("testAddString", strArr[0]);
		assertEquals("testGetStringArray", strArr[1]);
	}

	@Test
	public void testStringContains() {
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		assertTrue(arrayUtils.stringContains("testAddString"));
		assertTrue(arrayUtils.stringContains("testGetStringArray"));
	}

	@Test
	public void testStringRemove() {
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		boolean result = arrayUtils.stringRemove("testAddString");
		assertEquals(true, result);
		String[] strArr = arrayUtils.getStringArray();
		assertEquals(1, strArr.length);
		assertFalse(arrayUtils.stringContains("testAddString"));
		assertTrue(arrayUtils.stringContains("testGetStringArray"));
		result = arrayUtils.stringRemove("testAddString");
		assertEquals(false, result);
		assertEquals(1, strArr.length);
	}

	@Test
	public void testAddIntString() {
		arrayUtils.addInt("1");
		arrayUtils.addInt("2");
		int [] intArr = arrayUtils.getIntArray();
		assertEquals(2, intArr.length);
		assertEquals(1, intArr[0]);
		assertEquals(2, intArr[1]);
	}

	@Test
	public void testAddIntInt() {
		arrayUtils.addInt(1);
		arrayUtils.addInt(2);
		int [] intArr = arrayUtils.getIntArray();
		assertEquals(2, intArr.length);
		assertEquals(1, intArr[0]);
		assertEquals(2, intArr[1]);
	}

	@Test
	public void testGetIntArray() {
		arrayUtils.addInt(1);
		arrayUtils.addInt(2);
		int [] intArr = arrayUtils.getIntArray();
		assertEquals(2, intArr.length);
		assertEquals(1, intArr[0]);
		assertEquals(2, intArr[1]);
	}

	@Test
	public void testClear() {
		arrayUtils.addInt(1);
		arrayUtils.addInt(2);
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		arrayUtils.clear();
		int [] intArr = arrayUtils.getIntArray();
		String[] strArr = arrayUtils.getStringArray();
		assertEquals(0, intArr.length);
		assertEquals(0, strArr.length);
	}

	@Test
	public void testToStringArray() {
		Object [] args = {1, "two", new BigDecimal("3")};
		String [] result = ArrayUtils.toStringArray(args);
		assertEquals(3, result.length);
		assertEquals("1", result[0]);
		assertEquals("two", result[1]);
		assertEquals("3", result[2]);
	}

	@Test
	public void testToIntArray() {
		Object [] args = {1, new BigDecimal("3")};
		int [] result = ArrayUtils.toIntArray(args);
		assertEquals(2, result.length);
		assertEquals(1, result[0]);
		assertEquals(3, result[1]);
	}

	@Test
	public void testToObjectArray() {
		int a = 1;
		String b = "2";
		Date c = new Date();
		List list = new ArrayList();
		list.add(a);
		list.add(b);
		list.add(c);
		Iterator it = list.iterator();
		Object [] result = ArrayUtils.toObjectArray(it);
		assertEquals(3, result.length);
		assertEquals(a, result[0]);
		assertEquals(b, result[1]);
		assertEquals(c, result[2]);
	}

	@Test
	public void testToString() {
		arrayUtils.addInt(1);
		arrayUtils.addInt(2);
		arrayUtils.addStringArray("testAddString,testGetStringArray".split(","));
		assertEquals("[testAddString, testGetStringArray]  [1, 2]", arrayUtils.toString());
	}

}
