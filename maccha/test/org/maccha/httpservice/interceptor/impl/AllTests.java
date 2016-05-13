package org.maccha.httpservice.interceptor.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.interceptor.impl");
		//$JUnit-BEGIN$
		suite.addTestSuite(ParametersWebServiceInterceptorTest.class);
		suite.addTestSuite(AroundWebServiceInterceptorTest.class);
		suite.addTestSuite(DefaultWebServiceActionInvocationTest.class);
		//$JUnit-END$
		return suite;
	}

}
