package org.maccha.dao.type;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.type");
		//$JUnit-BEGIN$
		suite.addTestSuite(ZipClobStringTypeTest.class);
		suite.addTestSuite(ObjectTypeTest.class);
		suite.addTestSuite(Des3StringTest.class);
		//$JUnit-END$
		return suite;
	}

}
