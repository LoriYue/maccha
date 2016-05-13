package org.maccha.base.util;


import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

public class ProxyUtilsTest extends TestCase {

	@Test
	public void testGetObject() {
		Map args = new HashMap();
		Object proxy = ProxyUtils.getObject(Map.class, args);
		assertEquals("com.sun.proxy.$Proxy2", proxy.getClass().getName());
	}

	@Test
	public void testInvoke() {
		fail("Not yet implemented");
	}

}
