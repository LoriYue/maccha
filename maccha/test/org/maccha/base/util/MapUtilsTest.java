package org.maccha.base.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.maccha.base.metadata.ObjectMetadata;

public class MapUtilsTest extends TestCase{
	private MapUtils mapUtils;
	@Before
	public void setUp() {
		Map map = new HashMap();
		map.put("1", "1");
		map.put("2", 2);
		map.put("3", "3");
		map.put("4", "4");
		map.put("5", 5);
		mapUtils = new MapUtils(map);
	}
	@Test
	public void testGetInt() throws Exception {
		assertEquals(1, mapUtils.getInt("1"));
		assertEquals(2, mapUtils.getInt("2"));
	}

	@Test
	public void testGetLong() throws Exception {
		assertEquals(3, mapUtils.getLong("3"));
	}

	@Test
	public void testGetString() throws Exception {
		assertEquals("4", mapUtils.getString("4"));
		assertEquals("5", mapUtils.getString("5"));
	}

	@Test
	public void testSetString() throws Exception {
		mapUtils.setString("6", "6");
		assertEquals("6", mapUtils.getString("6"));
		assertEquals(6, mapUtils.getInt("6"));
	}

	@Test
	public void testSetObject() {
        mapUtils.setObject("7", 7);
        assertEquals(7, mapUtils.getObject("7"));
	}

	@Test
	public void testGetObject() {
		ObjectMetadata o = new ObjectMetadata();
        o.setEntityName("entity");
        mapUtils.setObject("8", o);
        assertEquals(o, mapUtils.getObject("8"));
	}

	@Test
	public void testGetBoolean() {
		mapUtils.setObject("9", true);
		assertEquals(true, mapUtils.getBoolean("9"));
	}

	@Test
	public void testGetNames() {
		Object[] names = mapUtils.getNames();
		assertEquals(5, names.length);
		StringBuffer buffer = new StringBuffer();
		for(Object o : names) {
			buffer.append(o);
		}
		assertTrue(buffer.toString().contains("1"));
		assertTrue(buffer.toString().contains("2"));
		assertTrue(buffer.toString().contains("3"));
		assertTrue(buffer.toString().contains("4"));
		assertTrue(buffer.toString().contains("5"));
		assertFalse(buffer.toString().contains("6"));
	}

	@Test
	public void testGetValues() {
		Object[] values = mapUtils.getValues();
		assertEquals(5, values.length);
		StringBuffer buffer = new StringBuffer();
		for(Object o : values) {
			buffer.append(o);
		}
		assertTrue(buffer.toString().contains("1"));
		assertTrue(buffer.toString().contains("2"));
		assertTrue(buffer.toString().contains("3"));
		assertTrue(buffer.toString().contains("4"));
		assertTrue(buffer.toString().contains("5"));
		assertFalse(buffer.toString().contains("6"));
	}

	@Test
	public void testModelToMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testMapToXml() {
		fail("Not yet implemented");
	}

	@Test
	public void testXmlToMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testToMapArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testMapArrayToMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNum() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNum() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsHas() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testToMapStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToMapStringArrayObjectArray() {
		fail("Not yet implemented");
	}

}
