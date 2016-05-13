package org.maccha.dao.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(DaoUtilsTest.class);
		suite.addTestSuite(SqlConfigResourceTest.class);
		//$JUnit-END$
		return suite;
	}

}
