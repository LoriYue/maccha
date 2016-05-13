package org.maccha.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import com.hotid.sendmsg.entity.Channel;

public class CollectionsUtilsTest extends TestCase{

	@Test
	public void testListToSet() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = CollectionsUtils.listToSet(list);
		assertEquals(3, set.size());
		set.add(1);
		assertEquals(3, set.size());
	}

	@Test
	public void testChangeSetToList() {
		Set set = new HashSet();
		set.add(1);
		set.add("2");
		set.add(10.99);
		List list = CollectionsUtils.changeSetToList(set);
		assertEquals(3, list.size());
		list.add(1);
		assertEquals(4, list.size());
	}

	@Test
	public void testArrayToList() {
		Object [] arr = {1, "2", 10.99};
		List list = CollectionsUtils.arrayToList(arr);
		assertEquals(3, list.size());
		assertEquals(1, (Integer)list.get(0)+0);
		assertEquals("2", (String)list.get(1));
		assertEquals(10.99, (Double)list.get(2)+0.00);
	}

	@Test
	public void testUnion() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		Collection collection = CollectionsUtils.union(list, set);
		assertEquals(5, collection.size());
		assertTrue(collection.contains(1));
	}

	@Test
	public void testIntersection() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		Collection collection = CollectionsUtils.intersection(list, set);
		assertEquals(1, collection.size());
		assertTrue(collection.contains(1));
		assertFalse(collection.contains("2"));
	}

	@Test
	public void testDisjunction() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		Collection collection = CollectionsUtils.disjunction(list, set);
		assertEquals(4, collection.size());
		assertFalse(collection.contains(1));
		assertTrue(collection.contains("12"));
	}

	@Test
	public void testSubtract() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		Collection collection = CollectionsUtils.subtract(list, set);
		assertEquals(2, collection.size());
		assertFalse(collection.contains(1));
		assertTrue(collection.contains("2"));
	}

	@Test
	public void testIsEqualCollection() {
		List list = new ArrayList();
		list.add(1);
		list.add("12");
		list.add(110.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		assertTrue(CollectionsUtils.isEqualCollection(list, set));
	}

	@Test
	public void testAddAllCollectionIterator() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Set set = new HashSet();
		set.add(1);
		set.add("12");
		set.add(110.99);
		CollectionsUtils.addAll(list, set.iterator());
		assertEquals(6, list.size());
		assertTrue(list.contains(1));
		assertTrue(list.contains("12"));
		CollectionsUtils.addAll(set, list.iterator());
		assertEquals(6, list.size());
	}

	@Test
	public void testAddAllCollectionEnumeration() {
		fail("Not yet test");
	}

	@Test
	public void testAddAllCollectionObjectArray() {
		List list = new ArrayList();
		list.add(1);
		list.add("2");
		list.add(10.99);
		Object [] elements = {3, "4", new Date()};
		CollectionsUtils.addAll(list, elements);
		assertEquals(6, list.size());
		assertTrue(list.contains(3));
		assertTrue(list.contains("4"));
	}

	@Test
	public void testQueryListStringArrayObjectArray() {
		List list = new ArrayList();
		Map channel1 = new HashMap();
		channel1.put("id", "1");
		channel1.put("status", "1");
		channel1.put("createUserId", "a");
		list.add(channel1);
		Map channel2 = new HashMap();
		channel2.put("id", "2");
		channel2.put("status", "1");
		channel2.put("createUserId", "a");
		list.add(channel2);
		Map channel3 = new HashMap();
		channel3.put("id", "3");
		channel3.put("status", "0");
		channel3.put("createUserId", "a");
		list.add(channel3);
		Map channel4 = new HashMap();
		channel4.put("id", "4");
		channel4.put("status", "1");
		channel4.put("createUserId", "b");
		list.add(channel4);
		String [] _names = {"status", "createUserId"};
		Object [] _values = {"1", "a"};
		List _list = CollectionsUtils.query(list, _names, _values);
		assertEquals(2, _list.size());
	}

	@Test
	public void testQueryListStringObject() {
		List list = new ArrayList();
		Map channel1 = new HashMap();
		channel1.put("id", "1");
		channel1.put("status", "1");
		channel1.put("createUserId", "a");
		list.add(channel1);
		Map channel2 = new HashMap();
		channel2.put("id", "2");
		channel2.put("status", "1");
		channel2.put("createUserId", "a");
		list.add(channel2);
		Map channel3 = new HashMap();
		channel3.put("id", "3");
		channel3.put("status", "0");
		channel3.put("createUserId", "a");
		list.add(channel3);
		Map channel4 = new HashMap();
		channel4.put("id", "4");
		channel4.put("status", "1");
		channel4.put("createUserId", "b");
		list.add(channel4);
		List _list = CollectionsUtils.query(list, "status", "1");
		assertEquals(3, _list.size());
	}

	@Test
	public void testQueryListMap() {
		List list = new ArrayList();
		Map channel1 = new HashMap();
		channel1.put("id", "1");
		channel1.put("status", "1");
		channel1.put("createUserId", "a");
		list.add(channel1);
		Map channel2 = new HashMap();
		channel2.put("id", "2");
		channel2.put("status", "1");
		channel2.put("createUserId", "a");
		list.add(channel2);
		Map channel3 = new HashMap();
		channel3.put("id", "3");
		channel3.put("status", "0");
		channel3.put("createUserId", "a");
		list.add(channel3);
		Map channel4 = new HashMap();
		channel4.put("id", "4");
		channel4.put("status", "1");
		channel4.put("createUserId", "b");
		list.add(channel4);
		Map args = new HashMap();
		args.put("status", "1");
		args.put("createUserId", "a");
		List _list = CollectionsUtils.query(list, args);
		assertEquals(2, _list.size());
	}

}
