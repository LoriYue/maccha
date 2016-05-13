package org.maccha.base;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.base");
		//$JUnit-BEGIN$
		suite.addTest(org.maccha.base.exception.AllTests.suite());
		suite.addTest(org.maccha.base.metadata.AllTests.suite());
		suite.addTest(org.maccha.base.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
