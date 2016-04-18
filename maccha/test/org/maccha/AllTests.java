package org.maccha;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha");
		//$JUnit-BEGIN$
		suite.addTest(org.maccha.base.AllTests.suite());
		suite.addTest(org.maccha.dao.AllTests.suite());
		suite.addTest(org.maccha.httpservice.AllTests.suite());
		suite.addTest(org.maccha.spring.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
