package org.maccha.httpservice.servlet;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.servlet");
		//$JUnit-BEGIN$
		suite.addTestSuite(JsonCrossDomainProxyServletTest.class);
		suite.addTestSuite(DataMessageServiceServletTest.class);
		//$JUnit-END$
		return suite;
	}

}
