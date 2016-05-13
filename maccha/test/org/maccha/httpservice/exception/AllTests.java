package org.maccha.httpservice.exception;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.exception");
		//$JUnit-BEGIN$
		suite.addTestSuite(WebServiceExceptionTest.class);
		//$JUnit-END$
		return suite;
	}

}
