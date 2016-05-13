package org.maccha.httpservice.controller;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.httpservice.controller");
		//$JUnit-BEGIN$
		suite.addTestSuite(ControllerDispatcherTest.class);
		//$JUnit-END$
		return suite;
	}

}
