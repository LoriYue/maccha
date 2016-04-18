package org.maccha.dao.interceptor;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.interceptor");
		//$JUnit-BEGIN$
		suite.addTestSuite(ServiceCallMonitorAdvisorTest.class);
		suite.addTestSuite(QueryFunctionInterceptorTest.class);
		suite.addTestSuite(QueryParamerInterceptorTest.class);
		//$JUnit-END$
		return suite;
	}

}
