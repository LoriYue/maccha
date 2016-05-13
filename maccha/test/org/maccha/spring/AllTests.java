package org.maccha.spring;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.spring");
		//$JUnit-BEGIN$
		suite.addTestSuite(MessageUtilsTest.class);
		suite.addTestSuite(SpringManagerTest.class);
		suite.addTest(org.maccha.spring.ext.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
