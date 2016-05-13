package org.maccha.httpservice;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.httpservice");
		//$JUnit-BEGIN$
		suite.addTestSuite(DataMessageTest.class);
		suite.addTestSuite(CrossDomainProxyActionTest.class);
		suite.addTestSuite(DataSetTest.class);
		suite.addTestSuite(EntityTest.class);
		suite.addTest(org.maccha.httpservice.action.AllTests.suite());
		suite.addTest(org.maccha.httpservice.client.AllTests.suite());
		suite.addTest(org.maccha.httpservice.controller.AllTests.suite());
		suite.addTest(org.maccha.httpservice.exception.AllTests.suite());
		suite.addTest(org.maccha.httpservice.interceptor.impl.AllTests.suite());
		suite.addTest(org.maccha.httpservice.servlet.AllTests.suite());
		suite.addTest(org.maccha.httpservice.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
