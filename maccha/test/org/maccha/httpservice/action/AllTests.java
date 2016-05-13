package org.maccha.httpservice.action;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.action");
		//$JUnit-BEGIN$
		suite.addTestSuite(BaseWebServiceActionTest.class);
		//$JUnit-END$
		return suite;
	}

}
