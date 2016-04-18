package org.maccha.httpservice.client;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.client");
		//$JUnit-BEGIN$
		suite.addTestSuite(DataMessageSenderTest.class);
		//$JUnit-END$
		return suite;
	}

}
